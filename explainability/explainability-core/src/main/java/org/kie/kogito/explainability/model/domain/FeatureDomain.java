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
package org.kie.kogito.explainability.model.domain;

import java.util.Set;

/**
 * Information about the search space domain for the model's features.
 */

public interface FeatureDomain {

    /**
     * Return whether this is an empty domain
     *
     * @return True if empty
     */
    boolean isEmpty();

    /**
     * Get start value for this boundary
     *
     * @return the start value
     */
    Double getLowerBound();

    /**
     * Get the end value for this boundary
     *
     * @return the end value
     */
    Double getUpperBound();

    /**
     * Get the possible values for this boundary
     *
     * @return the end value
     */
    Set<String> getCategories();

}