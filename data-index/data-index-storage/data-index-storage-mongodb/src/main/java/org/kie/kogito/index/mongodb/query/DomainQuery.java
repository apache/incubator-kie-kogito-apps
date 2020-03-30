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

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import javax.enterprise.context.Dependent;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.kie.kogito.index.mongodb.model.DomainEntity;
import org.kie.kogito.index.mongodb.utils.MongoDBUtils;
import org.kie.kogito.index.query.SortDirection;

import static java.lang.String.format;
import static org.kie.kogito.index.mongodb.utils.MongoDBUtils.filterValueAsStringFunction;
import static org.kie.kogito.index.mongodb.utils.MongoDBUtils.getCollection;

@Dependent
public class DomainQuery extends AbstractQuery<ObjectNode> {

    private static Function<String, String> filterAttributeFunction = attribute -> format("'domainData.%s'", attribute);

    String processId;

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    @Override
    public List<ObjectNode> execute() {
        MongoCollection<DomainEntity> collection = getCollection(this.processId, DomainEntity.class);
        Optional<Document> query = MongoDBUtils.generateQueryString(this.filters, filterAttributeFunction, filterValueAsStringFunction).map(Document::parse);
        Optional<Document> sort = this.generateSort();

        FindIterable<DomainEntity> find = query.map(collection::find).orElseGet(collection::find);
        find = sort.map(find::sort).orElse(find);
        find = Optional.ofNullable(this.offset).map(find::skip).orElse(find);
        find = Optional.ofNullable(this.limit).map(find::limit).orElse(find);

        List<ObjectNode> list = new LinkedList<>();
        try (MongoCursor<DomainEntity> cursor = find.iterator()) {
            while (cursor.hasNext()) {
                list.add(DomainEntity.toDomainObject(cursor.next()));
            }
        }
        return list;
    }

    private Optional<Document> generateSort() {
        return Optional.ofNullable(this.sortBy).map(sortBy -> sortBy.stream().reduce(
                new Document(), (d, sb) -> d.append(filterAttributeFunction.apply(sb.getAttribute()), SortDirection.ASC.equals(sb.getSort()) ? 1 : -1), (a, b) -> a));
    }
}
