package org.kie.kogito.index.graphql.query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

import org.kie.kogito.persistence.api.query.AttributeFilter;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class GraphQLQueryParser implements Function<Object, List<AttributeFilter<?>>> {

    private final Map<String, Function<Object, Stream<AttributeFilter<?>>>> mapper = new HashMap<>();

    public void mapAttribute(String name, Function<Object, Stream<AttributeFilter<?>>> mapperFunction) {
        mapper.put(name, mapperFunction);
    }

    @Override
    public List<AttributeFilter<?>> apply(Object where) {
        return where == null ? emptyList()
                : ((Map<String, Object>) where).entrySet().stream()
                        .filter(entry -> mapper.containsKey(entry.getKey()) && entry.getValue() != null)
                        .flatMap(entry -> mapper.get(entry.getKey()).apply(entry.getValue()))
                        .filter(Objects::nonNull)
                        .collect(toList());
    }

    @Override
    public String toString() {
        return "GraphQLQueryParser{" +
                "mapper=" + mapper +
                '}';
    }
}
