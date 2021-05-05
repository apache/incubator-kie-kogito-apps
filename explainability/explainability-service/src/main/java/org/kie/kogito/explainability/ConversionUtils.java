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
package org.kie.kogito.explainability;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.kie.kogito.explainability.api.CounterfactualDomainCategoricalDto;
import org.kie.kogito.explainability.api.CounterfactualDomainDto;
import org.kie.kogito.explainability.api.CounterfactualDomainRangeDto;
import org.kie.kogito.explainability.api.CounterfactualSearchDomainDto;
import org.kie.kogito.explainability.model.Feature;
import org.kie.kogito.explainability.model.FeatureFactory;
import org.kie.kogito.explainability.model.Output;
import org.kie.kogito.explainability.model.Type;
import org.kie.kogito.explainability.model.Value;
import org.kie.kogito.explainability.model.domain.CategoricalFeatureDomain;
import org.kie.kogito.explainability.model.domain.FeatureDomain;
import org.kie.kogito.explainability.model.domain.NumericalFeatureDomain;
import org.kie.kogito.tracing.typedvalue.CollectionValue;
import org.kie.kogito.tracing.typedvalue.TypedValue;

import com.fasterxml.jackson.databind.JsonNode;

import io.vertx.core.json.JsonObject;

public class ConversionUtils {

    private ConversionUtils() {
        // prevent initialization
    }

    protected static Feature toFeature(String name, Object value) {
        if (value instanceof JsonObject) {
            return FeatureFactory.newCompositeFeature(name, toFeatureList((JsonObject) value));
        }
        return toTypeValuePair(value)
                .map(p -> new Feature(name, p.getLeft(), p.getRight()))
                .orElse(null);
    }

    public static Feature toFeature(String name, TypedValue value) {
        if (value.isUnit()) {
            return toTypeValuePair(value.toUnit().getValue())
                    .map(p -> new Feature(name, p.getLeft(), p.getRight()))
                    .orElse(null);
        } else if (value.isStructure()) {
            return FeatureFactory.newCompositeFeature(name, toFeatureList(value.toStructure().getValue()));
        } else if (value.isCollection()) {
            return FeatureFactory.newCompositeFeature(name, toFeatureList(name, value.toCollection()));
        } else {
            throw new IllegalArgumentException(String.format("unexpected value kind %s", value.getKind()));
        }
    }

    public static FeatureDomain toFeatureDomain(String name, CounterfactualSearchDomainDto domain) {
        if (domain.isUnit()) {
            return toCounterfactualSearchDomain(domain.toUnit().getDomain()).orElse(null);
        } else {
            throw new IllegalArgumentException(String.format("Unsupported CounterfactualSearchDomain kind %s", domain.getKind()));
        }
    }

    public static Boolean toFeatureConstraint(String name, CounterfactualSearchDomainDto domain) {
        if (domain.isUnit()) {
            return domain.toUnit().isFixed();
        } else {
            throw new IllegalArgumentException(String.format("Unsupported CounterfactualSearchDomain kind %s", domain.getKind()));
        }
    }

    protected static List<Feature> toFeatureList(String name, CollectionValue collectionValue) {
        Collection<TypedValue> values = collectionValue.getValue();
        List<Feature> list = new ArrayList<>(values.size());
        int index = 0;
        for (TypedValue typedValue : values) {
            list.add(toFeature(name + "_" + index, typedValue));
            index++;
        }
        return list;
    }

    public static List<Feature> toFeatureList(JsonObject mainObj) {
        return toList(mainObj, ConversionUtils::toFeature);
    }

    public static List<Feature> toFeatureList(Map<String, TypedValue> values) {
        return toList(values, ConversionUtils::toFeature);
    }

    public static List<FeatureDomain> toFeatureDomainList(Map<String, CounterfactualSearchDomainDto> searchDomains) {
        return toList(searchDomains, ConversionUtils::toFeatureDomain);
    }

    public static List<Boolean> toFeatureConstraintList(Map<String, CounterfactualSearchDomainDto> searchDomains) {
        return toList(searchDomains, ConversionUtils::toFeatureConstraint);
    }

