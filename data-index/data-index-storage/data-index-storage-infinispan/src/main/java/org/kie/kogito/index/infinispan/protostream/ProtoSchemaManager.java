/*
 * Copyright 2019 Red Hat, Inc. and/or its affiliates.
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

package org.kie.kogito.index.infinispan.protostream;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.inject.Inject;

import org.infinispan.query.remote.client.ProtobufMetadataManagerConstants;
import org.kie.kogito.index.event.SchemaRegisteredEvent;
import org.kie.kogito.index.infinispan.cache.InfinispanCacheManager;
import org.kie.kogito.index.schema.SchemaDescriptor;
import org.kie.kogito.index.schema.SchemaRegistrationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Collections.emptyList;

@ApplicationScoped
public class ProtoSchemaManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProtoSchemaManager.class);

    @Inject
    ProtoSchemaAcceptor schemaAcceptor;

    @Inject
    @Any
    InfinispanCacheManager cacheManager;

    public void onSchemaRegisteredEvent(@Observes SchemaRegisteredEvent event) {
        if (schemaAcceptor.accept(event.getSchemaType())) {
            SchemaDescriptor schemaDescriptor = event.getSchemaDescriptor();
            cacheManager.getProtobufCache().put(schemaDescriptor.getName(), schemaDescriptor.getSchemaContent());
            schemaDescriptor.getProcessDescriptor().ifPresent(processDescriptor -> {
                Map<String, String> cache = cacheManager.getProtobufCache();
                cacheManager.getProcessIdModelCache().put(processDescriptor.getProcessId(), processDescriptor.getProcessType());

                List<String> errors = checkSchemaErrors(cache);

                if (!errors.isEmpty()) {
                    String message = "Proto Schema contain errors:\n" + String.join("\n", errors);
                    throw new SchemaRegistrationException(message);
                }

                if (LOGGER.isDebugEnabled()) {
                    logProtoCacheKeys();
                }
            });
        }
    }

    private List<String> checkSchemaErrors(Map<String, String> metadataCache) {
        if (metadataCache.containsKey(ProtobufMetadataManagerConstants.ERRORS_KEY_SUFFIX)) {
            List<String> errors = new ArrayList<>();
            // The existence of this key indicates there are errors in some files
            String files = metadataCache.get(ProtobufMetadataManagerConstants.ERRORS_KEY_SUFFIX);
            for (String fname : files.split("\n")) {
                String errorKey = fname + ProtobufMetadataManagerConstants.ERRORS_KEY_SUFFIX;
                final String error = metadataCache.get(errorKey);
                LOGGER.warn("Found errors in Protobuf schema file: {}\n{}\n", fname, error);
                errors.add(String.format("Protobuf schema file: %s\n%s\n", fname, error));
            }
            return errors;
        } else {
            return emptyList();
        }
    }

    private void logProtoCacheKeys() {
        LOGGER.debug(">>>>>>list cache keys start");
        cacheManager.getProtobufCache().entrySet().forEach(e -> LOGGER.debug(e.toString()));
        LOGGER.debug(">>>>>>list cache keys end");
    }
}