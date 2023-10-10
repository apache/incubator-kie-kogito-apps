package org.kie.kogito.trusty.storage.infinispan.testfield;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class MapToListTestField<M, K, V, L> extends ListTestField<M, L> {

    public MapToListTestField(
            String fieldName,
            Map<K, V> fieldValue,
            Function<M, Map<K, V>> getter,
            BiConsumer<M, Map<K, V>> setter,
            Class<L> elementClass,
            Function<Map<K, V>, List<L>> mapToList,
            Function<List<L>, Map<K, V>> listToMap) {
        super(fieldName, mapToList.apply(fieldValue), obj -> mapToList.apply(getter.apply(obj)), (obj, value) -> setter.accept(obj, listToMap.apply(value)), elementClass);
    }
}
