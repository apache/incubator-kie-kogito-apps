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

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.kie.kogito.index.jpa.model.AbstractEntity;
import org.kie.kogito.persistence.api.query.AttributeFilter;
import org.kie.kogito.persistence.api.query.AttributeSort;
import org.kie.kogito.persistence.api.query.Query;
import org.kie.kogito.persistence.api.query.SortDirection;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
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

    public JPAQuery(EntityManager em, Function<E, T> mapper, Class<E> entityClass) {
        this(em, mapper, entityClass, Optional.empty());
    }

    public JPAQuery(EntityManager em, Function<E, T> mapper, Class<E> entityClass, Optional<JsonPredicateBuilder> jsonPredicateBuilder) {
        this.em = em;
        this.mapper = mapper;
        this.entityClass = entityClass;
        this.jsonPredicateBuilder = jsonPredicateBuilder;
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
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<E> criteriaQuery = builder.createQuery(entityClass);
        Root<E> root = criteriaQuery.from(entityClass);
        addWhere(builder, criteriaQuery, root);
        if (sortBy != null && !sortBy.isEmpty()) {
            List<Order> orderBy = sortBy.stream().map(f -> {
                Path attributePath = getAttributePath(root, f.getAttribute());
                return f.getSort() == SortDirection.ASC ? builder.asc(attributePath) : builder.desc(attributePath);
            }).collect(toList());
            criteriaQuery.orderBy(orderBy);
        }
        jakarta.persistence.Query query = em.createQuery(criteriaQuery);
        if (limit != null) {
            query.setMaxResults(limit);
        }
        if (offset != null) {
            query.setFirstResult(offset);
        }
        return (List<T>) query.getResultList().stream().map(mapper).collect(toList());
    }

    protected Function<AttributeFilter<?>, Predicate> filterPredicateFunction(Root<E> root, CriteriaBuilder builder, CriteriaQuery<?> criteriaQuery) {
        return filter -> jsonPredicateBuilder.filter(b -> filter.isJson()).map(b -> b.buildPredicate(filter, root, builder))
                .orElseGet(() -> buildPredicateFunction(filter, root, builder, criteriaQuery));
    }

    protected final Predicate buildPredicateFunction(AttributeFilter filter, Root<E> root, CriteriaBuilder builder, CriteriaQuery<?> criteriaQuery) {
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
                return builder.or(getRecursivePredicate(filter, root, builder, criteriaQuery).toArray(new Predicate[] {}));
            case AND:
                return builder.and(getRecursivePredicate(filter, root, builder, criteriaQuery).toArray(new Predicate[] {}));
            case NOT:
                AttributeFilter<?> innerFilter = (AttributeFilter<?>) filter.getValue();
                // Check if this is a collection operation that needs special handling
                // Collection operations have attributes like "nodes.name" or use CONTAINS/CONTAINS_ALL/CONTAINS_ANY
                if (isCollectionNotOperation(innerFilter)) {
                    return buildCollectionNotPredicate(innerFilter, root, builder, criteriaQuery);
                }
                return builder.not(filterPredicateFunction(root, builder, criteriaQuery).apply(innerFilter));
            default:
                return null;
        }

    }

    /**
     * Checks if the filter is a collection operation (CONTAINS, CONTAINS_ALL, CONTAINS_ANY)
     * or contains collection attributes that require special NOT handling with subqueries.
     */
    private boolean isCollectionNotOperation(AttributeFilter<?> filter) {
        switch (filter.getCondition()) {
            case CONTAINS:
            case CONTAINS_ALL:
            case CONTAINS_ANY:
                return true;
            case AND:
            case OR:
                // Check if any nested filter is a collection operation or has collection attribute
                List<AttributeFilter<?>> nestedFilters = (List<AttributeFilter<?>>) filter.getValue();
                return nestedFilters.stream().anyMatch(f -> isCollectionNotOperation(f) ||
                        (f.getAttribute() != null && isCollectionAttribute(f.getAttribute())));
            case NOT:
                // Recursively check the inner filter
                return isCollectionNotOperation((AttributeFilter<?>) filter.getValue());
            case EQUAL:
            case LIKE:
            case IN:
            case GT:
            case GTE:
            case LT:
            case LTE:
            case BETWEEN:
            case IS_NULL:
            case NOT_NULL:
                // Check if this filter has a collection attribute (e.g., "nodes.name")
                return filter.getAttribute() != null && isCollectionAttribute(filter.getAttribute());
            default:
                return false;
        }
    }

    /**
     * Builds a NOT EXISTS subquery predicate for collection operations.
     * This ensures entity-level negation instead of per-row negation.
     */
    private Predicate buildCollectionNotPredicate(AttributeFilter<?> filter, Root<E> root,
            CriteriaBuilder builder, CriteriaQuery<?> criteriaQuery) {

        // Special handling for AND/OR: Apply De Morgan's Law
        // NOT (A AND B) = NOT A OR NOT B
        // NOT (A OR B) = NOT A AND NOT B
        if (filter.getCondition() == org.kie.kogito.persistence.api.query.FilterCondition.AND) {
            // NOT (A AND B) = NOT A OR NOT B
            List<AttributeFilter<?>> nestedFilters = (List<AttributeFilter<?>>) filter.getValue();
            List<Predicate> notPredicates = new java.util.ArrayList<>();

            for (AttributeFilter<?> nestedFilter : nestedFilters) {
                // Recursively build NOT predicate for each nested filter
                if (isCollectionNotOperation(nestedFilter) || isCollectionAttribute(nestedFilter.getAttribute())) {
                    notPredicates.add(buildCollectionNotPredicate(nestedFilter, root, builder, criteriaQuery));
                } else {
                    notPredicates.add(builder.not(filterPredicateFunction(root, builder, criteriaQuery).apply(nestedFilter)));
                }
            }

            return builder.or(notPredicates.toArray(new Predicate[0]));
        }

        if (filter.getCondition() == org.kie.kogito.persistence.api.query.FilterCondition.OR) {
            // NOT (A OR B) = NOT A AND NOT B
            List<AttributeFilter<?>> nestedFilters = (List<AttributeFilter<?>>) filter.getValue();
            List<Predicate> notPredicates = new java.util.ArrayList<>();

            for (AttributeFilter<?> nestedFilter : nestedFilters) {
                // Recursively build NOT predicate for each nested filter
                if (isCollectionNotOperation(nestedFilter) || isCollectionAttribute(nestedFilter.getAttribute())) {
                    notPredicates.add(buildCollectionNotPredicate(nestedFilter, root, builder, criteriaQuery));
                } else {
                    notPredicates.add(builder.not(filterPredicateFunction(root, builder, criteriaQuery).apply(nestedFilter)));
                }
            }

            return builder.and(notPredicates.toArray(new Predicate[0]));
        }

        // Special handling for CONTAINS_ALL: NOT (A AND B) = NOT A OR NOT B (De Morgan's Law)
        if (filter.getCondition() == org.kie.kogito.persistence.api.query.FilterCondition.CONTAINS_ALL) {
            List<?> values = (List<?>) filter.getValue();
            List<Predicate> notExistsPredicates = new java.util.ArrayList<>();

            for (Object value : values) {
                // Create a subquery for each value: NOT EXISTS (entity with this value)
                Subquery<Integer> subquery = criteriaQuery.subquery(Integer.class);
                Root<E> subRoot = subquery.from(entityClass);

                // Build predicate for this single value
                Path collectionPath = getAttributePath(subRoot, filter.getAttribute());
                Predicate valuePredicate = builder.isMember(value, collectionPath);

                subquery.select(builder.literal(1));
                subquery.where(
                        builder.equal(subRoot.get("id"), root.get("id")),
                        valuePredicate);

                // Add NOT EXISTS for this value
                notExistsPredicates.add(builder.not(builder.exists(subquery)));
            }

            // Return OR of all NOT EXISTS predicates
            return builder.or(notExistsPredicates.toArray(new Predicate[0]));
        }

        // For other operations (CONTAINS, CONTAINS_ANY), use single subquery
        Subquery<Integer> subquery = criteriaQuery.subquery(Integer.class);
        Root<E> subRoot = subquery.from(entityClass);

        // Correlate the subquery root with the outer query root by ID
        subquery.select(builder.literal(1));
        subquery.where(
                builder.equal(subRoot.get("id"), root.get("id")),
                buildInnerPredicateForSubquery(filter, subRoot, builder, criteriaQuery));

        // Return NOT EXISTS predicate
        return builder.not(builder.exists(subquery));
    }

    /**
     * Builds the inner predicate for the subquery, handling the collection operation.
     */
    private Predicate buildInnerPredicateForSubquery(AttributeFilter<?> filter, Root<E> subRoot,
            CriteriaBuilder builder, CriteriaQuery<?> criteriaQuery) {
        // For collection operations, we need to build the predicate using the subquery root
        // This will properly handle the JOIN and apply conditions at the entity level
        return buildPredicateFunction(filter, subRoot, builder, criteriaQuery);
    }

    private Path getAttributePath(Root<E> root, String attribute) {
        String[] split = attribute.split("\\.");
        if (split.length == 1) {
            return root.get(attribute);
        }

        Join join = root.join(split[0]);
        for (int i = 1; i < split.length - 1; i++) {
            join = join.join(split[i]);
        }
        return join.get(split[split.length - 1]);
    }

    private boolean isPluralAttribute(final String attribute) {
        return this.em.getMetamodel().entity(this.entityClass).getDeclaredPluralAttributes().stream()
                .map(Attribute::getName)
                .anyMatch(pluralAttribute -> pluralAttribute.equals(attribute));
    }

    /**
     * Checks if the attribute is a collection attribute (e.g., "nodes.name" starts with "nodes").
     * This helps detect when NOT operations need special subquery handling.
     */
    private boolean isCollectionAttribute(final String attribute) {
        if (attribute == null || !attribute.contains(".")) {
            return false;
        }
        // Extract the first part (e.g., "nodes" from "nodes.name")
        String firstPart = attribute.split("\\.")[0];
        return isPluralAttribute(firstPart);
    }

    private List<Predicate> getRecursivePredicate(AttributeFilter<?> filter, Root<E> root, CriteriaBuilder builder, CriteriaQuery<?> criteriaQuery) {
        return ((List<AttributeFilter<?>>) filter.getValue())
                .stream()
                .map(filterPredicateFunction(root, builder, criteriaQuery))
                .collect(toList());
    }

    @Override
    public long count() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
        Root<E> root = criteriaQuery.from(entityClass);
        criteriaQuery.select(builder.count(root));
        addWhere(builder, criteriaQuery, root);
        return em.createQuery(criteriaQuery).getSingleResult();
    }

    private <V> void addWhere(CriteriaBuilder builder, CriteriaQuery<V> criteriaQuery, Root<E> root) {
        if (filters != null && !filters.isEmpty()) {
            criteriaQuery.where(filters.stream().map(filterPredicateFunction(root, builder, criteriaQuery)).toArray(Predicate[]::new));
        }
    }
}
