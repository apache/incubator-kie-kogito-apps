/*
 *  Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

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
