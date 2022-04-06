/*
 * Copyright 2022 Red Hat, Inc. and/or its affiliates.
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

package org.kie.kogito.explainability.local.shap;

import org.kie.kogito.explainability.model.PerturbationContext;

public class CFBackgroundConfig {
    private final int nSeeds;
    private final int nToGenerate;
    private final long optaCalculationLimit;
    private final long optaTimeLimit;
    private final long modelTimeoutSeconds;
    private final double constraintExpansionFactor;
    private final double goalThreshold;
    private final PerturbationContext pc;

    /**
     * Generate CFs from the seed inputs that meet a certain goal output
     *
     * @param nSeeds: The number of closest seeds to use to generate CFs for:
     *        - This selects the n PredictionInputs from the seeds that have the closest output by Pythagorean
     *        distance to the goal, and uses these as starting points to generate counterfactuals
     * @param nToGenerate: The number of CFs to create in total
     * @param constraintExpansionFactor: For each feature, the range between the maximum value and minimum value is found as $diff
     *        Then, the FeatureContraints for each feature as set as:
     *        min = min - diff*constraintExpansionFactor
     *        max = max + diff*constraintExpansionFactor
     * @param goalThreshold: The CF goal threshold to define a satisfactory CF
     * @param modelTimeoutSeconds: timeout for the CF explanation predictAsync
     * @param optaCalculationLimit: Optaplanner termination configuration
     * @param optaTimeLimit: Optaplanner termination configuration
     **/

    public CFBackgroundConfig(int nSeeds, int nToGenerate, double constraintExpansionFactor, double goalThreshold,
            long modelTimeoutSeconds, long optaCalculationLimit, long optaTimeLimit, PerturbationContext pc) {
        this.nSeeds = nSeeds;
        this.nToGenerate = nToGenerate;
        this.constraintExpansionFactor = constraintExpansionFactor;
        this.goalThreshold = goalThreshold;
        this.modelTimeoutSeconds = modelTimeoutSeconds;
        this.optaTimeLimit = optaTimeLimit;
        this.optaCalculationLimit = optaCalculationLimit;
        this.pc = pc;
    }

    public int getnSeeds() {
        return nSeeds;
    }

    public int getnToGenerate() {
        return nToGenerate;
    }

    public double getConstraintExpansionFactor() {
        return constraintExpansionFactor;
    }

    public double getGoalThreshold() {
        return goalThreshold;
    }

    public long getModelTimeoutSeconds() {
        return modelTimeoutSeconds;
    }

    public long getOptaCalculationLimit() {
        return optaCalculationLimit;
    }

    public long getOptaTimeLimit() {
        return optaTimeLimit;
    }

    public PerturbationContext getPerturbationContext() {
        return pc;
    }

}