    public static <T> List<T> toList(JsonObject mainObj, BiFunction<String, Object, T> unitConverter) {
        if (mainObj == null) {
            return Collections.emptyList();
        }
        return mainObj.stream()
                .map(entry -> unitConverter.apply(entry.getKey(), entry.getValue()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public static <T, V> List<T> toList(Map<String, V> values, BiFunction<String, V, T> unitConverter) {
        if (values == null) {
            return Collections.emptyList();
        }
        return values.entrySet().stream()
                .map(entry -> unitConverter.apply(entry.getKey(), entry.getValue()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    protected static Output toOutput(String name, Object value) {
        if (value instanceof JsonObject) {
            return new Output(name, Type.COMPOSITE, new Value(toOutputList((JsonObject) value)), 1d);
        }
        return toTypeValuePair(value)
                .map(p -> new Output(name, p.getLeft(), p.getRight(), 1d))
                .orElse(null);
    }

    public static Output toOutput(String name, TypedValue value) {
        if (value.isUnit()) {
            return toTypeValuePair(value.toUnit().getValue())
                    .map(p -> new Output(name, p.getLeft(), p.getRight(), 1d))
                    .orElse(null);
        } else if (value.isStructure()) {
            return new Output(name, Type.COMPOSITE, new Value(toOutputList(value.toStructure().getValue())), 1d);
        } else if (value.isCollection()) {
            return new Output(name, Type.COMPOSITE, new Value(toOutputList(name, value.toCollection())), 1d);
        }
        return null;
    }

    protected static List<Output> toOutputList(String name, CollectionValue collectionValue) {
        Collection<TypedValue> values = collectionValue.getValue();
        List<Output> list = new ArrayList<>(values.size());
        int index = 0;
        for (TypedValue typedValue : values) {
            list.add(toOutput(name + "_" + index, typedValue));
            index++;
        }
        return list;
    }

    public static List<Output> toOutputList(JsonObject mainObj) {
        return toList(mainObj, ConversionUtils::toOutput);
    }

    public static List<Output> toOutputList(Map<String, TypedValue> values) {
        return toList(values, ConversionUtils::toOutput);
    }

    protected static Optional<Pair<Type, Value>> toTypeValuePair(Object value) {
        if (value instanceof Boolean) {
            return Optional.of(Pair.of(Type.BOOLEAN, new Value(value)));
        }
        if (value instanceof Number) {
            return Optional.of(Pair.of(Type.NUMBER, new Value(((Number) value).doubleValue())));
        }
        if (value instanceof String) {
            return Optional.of(Pair.of(Type.TEXT, new Value(value)));
        }
        return Optional.empty();
    }

    public static Optional<Pair<Type, Value>> toTypeValuePair(JsonNode jsonValue) {
        if (jsonValue.isBoolean()) {
            return Optional.of(Pair.of(Type.BOOLEAN, new Value(jsonValue.asBoolean())));
        }
        if (jsonValue.isNumber()) {
            return Optional.of(Pair.of(Type.NUMBER, new Value(jsonValue.asDouble())));
        }
        if (jsonValue.isTextual()) {
            return Optional.of(Pair.of(Type.TEXT, new Value(jsonValue.asText())));
        }
        return Optional.empty();
    }

    public static Optional<FeatureDomain> toCounterfactualSearchDomain(CounterfactualDomainDto domain) {
        if (domain instanceof CounterfactualDomainRangeDto) {
            CounterfactualDomainRangeDto range = (CounterfactualDomainRangeDto) domain;
            JsonNode lb = range.getLowerBound();
            JsonNode ub = range.getUpperBound();
            if (lb.isNumber() && ub.isNumber()) {
                return Optional.of(NumericalFeatureDomain.create(range.getLowerBound().asDouble(), range.getUpperBound().asDouble()));
            } else {
                throw new IllegalArgumentException(String.format("Unsupported CounterfactualDomainRangeDto [%s, %s]", lb.asText(), ub.asText()));
            }
        } else if (domain instanceof CounterfactualDomainCategoricalDto) {
            CounterfactualDomainCategoricalDto categorical = (CounterfactualDomainCategoricalDto) domain;
            Collection<JsonNode> jsonCategories = categorical.getCategories();
            String[] categories = new String[jsonCategories.size()];
            if (jsonCategories.stream().allMatch(JsonNode::isTextual)) {
                jsonCategories.stream().map(JsonNode::asText).collect(Collectors.toList()).toArray(categories);
                return Optional.of(CategoricalFeatureDomain.create(categories));
            } else {
                throw new IllegalArgumentException(String.format("Unsupported CounterfactualDomainCategoricalDto [%s]", String.join(", ", categories)));
            }
        }
        return Optional.empty();
    }

}
