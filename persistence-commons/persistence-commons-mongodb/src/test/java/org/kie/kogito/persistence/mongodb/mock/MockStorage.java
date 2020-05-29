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

package org.kie.kogito.persistence.mongodb.mock;

import javax.enterprise.context.ApplicationScoped;

import com.mongodb.client.MongoCollection;
import io.quarkus.arc.AlternativePriority;
import io.quarkus.mongodb.panache.runtime.MongoOperations;
import org.bson.Document;
import org.kie.kogito.persistence.api.query.Query;
import org.kie.kogito.persistence.mongodb.storage.AbstractStorage;

@ApplicationScoped
@AlternativePriority(0)
public class MockStorage extends AbstractStorage<String, String, Document> {

    @Override
    protected MongoCollection<Document> getCollection() {
        return MongoOperations.mongoDatabase(Document.class).getCollection("mock", Document.class);
    }

    @Override
    protected Document mapToEntity(String key, String value) {
        return new Document(MongoOperations.ID, key).append("value", value);
    }

    @Override
    protected String mapToModel(String key, Document entity) {
        return entity.get("value").toString();
    }

    @Override
    public Query<String> query() {
        return null;
    }

    @Override
    public String getRootType() {
        return "mock";
    }
}
