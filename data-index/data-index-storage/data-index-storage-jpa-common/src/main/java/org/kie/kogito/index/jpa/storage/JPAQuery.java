/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.kie.kogito.index.jpa.storage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.hibernate.query.criteria.*;
import org.kie.kogito.index.jpa.model.AbstractEntity;
import org.kie.kogito.index.jpa.model.DataIsolationKeyDescriptor;
import org.kie.kogito.index.jpa.model.DataIsolationKeyDescriptorRegistry;
import org.kie.kogito.persistence.api.query.AttributeFilter;
import org.kie.kogito.persistence.api.query.AttributeSort;
import org.kie.kogito.persistence.api.query.Query;
import org.kie.kogito.persistence.api.query.SortDirection;
import org.kie.kogito.process.Processes;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.*;
import jakarta.persistence.metamodel.Attribute;

import static java.util.stream.Collectors.toList;

public class JPAQuery<E extends AbstractEntity, T> implements Query<T> {

    protected final EntityManager em;
    private Integer limit;
    private Integer offset;
    private List<AttributeFilter<?>> filters;
    private List<AttributeSort> sortBy;
    protected final Class<E> entityClass;
    protected final Function<E, T> mapper;
    private Optional<JsonPredicateBuilder> jsonPredicateBuilder;
    private Optional<Processes> processes;

    public JPAQuery(EntityManager em, Function<E, T> mapper, Class<E> entityClass) {
        this(em, mapper, entityClass, Optional.empty(), Optional.empty());
    }

    public JPAQuery(EntityManager em, Function<E, T> mapper, Class<E> entityClass, Optional<JsonPredicateBuilder> jsonPredicateBuilder) {
        this(em, mapper, entityClass, jsonPredicateBuilder, Optional.empty());
    }

    public JPAQuery(EntityManager em, Function<E, T> mapper, Class<E> entityClass, Optional<JsonPredicateBuilder> jsonPredicateBuilder, Optional<Processes> processes) {
        this.em = em;
        this.mapper = mapper;
        this.entityClass = entityClass;
        this.jsonPredicateBuilder = jsonPredicateBuilder;
        this.processes = processes;
    }

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
        HibernateCriteriaBuilder builder = (HibernateCriteriaBuilder) em.getCriteriaBuilder();
        JpaCriteriaQuery<E> jpaMainQuery = builder.createQuery(entityClass);
        JpaRoot<E> root = jpaMainQuery.from(entityClass);

        applyFilters(builder, jpaMainQuery, root);

        if (sortBy != null && !sortBy.isEmpty()) {
            List<Order> orderBy = sortBy.stream().map(f -> {
                Path<?> attributePath = getAttributePath(root, f.getAttribute());
                return f.getSort() == SortDirection.ASC
                        ? builder.asc(attributePath)
                        : builder.desc(attributePath);
            }).collect(toList());
            jpaMainQuery.orderBy(orderBy);
        }

        jpaMainQuery.select(root);

        var query = em.createQuery(jpaMainQuery);
        if (limit != null) {
            query.setMaxResults(limit);
        }
        if (offset != null) {
            query.setFirstResult(offset);
        }

