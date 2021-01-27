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

package org.kie.kogito.explainability.utils;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * Performs a weighted linear regression over the provided features, observations, and weights
 * The algorithm is modified from modified from Dr. Walt Fair's WLR algorithm here:
 * https://www.codeproject.com/Articles/25335/An-Algorithm-for-Weighted-Linear-Regression?msg=4467580#xx4467580xx
 * We use Dr. Debabrata DasGupta's In-Place Matrix Inversion by Modified Gauss-Jordan algorithm to perform matrix
 * inversion, as described here:
 * https://www.researchgate.net/publication/271296470_In-Place_Matrix_Inversion_by_Modified_Gauss-Jordan_Algorithm,
 * */
public class WeightedLinearRegression {
    boolean intercept;
    boolean fitPerformed;

    private double [][] features;
    private double [] observations;
    private double [] sampleWeights;

    private double[] coefficients;
    private int nfeatures; //this is i
    private int nsamples; //this is j

    // used for jittering
    private final Random rn = new Random();


    /**
     * Create a WLR model.
     *
     * @param intercept Whether or not to include an intercept in the regression calculation.
     */
    public WeightedLinearRegression(boolean intercept) {
        this.intercept = intercept;
    }

    // MAIN ALGORITHM ==================================================================================================
    /**
     * Fit the WLR model to the features.
     *
     * @param features An {@code nsamples features nfeatures} array of doubles; each row contains one features point of size [nfeatures]
     * @param observations An {@code nsamples} array, where y[n] is the observation for features point n.
     * @param sampleWeights An {@code nsamples} array, where sampleWeights[n] is the weighting of features point n.
     *
     * @return C, an {@code nfeatures} array of coefficients as computed by the regression.
     * In the case where {@code intercept} is true, the last value of {@code C} is the intercept.
     *
     */
    public double[] fit(double[][] features, double[] observations, double[] sampleWeights)
            throws IllegalArgumentException, ArithmeticException {
        // if we want to compute an intercept, add a dummy feature at last column.
        if (this.intercept) {
            this.nfeatures = features[0].length + 1;
            this.nsamples = observations.length;
            this.features = new double[this.nsamples][this.nfeatures];
            for (int i = 0; i < this.nsamples; i++) {
                System.arraycopy(features[i], 0, this.features[i], 0, this.nfeatures - 1);
                this.features[i][this.nfeatures-1] = 1;
            }
        } else {
            this.nfeatures = features[0].length;
            this.nsamples = observations.length;
            this.features = features;
        }
        this.observations = observations;
        this.sampleWeights = sampleWeights;

        // CX = B
        this.coefficients = new double[this.nfeatures];
        // vectors and matrices for solver
        double[][] x = new double[this.nfeatures][this.nfeatures];
        double[] b = new double[this.nfeatures];

        // build X and B matrices
        for (int i = 0; i < this.nfeatures; i++) {
            b[i] = 0;
            for (int ii = 0; ii < this.nfeatures; ii++) {
                x[i][ii] = 0;
                for (int j = 0; j < this.nsamples; j++) {
                    x[i][ii] += (this.sampleWeights[j] * this.features[j][i] * this.features[j][ii]);

                    // hijack this loop to build B matrix, but we only need to loop over i and j
                    // therefore only do anything on the first step of the ii loop
                    if (ii == 0) {
                        b[i] += (this.sampleWeights[j] * this.features[j][i] * this.observations[j]);
                    }
                }
            }
        }

        try {
            x = this.safeInvert(x);
        } catch (ArithmeticException e) {
            throw new ArithmeticException(
                    "Weighted Linear Regression: Matrix cannot be inverted! " +
                            "This can be caused by a very under-specified model, where " +
                            "the ratio of samples to features is roughly less than 0.10. This model has a ratio of " +
                            (double) this.nsamples / this.nfeatures +
                            ".");
        }

        // recover the coefficients by multiplying the inverse coefficient matrix by B
        this.coefficients = this.matrixMultiplyVector(x, b);

        // mark the model as being fit and return coefficients
        this.fitPerformed = true;
        return this.coefficients;
    }


    // MODEL METRICS ===================================================================================================
    /**
     * Recover the goodness-of-fit of the WLR model. This is the coefficient of determination, as per:
     * https://en.wikipedia.org/wiki/Multiple_correlation
     * @return the coefficient of determination
     */
    public Double getGoodnessOfFit() throws ArithmeticException {
        if (! this.fitPerformed) {
            return Double.NaN;
        } else {
            double yBar = 0;
            double weightSum = 0;
            for (int i = 0; i < this.nsamples; i++) {
                yBar += this.sampleWeights[i] * this.observations[i];
                weightSum += this.sampleWeights[i];
            }
            if (weightSum == 0) {
                throw new ArithmeticException("Weights cannot sum to zero!");
            }
            yBar /= weightSum;
            double totalSquareSum = 0;
            double residualSquareSum = 0;
            for (int i = 0; i < this.nsamples; i++) {
                double f_i = 0;
                for (int j = 0; j < this.nfeatures; j++) {
                    f_i += this.features[i][j] * this.coefficients[j];
                }
                double residual = (this.observations[i] - f_i);
                double variance = (this.observations[i] - yBar);
                totalSquareSum += this.sampleWeights[i] * (variance * variance);
                residualSquareSum += this.sampleWeights[i] * (residual * residual);
            }
            if (totalSquareSum == 0){
                throw new ArithmeticException("Total variance of observations is zero."+
                        " Use more samples to correct this error");
            }
            return  1 - (residualSquareSum/totalSquareSum);
        }
    }

