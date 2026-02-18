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
package org.kie.kogito.app.audit.jpa.queries.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.kie.kogito.app.audit.jpa.queries.DataMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PojoMapper<T> implements DataMapper<T, Object[]> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PojoMapper.class);

    private Class<T> clazz;
    private Constructor<T> defaultConstructor;

    public PojoMapper(Class<T> clazz) {
        this.clazz = clazz;
        for (Constructor<?> constructor : clazz.getConstructors()) {
            if (constructor.getParameterCount() > 0) {
                defaultConstructor = (Constructor<T>) constructor;
            }
        }
    }

    @Override
    public List<T> produce(List<Object[]> data) {
        List<T> transformed = new ArrayList<>();
        Class<?>[] paramTypes = defaultConstructor.getParameterTypes();
        for (Object[] row : data) {
            try {
                // Hibernate 7 changed default return types for native queries
                // (e.g. OffsetDateTime instead of java.util.Date, Long instead of Integer).
                // Convert each value to match the constructor's declared parameter types.
                Object[] convertedRow = convertTypes(row, paramTypes);
                transformed.add(defaultConstructor.newInstance(convertedRow));
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                LOGGER.error("Could not transform data", e);
            }
        }
        return transformed;
    }

    private static Object[] convertTypes(Object[] row, Class<?>[] paramTypes) {
        Object[] result = new Object[row.length];
        for (int i = 0; i < row.length; i++) {
            result[i] = (i < paramTypes.length) ? convertValue(row[i], paramTypes[i]) : row[i];
        }
        return result;
    }

    private static Object convertValue(Object value, Class<?> targetType) {
        if (value == null || targetType.isInstance(value)) {
            return value;
        }
        // Hibernate 7 returns java.time types instead of java.util.Date
        if (targetType == Date.class) {
            if (value instanceof OffsetDateTime) {
                return Date.from(((OffsetDateTime) value).toInstant());
            }
            if (value instanceof Instant) {
                return Date.from((Instant) value);
            }
            if (value instanceof LocalDateTime) {
                return Date.from(((LocalDateTime) value).atZone(ZoneId.of("UTC")).toInstant());
            }
        }
        // Hibernate 7 may return different numeric types for native query columns
        if (targetType == Integer.class || targetType == int.class) {
            if (value instanceof Number) {
                return ((Number) value).intValue();
            }
        }
        if (targetType == Long.class || targetType == long.class) {
            if (value instanceof Number) {
                return ((Number) value).longValue();
            }
        }
        return value;
    }

}
