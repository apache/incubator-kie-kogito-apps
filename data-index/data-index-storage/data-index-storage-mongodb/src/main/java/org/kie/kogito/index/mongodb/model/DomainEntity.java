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

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.quarkus.mongodb.panache.runtime.MongoOperations;
import org.bson.BsonString;
import org.bson.Document;
import org.kie.kogito.index.mongodb.storage.DomainStorage;
import org.kie.kogito.persistence.mongodb.utils.ModelUtils;

import static org.kie.kogito.persistence.mongodb.utils.ModelUtils.jsonNodeToDocument;

public class DomainEntity {

    public static ObjectNode toObjectNode(String id, Document document) {
        if (document == null) {
            return null;
        }

        ObjectNode node = ModelUtils.documentToJsonNode(document, ObjectNode.class);
        node.remove(MongoOperations.ID);
        node.put(DomainStorage.ID, id);
        return node;
    }

    public static Document fromObjectNode(String id, ObjectNode node) {
        if (node == null) {
            return null;
        }

        ObjectNode n = node.deepCopy();
        n.remove(DomainStorage.ID);
        return jsonNodeToDocument(n).append(MongoOperations.ID, new BsonString(id));
    }
}
