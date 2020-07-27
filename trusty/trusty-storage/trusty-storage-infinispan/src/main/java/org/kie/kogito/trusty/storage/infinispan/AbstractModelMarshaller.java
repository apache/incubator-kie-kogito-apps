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

package org.kie.kogito.trusty.storage.infinispan;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.infinispan.protostream.MessageMarshaller;
import org.kie.kogito.persistence.infinispan.protostream.AbstractMarshaller;

abstract class AbstractModelMarshaller<T> extends AbstractMarshaller implements MessageMarshaller<T> {

    private final Class<? extends T> javaClass;

    public AbstractModelMarshaller(ObjectMapper mapper, Class<? extends T> javaClass) {
        super(mapper);
        this.javaClass = javaClass;
    }

    @Override
    public Class<? extends T> getJavaClass() {
        return javaClass;
    }

    @Override
    public String getTypeName() {
        return getJavaClass().getTypeName();
    }

}
