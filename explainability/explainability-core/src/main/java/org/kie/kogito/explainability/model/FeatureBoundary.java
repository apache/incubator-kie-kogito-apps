/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
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
package org.kie.kogito.explainability.model;

/**
 * Information about search space boundaries for features with a theoretically infinite domain.
 */

public class FeatureBoundary {
    private final double start;
    private final double end;

    public FeatureBoundary(double start, double end) {
        this.start = start;
        this.end = end;
    }

    /**
     * Get start value for this boundary
     *
     * @return the start value
     */
    public double getStart() {
        return start;
    }

    /**
     * Get the end value for this boundary
     *
     * @return the end value
     */
    public double getEnd() {
        return end;
    }
}
