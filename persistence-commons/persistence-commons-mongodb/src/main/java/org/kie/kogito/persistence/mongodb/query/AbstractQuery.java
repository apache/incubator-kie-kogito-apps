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

package org.kie.kogito.persistence.mongodb.query;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.conversions.Bson;
import org.kie.kogito.persistence.api.query.AttributeFilter;
import org.kie.kogito.persistence.api.query.AttributeSort;
import org.kie.kogito.persistence.api.query.Query;
import org.kie.kogito.persistence.api.query.SortDirection;
import org.kie.kogito.persistence.mongodb.utils.QueryUtils;

import static com.mongodb.client.model.Sorts.ascending;
import static com.mongodb.client.model.Sorts.descending;
import static com.mongodb.client.model.Sorts.orderBy;
import static java.util.stream.Collectors.toList;
import static org.kie.kogito.persistence.mongodb.utils.QueryUtils.CONVERT_ATTRIBUTE_FUNCTION;

public abstract class AbstractQuery<T, E> implements Query<T> {

    Integer limit;
    Integer offset;
    List<AttributeFilter<?>> filters;
    List<AttributeSort> sortBy;

    @Override
    public Query<T> limit(Integer limit) {
        this.limit = limit;
        return this;
    }

    @Override
    public Query<T> offset(Integer offset) {
        this.offset = offset;
        return this;
    }

    @Override
    public Query<T> filter(List<AttributeFilter<?>> filters) {
        this.filters = filters;
        return this;
    }

    @Override
    public Query<T> sort(List<AttributeSort> sortBy) {
        this.sortBy = sortBy;
        return this;
    }

    @Override
    public List<T> execute() {
        MongoCollection<E> collection = this.getCollection();
        Optional<Bson> query = QueryUtils.generateQuery(this.filters, this.convertAttributeFunction());
        Optional<Bson> sort = this.generateSort();

        FindIterable<E> find = query.map(collection::find).orElseGet(collection::find);
        find = sort.map(find::sort).orElse(find);
        find = Optional.ofNullable(this.offset).map(find::skip).orElse(find);
        find = Optional.ofNullable(this.limit).map(find::limit).orElse(find);

        List<T> list = new LinkedList<>();
        try (MongoCursor<E> cursor = find.iterator()) {
            while (cursor.hasNext()) {
                list.add(mapToModel(cursor.next()));
            }
        }
        return list;
    }

    protected abstract MongoCollection<E> getCollection();

    protected abstract T mapToModel(E e);

    private Optional<Bson> generateSort() {
        return Optional.ofNullable(this.sortBy).map(sortBy -> orderBy(sortBy.stream().map(
                sb -> SortDirection.ASC.equals(sb.getSort()) ?
                        ascending(this.convertAttributeFunction().apply(sb.getAttribute())) :
                        descending(this.convertAttributeFunction().apply(sb.getAttribute())))
                                                                              .collect(toList()))
        );
    }

    protected Function<String, String> convertAttributeFunction() {
        return CONVERT_ATTRIBUTE_FUNCTION;
    }
}
