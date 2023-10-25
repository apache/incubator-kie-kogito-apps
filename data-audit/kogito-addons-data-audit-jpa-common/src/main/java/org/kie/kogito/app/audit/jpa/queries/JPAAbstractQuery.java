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
package org.kie.kogito.app.audit.jpa.queries;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public abstract class JPAAbstractQuery {

    protected <T> List<T> executeWithEntityManager(EntityManager entityManager, String query, Class<T> clazz) {
        return entityManager.createQuery(query, clazz).getResultList();

    }

    protected <T> List<T> executeWithNamedQueryEntityManager(EntityManager entityManager, String query, Class<T> clazz) {

        return entityManager.createNamedQuery(query, clazz).getResultList();

    }

    protected <T> List<T> executeWithNamedQueryEntityManagerAndArguments(EntityManager entityManager, String query, Class<T> clazz, Map<String, Object> arguments) {

        TypedQuery<T> typedQuery = entityManager.createNamedQuery(query, clazz);
        arguments.forEach(typedQuery::setParameter);
        return typedQuery.getResultList();

    }

}
