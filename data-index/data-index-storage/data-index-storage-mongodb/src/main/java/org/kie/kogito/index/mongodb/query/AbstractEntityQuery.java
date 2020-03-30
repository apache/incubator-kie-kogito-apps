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

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import io.quarkus.mongodb.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import org.kie.kogito.index.mongodb.utils.MongoDBUtils;
import org.kie.kogito.index.query.AttributeSort;
import org.kie.kogito.index.query.SortDirection;

import static java.lang.String.format;
import static org.kie.kogito.index.mongodb.utils.MongoDBUtils.filterValueAsStringFunction;

public abstract class AbstractEntityQuery<T, E extends PanacheMongoEntityBase> extends AbstractQuery<T> {

    @Override
    public List<T> execute() {
        Optional<String> queryString = MongoDBUtils.generateQueryString(this.filters, this.getFilterAttributeFunction(), this.getFilterValueAsStringFunction());
        Optional<Sort> sort = generateSort();
        PanacheQuery<E> query = queryString.map(q -> sort.map(s -> queryWithSort(q, s)).orElseGet(() -> query(q)))
                .orElseGet(() -> sort.map(this::queryAllWithSort).orElseGet(this::queryAll));
        return generatePage().map(query::page).orElse(query).stream().map(this.convertFunction()).collect(Collectors.toList());
    }

    abstract PanacheQuery<E> queryWithSort(String queryString, Sort sort);

    abstract PanacheQuery<E> queryAllWithSort(Sort sort);

    abstract PanacheQuery<E> query(String queryString);

    abstract PanacheQuery<E> queryAll();

    abstract Function<E, T> convertFunction();

    private Optional<Sort> generateSort() {
        return Optional.ofNullable(this.sortBy).map(sortBy -> {
            if (sortBy.isEmpty()) {
                return null;
            } else {
                AttributeSort firstAttributeSort = sortBy.get(0);
                Sort firstSort = Sort.by(getFilterAttributeFunction().apply(firstAttributeSort.getAttribute()), mapSortDirection(firstAttributeSort.getSort()));
                if (sortBy.size() == 1) {
                    return firstSort;
                } else {
                    return sortBy.stream().skip(1).reduce(firstSort, (s, sb) -> s.and(getFilterAttributeFunction().apply(sb.getAttribute()), mapSortDirection(sb.getSort())), (a, b) -> a);
                }
            }
        });
    }

    private Optional<Page> generatePage() {
        Optional<Integer> offset = Optional.ofNullable(this.offset);
        return Optional.ofNullable(this.limit).map(Page::ofSize).map(p -> offset.map(p::index))
                .orElseGet(() -> offset.map(o -> Page.of(o, Integer.MAX_VALUE)));
    }

    private Sort.Direction mapSortDirection(SortDirection direction) {
        switch (direction) {
            case ASC:
                return Sort.Direction.Ascending;
            case DESC:
                return Sort.Direction.Descending;
        }
        return null;
    }

    BiFunction<String, Object, String> getFilterValueAsStringFunction() {
        return filterValueAsStringFunction;
    }

    Function<String, String> getFilterAttributeFunction() {
        return attribute -> format("'%s'", "id".equalsIgnoreCase(attribute) ? "_id" : attribute);
    }
}
