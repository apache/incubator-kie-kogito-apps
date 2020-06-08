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

package org.kie.kogito.index.protobuf;

import java.io.IOException;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.infinispan.protostream.FileDescriptorSource;

@ApplicationScoped
public class ProtostreamProducer {

    static final String KOGITO_INDEX_PROTO = "kogito-index.proto";
    static final String KOGITO_TYPES_PROTO = "kogito-types.proto";

    @Produces
    FileDescriptorSource kogitoTypesDescriptor() throws IOException {
        FileDescriptorSource source = new FileDescriptorSource();
        source.addProtoFile(KOGITO_INDEX_PROTO, Thread.currentThread().getContextClassLoader().getResourceAsStream("META-INF/kogito-index.proto"));
        source.addProtoFile(KOGITO_TYPES_PROTO, Thread.currentThread().getContextClassLoader().getResourceAsStream("kogito-types.proto"));
        return source;
    }
}
