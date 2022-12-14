/*
 * Copyright 2022 Red Hat, Inc. and/or its affiliates.
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
package org.kie.kogito.jobs.service.repository.marshaller;

import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;

import io.vertx.core.json.JsonObject;

@ApplicationScoped
public class PayloadMarshaller implements Marshaller<Object, JsonObject> {

    @Override
    public JsonObject marshall(Object value) {
        return Optional.ofNullable((String) value).map(v -> new JsonObject(v)).orElse(null);
    }

    @Override
    public Object unmarshall(JsonObject value) {
        return Optional.ofNullable(value).map(JsonObject::encode).orElse(null);
    }
}
