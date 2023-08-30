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

package org.kie.kogito.persistence.oracle.hibernate;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.SqlTypes;
import org.hibernate.usertype.UserType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import static java.lang.String.format;

public class JsonBinaryType implements UserType<JsonNode> {

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public int getSqlType() {
        return SqlTypes.BLOB;
    }

    @Override
    public Class<JsonNode> returnedClass() {
        return JsonNode.class;
    }

    @Override
    public boolean equals(JsonNode x, JsonNode y) throws HibernateException {
        return Objects.equals(x, y);
    }

    @Override
    public int hashCode(JsonNode x) throws HibernateException {
        return Objects.hashCode(x);
    }

    @Override
    public JsonNode nullSafeGet(ResultSet rs, int position, SharedSessionContractImplementor session, Object owner)
            throws SQLException {
        final Blob json = rs.getBlob(position);
        if (json.length() == 0) {
            return null;
        }
        try {
            return mapper.readTree(json.getBinaryStream());
        } catch (final Exception ex) {
            throw new RuntimeException("Failed to convert String to JSON: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void nullSafeSet(PreparedStatement ps, JsonNode value, int index, SharedSessionContractImplementor session)
            throws SQLException {
        if (value == null) {
            ps.setNull(index, SqlTypes.BLOB);
            return;
        }
        try {
            ps.setBlob(index, new ByteArrayInputStream(value.toString().getBytes()));
        } catch (final Exception ex) {
            throw new RuntimeException(format("Failed to convert JSON to String: %s", ex.getMessage()), ex);
        }
    }

    @Override
    public JsonNode deepCopy(JsonNode value) throws HibernateException {
        if (value == null) {
            return null;
        }
        return value.deepCopy();
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Serializable disassemble(JsonNode value) throws HibernateException {
        return this.deepCopy(value).toString();
    }

    @Override
    public JsonNode assemble(Serializable cached, Object owner) throws HibernateException {
        try {
            return mapper.readTree(cached.toString());
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(format("Failed to convert String to JSON: %s", ex.getMessage()), ex);
        }
    }

    @Override
    public JsonNode replace(JsonNode original, JsonNode target, Object owner) throws HibernateException {
        return original;
    }
}
