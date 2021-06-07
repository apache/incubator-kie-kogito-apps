/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.kogito.explainability.global.shap;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.util.CombinatoricsUtils;
import org.kie.kogito.explainability.Config;
import org.kie.kogito.explainability.global.GlobalExplainer;
import org.kie.kogito.explainability.global.pdp.PartialDependencePlotExplainer;
import org.kie.kogito.explainability.model.*;
import org.kie.kogito.explainability.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of the SHAP kernel explainer as per
 * https://proceedings.neurips.cc/paper/2017/file/8a20a8621978632d76c43dfd28b67767-Paper.pdf
 * see also https://github.com/slundberg/shap/blob/master/shap/explainers/_kernel.py
 */
public class ShapKernelExplainer implements GlobalExplainer<CompletableFuture<double[][][]>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(PartialDependencePlotExplainer.class);

    private final ShapConfig.linkType link;
    private final PredictionProvider model;
    private final double[][] backgroundData;
    private final double[][] modelnull;
    private final double[][] linkNull;
    private final double[] fnull;
    private final int rows;
    private final int cols;
    private final int outputSize;
    private Integer numSamples;
    private ArrayList<ShapSample> samplesAdded;
    private ArrayList<Integer> varyingFeatureGroups;
    private int numVarying;
    private HashMap<Integer, Integer> masksUsed;
    private final Random rn = new Random();

    /**
     * Define a ShapKernelExplainer.
     *
     * @param model: The PredictionProvider to be explained
     * @param config: A ShapConfig object with the configuration for this particular explainer
     * @param background: The background data used to define the context for any particular model
     *        output. This should be a representative sample of the data, to provide a useful
     *        null background for the model. Automated guidance for background data selection
     *        is a WIP.
     *
     * @return ShapKernelExplainer object
     */
    public ShapKernelExplainer(PredictionProvider model,
            ShapConfig config,
            List<PredictionInput> background)
            throws InterruptedException, ExecutionException, TimeoutException {
        this.link = config.getLink();
        this.model = model;
        this.backgroundData = MatrixUtils.matrixFromPredictionInput(background);

        // get shapes of input and output data
        int[] shape = MatrixUtils.getShape(this.backgroundData);
        this.rows = shape[0];
        this.cols = shape[1];

        if (this.rows > 100) {
            LOGGER.debug("Warning: Background data sets larger than 100 samples might be slow!");
        }

        // establish background data
        List<PredictionOutput> model_out = model.predictAsync(background)
                .get(Config.INSTANCE.getAsyncTimeout(), Config.INSTANCE.getAsyncTimeUnit());
        this.modelnull = MatrixUtils.matrixFromPredictionOutput(model_out);
        this.outputSize = MatrixUtils.getShape(this.modelnull)[1];

        //compute the mean of each column
        this.fnull = MatrixUtils.sum(
                MatrixUtils.matrixMultiply(this.modelnull, 1. / this.rows),
                MatrixUtils.Axis.ROW);
        this.linkNull = MatrixUtils.rowVector(this.link(this.fnull));

        // track number of samples
        this.numSamples = config.getNsamples();
        if (this.numSamples == null) {
            this.setNumSamples(2048 + (2 * this.cols));
        }

        // lower number of samples if it's greater than total feature permutation size
        if (this.cols <= 30) {
            int maxSamples = (int) Math.pow(2, this.cols) - 2;
            if (maxSamples < this.numSamples) {
                this.setNumSamples(maxSamples);
            }
        }
    }

    /**
     * Update the number of samples for the explanation
     *
     * @param numSamples: The new number of samples
     */
    private void setNumSamples(int numSamples) {
        this.numSamples = numSamples;
    }

    /**
     * The link function calculation for the explanation.
     * If link is IDENTITY: link: f(x) = x
     * else: link: f(x) = logit(x)
     *
     * @param x: the input to the link function
     *
     * @returns link(x)
     */
    private double link(double x) {
        if (this.link.equals(ShapConfig.linkType.IDENTITY)) {
            return x;
        } else {
            return Math.log(x / (1 - x));
        }
    }

    /**
     * Vector version of the link function
     *
     * @param v: the input to the link function
     *
     * @returns link(v)
     */
    private double[] link(double[] v) {
        return Arrays.stream(v)
                .map(this::link)
                .toArray();
    }

    /**
     * Determine which features vary across the background data and this particular input.
     * If the feature has one value across all background data points and the input, it does not vary.
     *
     * @param input: The PredictionInput to look for variance with, in conjunction with the background data
     */
    private void setVaryingFeatureGroups(PredictionInput input) {
        this.varyingFeatureGroups = new ArrayList<>();
        double[] inputVector = MatrixUtils.matrixFromPredictionInput(input)[0];
        double[] columnFeatures = new double[this.rows + 1];
        for (int col = 0; col < this.cols; col++) {
            System.arraycopy(MatrixUtils.getCol(this.backgroundData, col),
                    0, columnFeatures, 0, this.rows);
            columnFeatures[this.rows] = inputVector[col];
            long uniques = Arrays.stream(columnFeatures).distinct().count();
            if (uniques > 1) {
                this.varyingFeatureGroups.add(col);
            }
        }
        this.numVarying = this.varyingFeatureGroups.size();
    }

    /**
     * Normalize the weight vector to sum to 1.
     *
     * @param v: The vector to be normalized.
     *
     * @return The normalized vector
     */
    private double[] normalizeWeightVector(double[] v) {
        double[][] expanded = MatrixUtils.rowVector(v);
        double sum = MatrixUtils.sum(expanded, MatrixUtils.Axis.COLUMN)[0];
        if (sum == 0) {
            return v;
        } else {
            return MatrixUtils.matrixMultiply(expanded, 1 / sum)[0];
        }
    }

    /**
     * Add a sample to the WLRR computation
     *
     * @param pi: The specific input to generated the synthetic data sample
     * @param combination: The combination of features to include from the input
     * @param weight: The weight of this sample
     * @param inverse: Flag to add the complement of the specified sample instead of the specified sample.
     * @param fixed: Is this sample from a fully enumerated subset? If so, the final weight for the
     *        sample is known at creation time, and does not need to be changed. Otherwise, we'll
     *        need to readjust the given weight after we randomly choose samples.
     */
    private void addSample(PredictionInput pi, List<Integer> combination, double weight,
            boolean inverse, boolean fixed) {
        boolean[] mask = new boolean[this.cols];
        if (inverse) {
            for (int i = 0; i < numVarying; i++) {
                mask[this.varyingFeatureGroups.get(i)] = true;
            }
        }

        for (int i = 0; i < combination.size(); i++) {
            mask[this.varyingFeatureGroups.get(combination.get(i))] = !inverse;
        }
        int maskHash = this.hashMask(mask);
        if (this.masksUsed.containsKey(maskHash)) {
            ShapSample previousSample = this.samplesAdded.get(this.masksUsed.get(maskHash));
            previousSample.incrementWeight();
        } else {
            ShapSample sample = new ShapSample(pi, mask, this.backgroundData, weight, fixed);
            // map index in the samplesAdded list to the unique hash of this mask
            this.masksUsed.put(maskHash, this.samplesAdded.size());
            this.samplesAdded.add(sample);
        }
    }

    /**
     * Hash a boolean mask array. Hashing is done by treating the boolean array of size n
     * as an n-digit binary number, and then computing the base-10 integer represented by this binary.
     *
     * @param mask: The boolean mask to be hashed.
     *
     * @returns the mask hash
     */
    private int hashMask(boolean[] mask) {
        int maskSize = mask.length;
        int hash = 0;
        for (int i = 0; i < maskSize; i++) {
            hash += mask[i] ? Math.pow(2, (maskSize - i - 1)) : 0;
        }
        return hash;
    }

    /**
     * Compute the shap values for a specific prediction
     *
     * @param prediction: The prediction to be explained.
     *
     * @returns the shap values for this prediction, of shape [n_model_outputs x n_features]
     */
    private double[][] explain(Prediction prediction) throws InterruptedException, ExecutionException {
        this.samplesAdded = new ArrayList<>();
        PredictionInput pi = prediction.getInput();
        PredictionOutput po = prediction.getOutput();
        if (pi.getFeatures().size() != this.cols) {
            throw new IllegalArgumentException(String.format(
                    "Prediction input feature count (%d) does not match background data feature count (%d)",
                    pi.getFeatures().size(), this.cols));
        }
        if (po.getOutputs().size() != this.outputSize) {
            throw new IllegalArgumentException(String.format(
                    "Prediction output size (%d) does not match background data output size (%d)",
                    po.getOutputs().size(), this.outputSize));
        }

        double[][] poMatrix = MatrixUtils.matrixFromPredictionOutput(po);

        //first find varying features
        this.setVaryingFeatureGroups(pi);

        double[][] out = new double[this.outputSize][this.cols];

        // if no features vary, then the features do not effect output, and all shap values are zero.
        if (this.numVarying == 0) {
            return out;
        } else if (this.numVarying == 1)
        // if 1 feature varies, this feature has all the effect
        {
            double[] diff = MatrixUtils.matrixDifference(poMatrix, this.linkNull)[0];
            for (int i = 0; i < this.outputSize; i++) {
                out[i][this.varyingFeatureGroups.get(0)] = diff[i];
            }
            return out;
        } else
        // if more than 1 feature varies, we need to perform WLR
        {
            // establish sizes of feature permutations (called subsets)
            ShapStatistics shapStats = this.computeSubsetStatistics();

            // weight each subset by number of features
            this.initializeWeights(shapStats);

            // add all fully enumerated subsets
            this.addCompleteSubsets(shapStats, pi);

            // renormalize weights after full subsets have been added
            this.renormalizeWeights(shapStats);

            // sample non-fully enumerated subsets
            this.addNonCompleteSubsets(shapStats, pi);

            // run the synthetic data generated through the model
            double[][] expectations = this.runSyntheticData();

            // run the wlr model over the synthetic data results
            return this.solve(expectations, poMatrix[0]);
        }
    }

    /**
     * Create a shap statistics object for this explanation, given the configuration for this explainer.
     *
     * @returns ShapStatics object
     */
    private ShapStatistics computeSubsetStatistics() {
        // the size of the range (0, numVarying//2) is the number of possible feature permutations
        // above numVarying//2 all sets are complements of earlier sets
        int numSubsetSizes = (int) Math.ceil((this.numVarying - 1) / 2.);

        // how many of those subsets have a complement set? If numVarying is even, all. If odd, all but the "middle" set
        int largestPairedSubsetSize = numVarying % 2 == 1 ? numSubsetSizes : numSubsetSizes - 1;

        // compute the number of subsets at each size
        // this can be a potentially colossal number.
        // If we get an arithmetic error, truncate the result to numSamples^2
        int[] numSubsetsAtSize = new int[numSubsetSizes + 1];
        for (int i = 1; i < numSubsetSizes + 1; i++) {
            try {
                numSubsetsAtSize[i] = (int) CombinatoricsUtils.binomialCoefficient(this.numVarying, i);
            } catch (MathArithmeticException e) {
                numSubsetsAtSize[i] = this.numSamples * this.numSamples;
            }
        }

        int numSamplesRemaining = this.numSamples;
        return new ShapStatistics(numSubsetSizes, largestPairedSubsetSize, numSubsetsAtSize, numSamplesRemaining);
    }

    /**
     * Set the weights for each subset size. This is computed based on the total number of varying features,
     * the number of permutations with $n features, and whether there is a complement set to this subset
     *
     * @param shapStats: The ShapStatistics object for this explanation
     */
    private void initializeWeights(ShapStatistics shapStats) {
        // compute the weighting for a subset of a particular size
        double[] rawWeights = new double[shapStats.getNumSubsetSizes() + 1];
        for (int subsetSize = 1; subsetSize <= shapStats.getNumSubsetSizes(); subsetSize++) {
            double weight = (this.numVarying - 1.) / (subsetSize * (this.numVarying - subsetSize));
            // the inverse subset has the same weight
            if (subsetSize <= shapStats.getLargestPairedSubsetSize()) {
                weight *= 2;
            }
            rawWeights[subsetSize] = weight;
        }
        double[] weightOfSubsetSize = normalizeWeightVector(rawWeights);
        shapStats.setWeightOfSubsetSize(weightOfSubsetSize);
        shapStats.setRemainingWeights(Arrays.copyOf(weightOfSubsetSize, weightOfSubsetSize.length));
    }

    /**
     * For every subset that we can fully evaluate (ie, subsetSize <= samplesRemaining), add all samples
     * from the subset
     *
     * @param shapStats: The ShapStatistics object for this explanation
     * @param pi: The PredictionInput for this explanation
     */
    private void addCompleteSubsets(ShapStatistics shapStats, PredictionInput pi) {
        this.masksUsed = new HashMap<>();

        // fill out all subsets that can be completely filled
        for (int subsetSize = 1; subsetSize < shapStats.getNumSubsetSizes() + 1; subsetSize++) {
            // get n subsets at particular size
            int numSubsets = shapStats.getNumSubsetsAtSize()[subsetSize];

            // if inverse exists, double size
            numSubsets *= subsetSize <= shapStats.getLargestPairedSubsetSize() ? 2 : 1;
            double samplingWeight = shapStats.getRemainingWeights()[subsetSize];
            // if we have enough samples for the entirety of this subset:
            if (shapStats.getNumSamplesRemaining() * samplingWeight >= numSubsets) {
                shapStats.incrementNumFullSubsets();
                shapStats.decreaseNumSamplesRemainingBy(numSubsets);
                double[] remainingWeights = shapStats.getRemainingWeights();
                remainingWeights[subsetSize] = 0;
                shapStats.setRemainingWeights(normalizeWeightVector(remainingWeights));

                Iterator<int[]> combinations = CombinatoricsUtils.combinationsIterator(this.numVarying, subsetSize);
                double individualWeight = shapStats.getWeightOfSubsetSize()[subsetSize] / numSubsets;
                while (combinations.hasNext()) {
                    List<Integer> combination = Arrays.stream(combinations.next()).boxed().collect(Collectors.toList());
                    addSample(pi, combination, individualWeight, false, true);
                    if (subsetSize <= shapStats.getLargestPairedSubsetSize()) {
                        addSample(pi, combination, individualWeight, true, true);
                    }
                }
            } else {
                break;
            }
        }
    }

    /**
     * Renormalize the remaining weights such that the weight vector for all non-enumerated subsets
     * sums to 1.
     *
     * @param shapStats: The ShapStatistics object for this explanation
     */
    private void renormalizeWeights(ShapStatistics shapStats) {
        //grab another copy of the normalized weights
        double[] weightOfSubsetSize = shapStats.getWeightOfSubsetSize();
        double[] remainingWeights = Arrays.copyOf(weightOfSubsetSize, weightOfSubsetSize.length);
        for (int i = 0; i < remainingWeights.length; i++) {
            if (i < shapStats.getLargestPairedSubsetSize()) {
                remainingWeights[i] /= 2;
            }
        }
        double[] nonFullRemainingWeights = Arrays.copyOfRange(remainingWeights, shapStats.getNumFullSubsets() + 1,
                shapStats.getNumSubsetSizes() + 1);
        shapStats.setFinalRemainingWeights(this.normalizeWeightVector(nonFullRemainingWeights));
    }

    /**
     * For every subset that we cannot fully evaluate (ie, subsetSize > samplesRemaining), grab a
     * random amount of subset samples, weighted by the subset weight
     *
     * @param shapStats: The ShapStatistics object for this explanation
     * @param pi: The PredictionInput for this explanation
     */
    private void addNonCompleteSubsets(ShapStatistics shapStats, PredictionInput pi) {
        if (shapStats.getNumFullSubsets() < shapStats.getNumSubsetSizes()) {
            // draw a bunch of random samples from remaining subsets
            List<Integer> subsetSizesRemaining = IntStream
                    .range(shapStats.getNumFullSubsets() + 1, shapStats.getNumSubsetSizes() + 1)
                    .boxed()
                    .collect(Collectors.toList());

            List<Double> subsetSizeWeights = Arrays.stream(shapStats.getFinalRemainingWeights())
                    .boxed()
                    .collect(Collectors.toList());

            RandomChoice<Integer> subsetSampler = new RandomChoice<>(subsetSizesRemaining, subsetSizeWeights);
            List<Integer> sizeSamples = subsetSampler.sample(shapStats.getNumSamplesRemaining() * 4, this.rn);
            List<Integer> maskSizes = IntStream.range(0, this.numVarying).boxed().collect(Collectors.toList());

            int sampleIdx = 0;
            while (shapStats.getNumSamplesRemaining() > 0) {
                int subsetSize = sizeSamples.get(sampleIdx);
                sampleIdx += 1;
                Collections.shuffle(maskSizes);
                List<Integer> maskIdxs = maskSizes.subList(0, subsetSize);
                this.addSample(pi, maskIdxs, 1., false, false);
                shapStats.decreaseNumSamplesRemainingBy(1);

                // add compliment if possible
                if (shapStats.getNumSamplesRemaining() > 0 && subsetSize <= shapStats.getLargestPairedSubsetSize()) {
                    this.addSample(pi, maskIdxs, 1., true, false);
                    shapStats.decreaseNumSamplesRemainingBy(1);
                }
            }

            this.normalizeSampleWeights(shapStats);
        }
    }

    /**
     * For the non-fully-enumerated subsets, readjust the sample weighting based on the number
     * of randomly chosen samples at each subset size. This is necessary because the sample weighting
     * depends on the number of samples at a particular subset size, which can only be known after
     * we have randomly chosen these samples.
     *
     * @param shapStats: The ShapStatistics object for this explanation
     */
    private void normalizeSampleWeights(ShapStatistics shapStats) {
        double nonFullWeight = 0;
        for (int i = shapStats.getNumFullSubsets() + 1; i < shapStats.getNumSubsetSizes() + 1; i++) {
            nonFullWeight += shapStats.getWeightOfSubsetSize()[i];
        }

        double nonFixedWeight = 0.;
        for (int i = 0; i < this.samplesAdded.size(); i++) {
            if (!this.samplesAdded.get(i).isFixed()) {
                nonFixedWeight += this.samplesAdded.get(i).getWeight();
            }
        }

        for (int i = 0; i < this.samplesAdded.size(); i++) {
            ShapSample sample = this.samplesAdded.get(i);
            if (!sample.isFixed()) {
                sample.setWeight(sample.getWeight() * nonFullWeight / nonFixedWeight);
            }
        }
    }

    /**
     * Pass the synthetic data samples through the model. For each sample, the expectation is
     * the mean output of the model over the entirely of that sample's synthetic data, subtracted
     * from the mean output of the model over the background data.
     *
     * @returns the expectations of the model over the synthetic data, of shape [nsamples x modelOutputSize]
     */
    private double[][] runSyntheticData() throws InterruptedException, ExecutionException {
        double[][] expectations = new double[this.samplesAdded.size()][this.outputSize];
        for (int i = 0; i < this.samplesAdded.size(); i++) {
            List<PredictionInput> pis = this.samplesAdded.get(i).getSyntheticData();
            List<PredictionOutput> pos = this.model.predictAsync(pis).get();

            double[][] posMatrix = MatrixUtils.matrixFromPredictionOutput(pos);
            double[] expectation = MatrixUtils.sum(
                    MatrixUtils.matrixMultiply(posMatrix, 1. / this.rows),
                    MatrixUtils.Axis.ROW);
            expectation = this.link(expectation);
            expectations[i] = MatrixUtils.matrixDifference(MatrixUtils.rowVector(expectation), this.linkNull)[0];
        }
        return expectations;
    }

    /**
     * Create a WLR model to explain how the model depends on each particular feature.
     *
     * @param expectations: The expectations of each sample
     * @param poMatrix: The predictionOutputs for this explanation's prediction
     *
     * @returns the shap values as found by the WLR
     */
    private double[][] solve(double[][] expectations, double[] poMatrix) {
        double[][] shapVals = new double[this.outputSize][this.cols];
        double[][] xs = new double[this.samplesAdded.size()][this.cols];
        double[] ws = new double[this.samplesAdded.size()];
        double[] ys = new double[this.samplesAdded.size()];

        for (int output = 0; output < this.outputSize; output++) {
            int dropIdx = this.varyingFeatureGroups.get(this.varyingFeatureGroups.size() - 1);

            for (int i = 0; i < this.samplesAdded.size(); i++) {
                for (int j = 0; j < this.cols; j++) {
                    xs[i][j] = this.samplesAdded.get(i).getMask()[j] ? 1. : 0.;
                }
                ws[i] = this.samplesAdded.get(i).getWeight();
                ys[i] = expectations[i][output];
            }

            double outputChange = this.link(poMatrix[output]) - this.link(this.fnull[output]);

            // drop a feature for regularization
            double[][] dropMask = MatrixUtils.rowVector(MatrixUtils.getCol(xs, dropIdx));
            double[][] dropEffect = MatrixUtils.matrixMultiply(dropMask, outputChange);
            double[] adjY = MatrixUtils.matrixDifference(MatrixUtils.rowVector(ys), dropEffect)[0];
            List<Integer> included = new ArrayList<>();
            this.varyingFeatureGroups.forEach(v -> {
                if (v != dropIdx) {
                    included.add(v);
                }
            });
            double[][] includeMask = MatrixUtils.transpose(MatrixUtils.getCols(xs, included));
            double[][] maskDiff = MatrixUtils.transpose(MatrixUtils.matrixRowDifference(includeMask, dropMask[0]));

            shapVals[output] = this.runWLRR(maskDiff, adjY, ws, outputChange, dropIdx);

        }
        return shapVals;
    }

    /**
     * Run the WLR model over the expectations.
     *
     * @param maskDiff: The mask matrix, not including the regularization feature
     * @param adjY: The expected model outputs, adjusted for dropping the regularization feature
     * @param ws: The weights of each sample
     * @param outputChange: The raw difference between the model output and the null output
     * @param dropIdx: The regularization feature index
     *
     * @returns the shap values as found by the WLR
     */
    // run the WLRR for a single output
    private double[] runWLRR(double[][] maskDiff, double[] adjY, double[] ws, double outputChange, int dropIdx) {
        WeightedLinearRegressionResults wlrr = WeightedLinearRegression.fit(maskDiff, adjY,
                ws, false, this.rn);
        double[] coeffs = wlrr.getCoefficients();
        int usedCoefs = 0;
        double[] shapSlice = new double[this.cols];
        for (int i = 0; i < this.varyingFeatureGroups.size(); i++) {
            int idx = this.varyingFeatureGroups.get(i);
            if (idx != dropIdx) {
                shapSlice[idx] = coeffs[usedCoefs];
                usedCoefs += 1;
            }
        }
        shapSlice[dropIdx] = outputChange - Arrays.stream(coeffs).sum();
        return shapSlice;
    }

    /**
     * Shap from model and metadata. Shap cannot do this, use
     *
     * @returns IllegalArgumentException
     */
    @Override
    public CompletableFuture<double[][][]> explainFromMetadata(PredictionProvider model,
            PredictionProviderMetadata metadata) {
        throw new IllegalArgumentException("Shap cannot be performed using PredictionProviderMetadata. Please " +
                "use explainFromPredictions instead.");
    }

    /**
     * Perform SHAP from a model and predictions.
     *
     * @param model: The model to be explained
     * @param predictions: An iterable of model predictions
     * @returns IllegalArgumentException
     */
    @Override
    public CompletableFuture<double[][][]> explainFromPredictions(PredictionProvider model,
            Collection<Prediction> predictions)
            throws InterruptedException, ExecutionException {
        double[][][] shapValues = new double[predictions.size()][this.outputSize][this.cols];
        Iterator<Prediction> predictionIterator = predictions.iterator();
        for (int i = 0; i < predictions.size(); i++) {
            shapValues[i] = this.explain(predictionIterator.next());
        }
        return CompletableFuture.completedFuture(shapValues);
    }

}
