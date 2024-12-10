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
package org.kie.kogito.index.postgresql;

import org.kie.kogito.persistence.api.query.AttributeFilter;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class PostgresqlJsonNavigator {

    private PostgresqlJsonNavigator() {
    }

    public static Predicate buildPredicate(AttributeFilter<?> filter, Root<?> root,
            CriteriaBuilder builder) {
        switch (filter.getCondition()) {
            case EQUAL:
                boolean isString = filter.getValue() instanceof String;
                return builder.equal(buildPathExpression(builder, root, filter.getAttribute(), isString), buildObjectExpression(builder, filter.getValue(), isString));
        }
        throw new UnsupportedOperationException();
    }

    private static Expression<?> buildObjectExpression(CriteriaBuilder builder, Object value, boolean isString) {
        return isString ? builder.literal(value) : builder.function("to_jsonb", Object.class, builder.literal(value));
    }

    private static Expression<?> buildPathExpression(CriteriaBuilder builder, Root<?> root, String attributeName, boolean isStr) {
        String[] attributes = attributeName.split("\\.");
        Expression<?>[] arguments = new Expression[attributes.length];
        arguments[0] = root.get(attributes[0]);
        for (int i = 1; i < attributes.length; i++) {
            arguments[i] = builder.literal(attributes[i]);
        }
        return isStr ? builder.function("jsonb_extract_path_text", String.class, arguments) : builder.function("jsonb_extract_path", Object.class, arguments);
    }

}
