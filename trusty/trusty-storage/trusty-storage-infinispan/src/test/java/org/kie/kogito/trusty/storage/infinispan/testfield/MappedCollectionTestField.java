package org.kie.kogito.trusty.storage.infinispan.testfield;

import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MappedCollectionTestField<M, T, C> extends CollectionTestField<M, T> {

    public MappedCollectionTestField(String fieldName,
            Collection<C> fieldValue,
            Function<M, Collection<C>> getter,
            BiConsumer<M, Collection<C>> setter,
            Function<T, C> mapElementToPersistentClass,
            Function<C, T> mapElementFromPersistentClass,
            Class<T> elementClass) {
        super(fieldName,
                fieldValue.stream().map(mapElementFromPersistentClass).collect(Collectors.toList()),
                m -> getter.apply(m).stream().map(mapElementFromPersistentClass).collect(Collectors.toList()),
                (m, ts) -> setter.accept(m, ts.stream().map(mapElementToPersistentClass).collect(Collectors.toList())),
                elementClass);
    }

}
