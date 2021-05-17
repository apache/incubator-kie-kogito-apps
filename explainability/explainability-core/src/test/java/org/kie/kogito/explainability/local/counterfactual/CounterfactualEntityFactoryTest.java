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
package org.kie.kogito.explainability.local.counterfactual;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.kie.kogito.explainability.local.counterfactual.entities.BooleanEntity;
import org.kie.kogito.explainability.local.counterfactual.entities.CategoricalEntity;
import org.kie.kogito.explainability.local.counterfactual.entities.CounterfactualEntity;
import org.kie.kogito.explainability.local.counterfactual.entities.CounterfactualEntityFactory;
import org.kie.kogito.explainability.local.counterfactual.entities.DoubleEntity;
import org.kie.kogito.explainability.local.counterfactual.entities.IntegerEntity;
import org.kie.kogito.explainability.local.counterfactual.entities.fixed.FixedBooleanEntity;
import org.kie.kogito.explainability.local.counterfactual.entities.fixed.FixedCategoricalEntity;
import org.kie.kogito.explainability.local.counterfactual.entities.fixed.FixedDoubleEntity;
import org.kie.kogito.explainability.local.counterfactual.entities.fixed.FixedIntegerEntity;
import org.kie.kogito.explainability.model.Feature;
import org.kie.kogito.explainability.model.FeatureFactory;
import org.kie.kogito.explainability.model.PredictionFeatureDomain;
import org.kie.kogito.explainability.model.PredictionInput;
import org.kie.kogito.explainability.model.domain.CategoricalFeatureDomain;
import org.kie.kogito.explainability.model.domain.EmptyFeatureDomain;
import org.kie.kogito.explainability.model.domain.FeatureDomain;
import org.kie.kogito.explainability.model.domain.NumericalFeatureDomain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CounterfactualEntityFactoryTest {

    @Test
    void testIntegerFactory() {
        int value = 5;
        final Feature feature = FeatureFactory.newNumericalFeature("int-feature", value);
        final FeatureDomain domain = NumericalFeatureDomain.create(0.0, 10.0);
        final CounterfactualEntity counterfactualEntity = CounterfactualEntityFactory.from(feature, false, domain);
        assertTrue(counterfactualEntity instanceof IntegerEntity);
    }

    @Test
    void testFixedIntegerFactory() {
        int value = 5;
        final Feature feature = FeatureFactory.newNumericalFeature("int-feature", value);
        final FeatureDomain domain = EmptyFeatureDomain.create();
        final CounterfactualEntity counterfactualEntity = CounterfactualEntityFactory.from(feature, true, domain);
        assertTrue(counterfactualEntity instanceof FixedIntegerEntity);
    }

    @Test
    void testDoubleFactory() {
        double value = 5.0;
        final Feature feature = FeatureFactory.newNumericalFeature("double-feature", value);
        final FeatureDomain domain = NumericalFeatureDomain.create(0.0, 10.0);
        final CounterfactualEntity counterfactualEntity = CounterfactualEntityFactory.from(feature, false, domain);
        assertTrue(counterfactualEntity instanceof DoubleEntity);
    }

    @Test
    void testFixedDoubleFactory() {
        double value = 5.0;
        final Feature feature = FeatureFactory.newNumericalFeature("double-feature", value);
        final FeatureDomain domain = EmptyFeatureDomain.create();
        final CounterfactualEntity counterfactualEntity = CounterfactualEntityFactory.from(feature, true, domain);
        assertTrue(counterfactualEntity instanceof FixedDoubleEntity);
    }

    @Test
    void testBooleanFactory() {
        final Feature feature = FeatureFactory.newBooleanFeature("bool-feature", false);
        final CounterfactualEntity counterfactualEntity =
                CounterfactualEntityFactory.from(feature, false, EmptyFeatureDomain.create());
        assertTrue(counterfactualEntity instanceof BooleanEntity);
    }

    @Test
    void testFixedBooleanFactory() {
        final Feature feature = FeatureFactory.newBooleanFeature("bool-feature", false);
        final CounterfactualEntity counterfactualEntity =
                CounterfactualEntityFactory.from(feature, true, EmptyFeatureDomain.create());
        assertTrue(counterfactualEntity instanceof FixedBooleanEntity);
    }

    @Test
    void testCategoricalFactoryObject() {
        final Feature feature = FeatureFactory.newCategoricalFeature("categorical-feature", "foo");
        final FeatureDomain domain = CategoricalFeatureDomain.create("foo", "bar");
        final CounterfactualEntity counterfactualEntity = CounterfactualEntityFactory.from(feature, false, domain);
        assertTrue(counterfactualEntity instanceof CategoricalEntity);
        assertEquals(domain.getCategories(), ((CategoricalEntity) counterfactualEntity).getValueRange());
    }

    @Test
    void testFixedCategoricalEntity() {
        final Feature feature = FeatureFactory.newCategoricalFeature("categorical-feature", "foo");
        final FeatureDomain domain = EmptyFeatureDomain.create();
        final CounterfactualEntity counterfactualEntity = CounterfactualEntityFactory.from(feature, true, domain);
        assertTrue(counterfactualEntity instanceof FixedCategoricalEntity);
    }

    @Test
    void testCategoricalFactorySet() {
        final Feature feature = FeatureFactory.newCategoricalFeature("categorical-feature", "foo");
        final FeatureDomain domain = CategoricalFeatureDomain.create(Set.of("foo", "bar"));
        final CounterfactualEntity counterfactualEntity = CounterfactualEntityFactory.from(feature, false, domain);
        assertTrue(counterfactualEntity instanceof CategoricalEntity);
        assertEquals(domain.getCategories(), ((CategoricalEntity) counterfactualEntity).getValueRange());
    }

    @Test
    void testCategoricalFactoryList() {
        final Feature feature = FeatureFactory.newCategoricalFeature("categorical-feature", "foo");
        final FeatureDomain domain = CategoricalFeatureDomain.create(List.of("foo", "bar"));
        final CounterfactualEntity counterfactualEntity = CounterfactualEntityFactory.from(feature, false, domain);
        assertTrue(counterfactualEntity instanceof CategoricalEntity);
        assertEquals(domain.getCategories(), ((CategoricalEntity) counterfactualEntity).getValueRange());
    }

    @Test
    void testCreateFixedEntities() {
        List<Feature> features = new LinkedList<>();
        List<FeatureDomain> featureDomains = new LinkedList<>();
        List<Boolean> constraints = new LinkedList<>();
        features.add(FeatureFactory.newNumericalFeature("f-num1", 100.0));
        constraints.add(true);
        featureDomains.add(EmptyFeatureDomain.create());

        features.add(FeatureFactory.newNumericalFeature("f-num2", 100.0));
        constraints.add(false);
        featureDomains.add(NumericalFeatureDomain.create(0.0, 1000.0));

        features.add(FeatureFactory.newNumericalFeature("f-num3", 100.0));
        constraints.add(true);
        featureDomains.add(EmptyFeatureDomain.create());

        features.add(FeatureFactory.newNumericalFeature("f-num4", 100.0));
        constraints.add(false);
        featureDomains.add(NumericalFeatureDomain.create(0.0, 1000.0));

        PredictionFeatureDomain featureDomain = new PredictionFeatureDomain(featureDomains);

        PredictionInput input = new PredictionInput(features);

        List<CounterfactualEntity> entities =
                CounterfactualEntityFactory.createEntities(input, featureDomain, constraints, null);

        assertTrue(entities.get(0) instanceof FixedDoubleEntity);
        assertTrue(entities.get(1) instanceof DoubleEntity);
        assertTrue(entities.get(2) instanceof FixedDoubleEntity);
        assertTrue(entities.get(3) instanceof DoubleEntity);
    }
}
