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

package org.kie.kogito.index.mongodb.query;

import javax.enterprise.context.Dependent;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.kie.kogito.index.mongodb.storage.DomainStorage;
import org.kie.kogito.index.mongodb.model.DomainEntity;
import org.kie.kogito.persistence.mongodb.query.AbstractQuery;

import static io.quarkus.mongodb.panache.runtime.MongoOperations.ID;

@Dependent
public class DomainQuery extends AbstractQuery<ObjectNode, Document> {

    DomainStorage domainStorage;

    public void setDomainStorage(DomainStorage domainStorage) {
        this.domainStorage = domainStorage;
    }

    @Override
    protected MongoCollection<Document> getCollection() {
        return domainStorage.getCollection();
    }

    @Override
    protected ObjectNode mapToModel(Document document) {
        return DomainEntity.toObjectNode(document.getString(ID), document);
    }
}
