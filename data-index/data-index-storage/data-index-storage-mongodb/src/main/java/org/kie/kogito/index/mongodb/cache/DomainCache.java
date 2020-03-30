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

package org.kie.kogito.index.mongodb.cache;

import java.util.Optional;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mongodb.client.model.ReplaceOptions;
import io.quarkus.mongodb.panache.runtime.MongoOperations;
import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.Document;
import org.kie.kogito.index.mongodb.model.DomainEntity;
import org.kie.kogito.index.mongodb.query.DomainQuery;
import org.kie.kogito.index.mongodb.utils.MongoDBUtils;
import org.kie.kogito.index.query.Query;

@Dependent
public class DomainCache extends AbstractCache<String, ObjectNode> {

    @Inject
    ProcessIdCache processIdCache;

    @Inject
    DomainQuery domainQuery;

    String processId;

    public void setProcessId(String processId) {
        this.processId = processId;
        this.domainQuery.setProcessId(processId);
    }

    @Override
    public Query<ObjectNode> query() {
        return domainQuery;
    }

    @Override
    public String getRootType() {
        return processIdCache.get(processId);
    }

    @Override
    public ObjectNode get(Object o) {
        return Optional.ofNullable(MongoDBUtils.getCollection(this.processId, DomainEntity.class).find(new Document(MongoOperations.ID, o)).first()).map(DomainEntity::toDomainObject).orElse(null);
    }

    @Override
    public ObjectNode put(String s, ObjectNode jsonNodes) {
        ObjectNode oldNode = this.get(s);
        Optional.ofNullable(DomainEntity.fromDomainObject(s, jsonNodes)).ifPresent(
                entity -> MongoDBUtils.getCollection(this.processId, DomainEntity.class).replaceOne(
                        new BsonDocument().append(MongoOperations.ID, new BsonString(entity.processInstanceId)),
                        entity, new ReplaceOptions().upsert(true)));
        Optional.ofNullable(oldNode).map(o -> this.objectUpdatedListener).orElseGet(() -> this.objectCreatedListener).ifPresent(l -> l.accept(jsonNodes));
        return oldNode;
    }

    @Override
    public void clear() {
        MongoDBUtils.getCollection(this.processId, DomainEntity.class).deleteMany(new Document());
    }

    @Override
    public ObjectNode remove(Object o) {
        ObjectNode oldNode = this.get(o);
        Optional.ofNullable(oldNode).ifPresent(i -> MongoDBUtils.getCollection(this.processId, DomainEntity.class).deleteOne(new Document().append("_id", o)));
        Optional.ofNullable(oldNode).flatMap(i -> this.objectRemovedListener).ifPresent(l -> l.accept((String) o));
        return oldNode;
    }

    @Override
    public int size() {
        return (int) MongoDBUtils.getCollection(this.processId, DomainEntity.class).countDocuments();
    }

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }
}
