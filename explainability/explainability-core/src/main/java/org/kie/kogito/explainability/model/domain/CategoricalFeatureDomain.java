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

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CategoricalFeatureDomain extends AbstractCategoricalFeatureDomain<String> {

    private CategoricalFeatureDomain(Set<String> categories) {
        super(categories);
    }

    /**
     * Create a {@link FeatureDomain} for a categorical feature
     *
     * @param categories A set with all the allowed category values
     * @return A {@link FeatureDomain}
     */
    public static FeatureDomain<String> create(Set<String> categories) {
        return new CategoricalFeatureDomain(categories);
    }

    public static FeatureDomain<String> create(List<String> categories) {
        return new CategoricalFeatureDomain(new HashSet<>(categories));
    }

    public static FeatureDomain<String> create(String... categories) {
        return new CategoricalFeatureDomain(new HashSet<>(Arrays.asList(categories)));
    }
}
