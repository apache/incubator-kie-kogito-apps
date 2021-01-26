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
    private double [] weights;

    // vectors and matrices for solver
    private double[][] X;
    private double[] B;
    private double[] C;
    private int nfeatures; //this is i
    private int nsamples; //this is j


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
     * Fit the WLR model to the data.
     *
     * @param x An {@code nsamples x nfeatures} array of doubles; each row contains one data point of size [nfeatures]
     * @param y An {@code nsamples} array, where y[n] is the observation for data point n.
     * @param sample_weights An {@code nsamples} array, where sample_weights[n] is the weighting of data point n.
     *
     * @return C, an {@code nfeatures} array of coefficients as computed by the regression.
     * In the case where {@code intercept} is true, the last value of {@code C} is the intercept.
     *
     */
    public double[] fit(double[][] x, double[] y, double[] sample_weights) throws ArithmeticException {
        // if we want to compute an intercept, add a dummy feature at last column.
        if (this.intercept) {
            this.nfeatures = x[0].length + 1;
            this.nsamples = y.length;
            this.features = new double[this.nsamples][this.nfeatures];
            for (int i = 0; i < this.nsamples; i++) {
                System.arraycopy(x[i], 0, this.features[i], 0, this.nfeatures - 1);
                this.features[i][this.nfeatures-1] = 1;
            }
        } else {
            this.nfeatures = x[0].length;
            this.nsamples = y.length;
            this.features = x;
        }
        this.observations = y;
        this.weights = sample_weights;


        // CX = B
        this.C = new double[this.nfeatures];
        this.X = new double[this.nfeatures][this.nfeatures];
        this.B = new double[this.nfeatures];

        // build X and B matrices
        for (int i = 0; i < this.nfeatures; i++) {
            this.B[i] = 0;
            for (int ii = 0; ii < this.nfeatures; ii++) {
                this.X[i][ii] = 0;
                for (int j = 0; j < this.nsamples; j++) {
                    this.X[i][ii] += (this.weights[j] * this.features[j][i] * this.features[j][ii]);

                    // hijack this loop to build B matrix, but we only need to loop over i and j
                    // therefore only do anything on the first step of the ii loop
                    if (ii == 0) {
                        this.B[i] += (this.weights[j] * this.features[j][i] * this.observations[j]);
                    }
                }
            }
        }

        // invert the coefficient matrix X in-place
        boolean inversionSuccessful = false;
        double[][] xInv;
        int jitterTries = 0;
        while (! inversionSuccessful && jitterTries < 10) {
            try {
                 xInv = invertSquareMatrix(X);
                inversionSuccessful = true;
                this.X = xInv;
            } catch(ArithmeticException e){
                // if the inversion is unsuccessful, we can try slightly jittering the matrix.
                // this will reduce the accuracy of the regression marginally, but ensures that we get results
                jitterMatrix(X, .0000001);
                jitterTries += 1;
            }
        }

        // if jittering didn't work, throw an error
        if (! inversionSuccessful){
            throw new ArithmeticException(
                    "Weighted Linear Regression: Matrix cannot be inverted! " +
                            "This can be caused by a very under-specified model, where " +
                            "the ratio of samples to features is roughly less than 0.10. This model has a ratio of " +
                            (double) this.nsamples/this.nfeatures +
                            ".");
        }

        // recover the coefficients by multiplying the inverse coefficient matrix by B
        for (int i = 0; i < this.nfeatures; i++) {
            this.C[i] = 0;
            for (int j = 0; j < this.nfeatures; j++) {
                this.C[i] += this.X[i][j] * this.B[j];
            }
        }

        // mark the model as being fit and return coefficients
        this.fitPerformed = true;
        return this.C;
    }


    // MODEL METRICS ===================================================================================================
    /**
     * Recover the goodness-of-fit of the WLR model. This is the coefficient of determination, as per:
     * https://en.wikipedia.org/wiki/Multiple_correlation
     * @return the coefficient of determination
     */
    public Double getGoodnessOfFit(){
        if (! this.fitPerformed) {
            return Double.NaN;
        } else {
            double yBar = 0;
            double weightSum = 0;
            for (int i = 0; i < this.nsamples; i++) {
                yBar += this.weights[i] * this.observations[i];
                weightSum += this.weights[i];
            }
            yBar /= weightSum;
            double totalSquareSum = 0;
            double residualSquareSum = 0;
            for (int i = 0; i < this.nsamples; i++) {
                double f_i = 0;
                for (int j = 0; j < this.nfeatures; j++) {
                    f_i += this.features[i][j] * this.C[j];
                }
                double residual = (this.observations[i] - f_i);
                double variance = (this.observations[i] - yBar);
                totalSquareSum += this.weights[i] * (variance * variance);
                residualSquareSum += this.weights[i] * (residual * residual);
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
                    f_i += this.features[i][j] * this.C[j];
                }
                double residual = (this.observations[i] - f_i);
                totalResidual += this.weights[i] * (residual * residual);
                weightSum += this.weights[i];
            }
            return totalResidual/weightSum;
        }
    }

    // MATRIX UTILITIES ================================================================================================
    /**
     * Inverts the square, non-singular matrix X
     * @param X a square, non-singular double[][] X
     * @return the inverted matrix
     *
     * Dr. Debabrata DasGupta's description of the algorithm is here:
     * https://www.researchgate.net/publication/271296470_In-Place_Matrix_Inversion_by_Modified_Gauss-Jordan_Algorithm
     */
    public static double[][] invertSquareMatrix(double[][] X) {
        int size = X.length;
        double[][] copy = new double[size][size];
        for (int i = 0; i < size; i++) {
            copy[i] = Arrays.copyOf(X[i], size);
        }

        double[] pivotsUsed = new double[size];
        for (int i = 0; i < size; i++) {
            pivotsUsed[i] = 0;
        }
        // perform both operations until each row has been used as pivot
        // once we've done all iterations, X will be inverted in place
        for (int iterations=0; iterations<size; iterations++) {
            // OPERATION 1
            // find the pivot
            // the pivot idx is the idx of the diagonal element with largest absolute value
            // that hasn't already been used as a pivot
            double maxAbs = 0;
            int pivot = 0;
            double pivotVal = 0;
            for (int diagIdx = 0; diagIdx < size; diagIdx++) {
                double abs = Math.abs(copy[diagIdx][diagIdx]);
                if (abs > maxAbs && pivotsUsed[diagIdx] == 0) {
                    pivot = diagIdx;
                    maxAbs = abs;
                    pivotVal = copy[diagIdx][diagIdx];
                }
            }

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
     * Jitters the values of a matrix IN-PLACE by a random number in range (0, delta)
     * @param X the matrix to be jittered
     * @param delta the scale of the jittering
     *
     */
    private static void jitterMatrix(double[][] X, double delta){
        Random rn = new Random();
        for (int i=0; i<X.length; i++){
            for (int j=0; j<X[0].length; j++){
                X[i][j] += delta * rn.nextDouble();
            }
        }
    }
}
