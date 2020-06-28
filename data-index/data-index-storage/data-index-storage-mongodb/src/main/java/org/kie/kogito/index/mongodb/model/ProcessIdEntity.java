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

package org.kie.kogito.index.mongodb.model;

import java.util.Objects;

import io.quarkus.mongodb.panache.MongoEntity;
import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import org.bson.codecs.pojo.annotations.BsonId;

import static org.kie.kogito.index.Constants.PROCESS_ID_MODEL_STORAGE;

@MongoEntity(collection = PROCESS_ID_MODEL_STORAGE)
public class ProcessIdEntity extends PanacheMongoEntityBase {

    @BsonId
    public String processId;

    public String fullTypeName;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProcessIdEntity that = (ProcessIdEntity) o;
        return Objects.equals(processId, that.processId) &&
                Objects.equals(fullTypeName, that.fullTypeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(processId, fullTypeName);
    }
}
