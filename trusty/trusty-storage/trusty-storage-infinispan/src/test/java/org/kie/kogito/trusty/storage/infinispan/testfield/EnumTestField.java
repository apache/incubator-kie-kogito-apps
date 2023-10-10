package org.kie.kogito.trusty.storage.infinispan.testfield;

import java.util.function.BiConsumer;
import java.util.function.Function;

import static org.kie.kogito.persistence.infinispan.protostream.AbstractMarshaller.enumFromString;
import static org.kie.kogito.persistence.infinispan.protostream.AbstractMarshaller.stringFromEnum;

public class EnumTestField<M, E extends Enum<E>> extends StringTestField<M> {

    public EnumTestField(String fieldName, E fieldValue, Function<M, E> getter, BiConsumer<M, E> setter, Class<E> enumClass) {
        super(fieldName, stringFromEnum(fieldValue), obj -> stringFromEnum(getter.apply(obj)), (obj, value) -> setter.accept(obj, enumFromString(value, enumClass)));
    }
}
