/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
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
package org.kie.kogito.index.postgresql.storage;

import java.util.List;
import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kie.kogito.index.model.ProcessInstance;
import org.kie.kogito.index.postgresql.model.ProcessInstanceEntity;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.kie.kogito.persistence.api.query.QueryFilterFactory.orderBy;
import static org.kie.kogito.persistence.api.query.SortDirection.ASC;
import static org.kie.kogito.persistence.api.query.SortDirection.DESC;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DomainQueryTest {

    private static final Class rootType = ProcessInstanceEntity.class;

    @Mock
    PanacheRepositoryBase<ProcessInstance, String> repository;

    @Mock
    EntityManager entityManager;

    @Mock
    CriteriaBuilder criteriaBuilder;

    @Mock
    CriteriaQuery criteriaQuery;

    @Mock
    TypedQuery mockQuery;

    @BeforeEach
    public void setup() {
        when(repository.getEntityManager()).thenReturn(entityManager);
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(rootType)).thenReturn(criteriaQuery);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(mockQuery);
    }

    @Test
    void testNoParameters() {
        PostgreSqlQuery query = new PostgreSqlQuery(repository, Function.identity(), rootType);

        query.execute();

        verify(criteriaQuery).from(rootType);
        verify(criteriaQuery, never()).where(any(Predicate.class));
        verify(criteriaQuery, never()).orderBy(any(List.class));
        verify(entityManager).createQuery(criteriaQuery);
        verify(mockQuery).getResultList();
    }

    @Test
    void testEmptyParameters() {
        PostgreSqlQuery query = new PostgreSqlQuery(repository, Function.identity(), rootType);
        query.filter(emptyList());
        query.sort(emptyList());

        query.execute();

        verify(criteriaQuery).from(rootType);
        verify(criteriaQuery, never()).where(any(Predicate.class));
        verify(criteriaQuery, never()).orderBy(any(List.class));
        verify(entityManager).createQuery(criteriaQuery);
        verify(mockQuery).getResultList();
    }

    @Test
    void testPagination() {
        PostgreSqlQuery query = new PostgreSqlQuery(repository, Function.identity(), rootType);
        query.limit(10);
        query.offset(0);

        query.execute();

        verify(criteriaQuery).from(rootType);
        verify(criteriaQuery, never()).where(any(Predicate.class));
        verify(criteriaQuery, never()).orderBy(any(List.class));
        verify(entityManager).createQuery(criteriaQuery);
        verify(mockQuery).setFirstResult(0);
        verify(mockQuery).setMaxResults(10);
        verify(mockQuery).getResultList();
    }

    @Test
    void testOrderBy() {
        Root root = mock(Root.class);
        when(root.get(any(String.class))).thenAnswer(inv -> {
            Path path = mock(Path.class);
            when(path.getAlias()).thenReturn(inv.getArgument(0));
            return path;
        });
        when(criteriaQuery.from(rootType)).thenReturn(root);
        when(criteriaBuilder.asc(any())).thenAnswer(inv -> {
            Order order = mock(Order.class);
            when(order.isAscending()).thenReturn(true);
            when(order.getExpression()).thenReturn(inv.getArgument(0));
            return order;
        });
        when(criteriaBuilder.desc(any())).thenAnswer(inv -> {
            Order order = mock(Order.class);
            when(order.isAscending()).thenReturn(false);
            when(order.getExpression()).thenReturn(inv.getArgument(0));
            return order;
        });

        PostgreSqlQuery query = new PostgreSqlQuery(repository, Function.identity(), rootType);
        query.sort(asList(orderBy("name", DESC), orderBy("date", ASC)));

        query.execute();

        verify(criteriaQuery).from(rootType);
        verify(criteriaQuery, never()).where(any(Predicate.class));
        ArgumentCaptor<List<Order>> captor = ArgumentCaptor.forClass(List.class);
        verify(criteriaQuery).orderBy(captor.capture());
        assertThat(captor.getValue()).hasSize(2);
        assertThat(captor.getValue().get(0).isAscending()).isFalse();
        assertThat(captor.getValue().get(0).getExpression().getAlias()).isEqualTo("name");
        assertThat(captor.getValue().get(1).isAscending()).isTrue();
        assertThat(captor.getValue().get(1).getExpression().getAlias()).isEqualTo("date");
        verify(entityManager).createQuery(criteriaQuery);
        verify(mockQuery).getResultList();
    }

}