    /**
     * Recover the mean square error of the WLR model.
     * @return the mean squared error of the model
     */
    public Double getMSE(){
        if (! this.fitPerformed) {
            return Double.NaN;
        } else {
            double totalResidual = 0;
            double weightSum = 0;
            for (int i = 0; i < this.nsamples; i++) {
                double f_i = 0;
                for (int j = 0; j < this.nfeatures; j++) {
                    f_i += this.features[i][j] * this.coefficients[j];
                }
                double residual = (this.observations[i] - f_i);
                totalResidual += this.sampleWeights[i] * (residual * residual);
                weightSum += this.sampleWeights[i];
            }
            if (weightSum == 0) {
                throw new ArithmeticException("Weights cannot sum to zero!");
            }
            return totalResidual/weightSum;
        }
    }

    // MATRIX UTILITIES ================================================================================================
    /**
     * Find the product
     * @param m a square double[][]; the matrix to find the diagonal element within
     * @param v a binary int[], marking whether a specific index has been used as a pivot yet or not
     * @return the index of the found pivot
     *
     */

    private double[] matrixMultiplyVector(double[][] m, double [] v) throws IllegalArgumentException {
        int mSize = m[0].length;
        if (mSize != v.length){
            throw new IllegalArgumentException("# columns of matrix must match vector length for "+
                    "matrix-vector multiplication! Matrix columns: "+
                    mSize+
                    ", Vector length: "+
                    v.length);
        }

        double[] product = new double[mSize];
        for (int i = 0; i < this.nfeatures; i++) {
            product[i] = 0;
            for (int j = 0; j < this.nfeatures; j++) {
                product[i] += m[i][j] * v[j];
            }
        }
        return product;
    }

    /**
     * Find the diagonal element at row i with the largest absolute value, where {@code pivotsUsed[i] == 0}
     * This is the pivot value for the Gauss-Jordan algorithm
     * @param x a square double[][]; the matrix to find the diagonal element within
     * @param pivotsUsed a binary int[], marking whether a specific index has been used as a pivot yet or not
     * @return the index of the found pivot
     *
     */
    private int findPivot(double [][] x, int[] pivotsUsed){
        double maxAbs = 0;
        int pivot = 0;
        int size = x.length;
        for (int diagIdx = 0; diagIdx < size; diagIdx++) {
            double abs = Math.abs(x[diagIdx][diagIdx]);
            if (abs > maxAbs && pivotsUsed[diagIdx] == 0) {
                pivot = diagIdx;
                maxAbs = abs;
            }
        }
        return pivot;
    }


    /**
     * Inverts the square, non-singular matrix X
     * @param x a square, non-singular double[][] X
     * @return the inverted matrix
     *
     * Dr. Debabrata DasGupta's description of the algorithm is here:
     * https://www.researchgate.net/publication/271296470_In-Place_Matrix_Inversion_by_Modified_Gauss-Jordan_Algorithm
     */
    private double[][] invertSquareMatrix(double[][] x) {
        int size = x.length;
        double[][] copy = new double[size][size];
        for (int i = 0; i < size; i++) {
            copy[i] = Arrays.copyOf(x[i], size);
        }

        // initialize array to track which pivots have been used
        int[] pivotsUsed = IntStream.range(0, size).map(i -> 0).toArray();

        // perform both operations until each row has been used as pivot
        // once we've done all iterations, X will be inverted in place
        for (int iterations=0; iterations<size; iterations++) {
            // OPERATION 1
            // find the pivot
            // the pivot idx is the idx of the diagonal element with largest absolute value
            // that hasn't already been used as a pivot
            int pivot = findPivot(copy, pivotsUsed);
            double pivotVal = copy[pivot][pivot];

            if (pivotVal == 0){
                throw new ArithmeticException("Matrix is singular and cannot be inverted");
            }

            //virtualize the pivot
            copy[pivot][pivot] = 1.;

            //mark the pivot used
            pivotsUsed[pivot] = 1;

            // normalize the pivot row
            for (int i = 0; i < size; i++) {
                copy[pivot][i] /= pivotVal;
            }

            // OPERATION 2
            // reduce each non-pivot column by X[p][i] * X[i][pivot]
            for (int i = 0; i<size; i++){
                if (i != pivot){
                    double rowValueAtPivot = copy[i][pivot];
                    copy[i][pivot] = 0.;
                    for (int j=0; j<size; j++){
                        copy[i][j] -= copy[pivot][j] * rowValueAtPivot;
                    }
                }
            }
        }
        return copy;
    }

    /**
     * Attempt to invert the given matrix in-place.
     * If the matrix is singular, jitter the values slightly to break singularity
     * @param x a square double[][]; the matrix to be inverted
     * @return boolean, whether or not the inversion was successful
     *
     */
    public double[][] safeInvert(double[][] x) throws ArithmeticException {
        double[][] xInv;
        for (int jitterTries=0; jitterTries < 10; jitterTries++) {
            try {
                xInv = invertSquareMatrix(x);
                return xInv;
            } catch(ArithmeticException e){
                // if the inversion is unsuccessful, we can try slightly jittering the matrix.
                // this will reduce the accuracy of the inversion marginally, but ensures that we get results
                this.jitterMatrix(x, .0000001);
            }
        }

        // if jittering didn't work, throw an error
        throw new ArithmeticException("Matrix is singular and could not be inverted via jittering");
    }


    /**
     * Jitters the values of a matrix IN-PLACE by a random number in range (0, delta)
     * @param X the matrix to be jittered
     * @param delta the scale of the jittering
     *
     */
    private void jitterMatrix(double[][] X, double delta){
        for (int i=0; i<X.length; i++){
            for (int j=0; j<X[0].length; j++){
                X[i][j] += delta * this.rn.nextDouble();
            }
        }
    }
}