        return query.getResultList().stream()
                .map(mapper)
                .collect(toList());
    }

    protected Function<AttributeFilter<?>, Predicate> filterPredicateFunction(Root<E> root, CriteriaBuilder builder) {
        return filter -> jsonPredicateBuilder.filter(b -> filter.isJson()).map(b -> b.buildPredicate(filter, root, builder))
                .orElseGet(() -> buildPredicateFunction(filter, root, builder));
    }

    protected final Predicate buildPredicateFunction(AttributeFilter filter, Root<E> root, CriteriaBuilder builder) {
        switch (filter.getCondition()) {
            case CONTAINS:
                return builder.isMember(filter.getValue(), getAttributePath(root, filter.getAttribute()));
            case CONTAINS_ALL:
                List<Predicate> predicatesAll = (List<Predicate>) ((List) filter.getValue()).stream()
                        .map(o -> builder.isMember(o, getAttributePath(root, filter.getAttribute()))).collect(toList());
                return builder.and(predicatesAll.toArray(new Predicate[] {}));
            case CONTAINS_ANY:
                List<Predicate> predicatesAny = (List<Predicate>) ((List) filter.getValue()).stream()
                        .map(o -> builder.isMember(o, getAttributePath(root, filter.getAttribute()))).collect(toList());
                return builder.or(predicatesAny.toArray(new Predicate[] {}));
            case IN:
                return getAttributePath(root, filter.getAttribute()).in((Collection<?>) filter.getValue());
            case LIKE:
                return builder.like(getAttributePath(root, filter.getAttribute()),
                        filter.getValue().toString().replaceAll("\\*", "%"));
            case EQUAL:
                return builder.equal(getAttributePath(root, filter.getAttribute()), filter.getValue());
            case IS_NULL:
                Path pathNull = getAttributePath(root, filter.getAttribute());
                return isPluralAttribute(filter.getAttribute()) ? builder.isEmpty(pathNull) : builder.isNull(pathNull);
            case NOT_NULL:
                Path pathNotNull = getAttributePath(root, filter.getAttribute());
                return isPluralAttribute(filter.getAttribute()) ? builder.isNotEmpty(pathNotNull) : builder.isNotNull(pathNotNull);
            case BETWEEN:
                List<Object> value = (List<Object>) filter.getValue();
                return builder
                        .between(getAttributePath(root, filter.getAttribute()), (Comparable) value.get(0),
                                (Comparable) value.get(1));
            case GT:
                return builder.greaterThan(getAttributePath(root, filter.getAttribute()), (Comparable) filter.getValue());
            case GTE:
                return builder.greaterThanOrEqualTo(getAttributePath(root, filter.getAttribute()),
                        (Comparable) filter.getValue());
            case LT:
                return builder.lessThan(getAttributePath(root, filter.getAttribute()), (Comparable) filter.getValue());
            case LTE:
                return builder
                        .lessThanOrEqualTo(getAttributePath(root, filter.getAttribute()), (Comparable) filter.getValue());
            case OR:
                return builder.or(getRecursivePredicate(filter, root, builder).toArray(new Predicate[] {}));
            case AND:
                return builder.and(getRecursivePredicate(filter, root, builder).toArray(new Predicate[] {}));
            case NOT:
                return builder.not(filterPredicateFunction(root, builder).apply((AttributeFilter<?>) filter.getValue()));
            default:
                return null;
        }

    }

    private Path getAttributePath(Root<E> root, String attribute) {
        String[] split = attribute.split("\\.");
        if (split.length == 1) {
            return root.get(attribute);
        }

        Path path = root.get(split[0]);
        for (int i = 1; i < split.length; i++) {
            path = path.get(split[i]);
        }
        return path;
    }

    private boolean isPluralAttribute(final String attribute) {
        return this.em.getMetamodel().entity(this.entityClass).getDeclaredPluralAttributes().stream()
                .map(Attribute::getName)
                .anyMatch(pluralAttribute -> pluralAttribute.equals(attribute));
    }

    private List<Predicate> getRecursivePredicate(AttributeFilter<?> filter, Root<E> root, CriteriaBuilder builder) {
        return ((List<AttributeFilter<?>>) filter.getValue())
                .stream()
                .map(filterPredicateFunction(root, builder))
                .collect(toList());
    }

    @Override
    public long count() {
        HibernateCriteriaBuilder builder = (HibernateCriteriaBuilder) em.getCriteriaBuilder();
        JpaCriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
        JpaRoot<E> root = criteriaQuery.from(entityClass);
        criteriaQuery.select(builder.count(root));

        applyFilters(builder, criteriaQuery, root);

        return em.createQuery(criteriaQuery).getSingleResult();
    }

    /**
     * Applies all filtering (data isolation + user filters) and sets the WHERE clause.
     *
     * @param builder the Hibernate criteria builder
     * @param criteriaQuery the criteria query to apply filters to
     * @param root the query root
     */
    private void applyFilters(
            HibernateCriteriaBuilder builder,
            JpaCriteriaQuery<?> criteriaQuery,
            Root<E> root) {

        List<Predicate> predicates = new ArrayList<>();

        // Apply data isolation filtering
        applyDataIsolationFiltering(builder, criteriaQuery, root, predicates);

        // Apply user-defined filters
        if (filters != null && !filters.isEmpty()) {
            predicates.addAll(filters.stream()
                    .map(filterPredicateFunction(root, builder))
                    .toList());
        }

        // Set WHERE clause if predicates exist
        if (!predicates.isEmpty()) {
            criteriaQuery.where(predicates.toArray(new Predicate[0]));
        }
    }

    /**
     * Applies data isolation filtering using CTE if process IDs are present.
     * This method adds the CTE join predicate to the predicates list.
     *
     * @param builder the Hibernate criteria builder
     * @param query the JPA criteria query
     * @param root the query root
     * @param predicates the list to add predicates to
     */
    private void applyDataIsolationFiltering(
            HibernateCriteriaBuilder builder,
            JpaCriteriaQuery<?> query,
            Root<E> root,
            List<Predicate> predicates) {

        if (processes.isEmpty()) {
            return;
        }

        if (processes.get().processIds().isEmpty()) {
            predicates.add(builder.disjunction());
            return;
        }

        JpaCteCriteria<Tuple> cte = buildDataIsolationFilteringCte(builder, query);

        JpaSubQuery<Integer> subquery = query.subquery(Integer.class);
        JpaRoot<Tuple> cteRoot = subquery.from(cte);

        subquery.select(builder.literal(1));

        DataIsolationKeyDescriptor descriptor = DataIsolationKeyDescriptorRegistry.getDescriptor(entityClass);
        Path<String> rootProcessIdPath = descriptor.rootProcessId() != null ? getAttributePath(root, descriptor.rootProcessId()) : null;
        Path<String> processIdPath = getAttributePath(root, descriptor.processId());

        Predicate correlationPredicate;
        if (rootProcessIdPath != null) {
            correlationPredicate = builder.or(
                    builder.equal(rootProcessIdPath, cteRoot.get("pid")),
                    builder.and(
                            builder.isNull(rootProcessIdPath),
                            builder.equal(processIdPath, cteRoot.get("pid"))));
        } else {
            correlationPredicate = builder.equal(processIdPath, cteRoot.get("pid"));
        }

        subquery.where(correlationPredicate);
        predicates.add(builder.exists(subquery));
    }

    private JpaCteCriteria<Tuple> buildDataIsolationFilteringCte(
            HibernateCriteriaBuilder builder,
            JpaCriteriaQuery<?> mainQuery) {
        List<JpaCriteriaQuery<Tuple>> rows = processes.get().processIds().stream().map(id -> {
            JpaCriteriaQuery<Tuple> row = builder.createTupleQuery();
            row.select(builder.tuple(
                    builder.literal(id).alias("pid")));
            return row;
        }).toList();

        if (rows.isEmpty()) {
            return null;
        }

        CriteriaQuery<Tuple> unionedRowsQuery = rows.get(0);
        for (int i = 1; i < rows.size(); i++) {
            unionedRowsQuery = builder.unionAll(unionedRowsQuery, rows.get(i));
        }

        return mainQuery.with("allowed_processes", unionedRowsQuery);
    }

}
