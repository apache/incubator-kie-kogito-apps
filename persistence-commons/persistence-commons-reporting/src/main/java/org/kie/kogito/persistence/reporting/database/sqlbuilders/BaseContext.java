/*
 * Copyright 2022 Red Hat, Inc. and/or its affiliates.
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
package org.kie.kogito.persistence.reporting.database.sqlbuilders;

import java.util.List;
import java.util.Objects;

import org.kie.kogito.persistence.reporting.model.Field;
import org.kie.kogito.persistence.reporting.model.Mapping;
import org.kie.kogito.persistence.reporting.model.PartitionField;
import org.kie.kogito.persistence.reporting.model.paths.PathSegment;

public abstract class BaseContext<T, F extends Field<T>, P extends PartitionField<T>, M extends Mapping<T, F>> implements Context<T, F, P, M> {

    private final String mappingId;
    private final String sourceTableName;
    private final String sourceTableJsonFieldName;
    private final List<F> sourceTableIdentityFields;
    private final List<P> sourceTablePartitionFields;
    private final String targetTableName;
    private final List<M> mappings;
    private final List<PathSegment> mappingPaths;

    protected BaseContext(final String mappingId,
            final String sourceTableName,
            final String sourceTableJsonFieldName,
            final List<F> sourceTableIdentityFields,
            final List<P> sourceTablePartitionFields,
            final String targetTableName,
            final List<M> mappings,
            final List<PathSegment> mappingPaths) {
        this.mappingId = Objects.requireNonNull(mappingId);
        this.sourceTableName = Objects.requireNonNull(sourceTableName);
        this.sourceTableJsonFieldName = Objects.requireNonNull(sourceTableJsonFieldName);
        this.sourceTableIdentityFields = Objects.requireNonNull(sourceTableIdentityFields);
        this.sourceTablePartitionFields = Objects.requireNonNull(sourceTablePartitionFields);
        this.targetTableName = Objects.requireNonNull(targetTableName);
        this.mappings = Objects.requireNonNull(mappings);
        this.mappingPaths = Objects.requireNonNull(mappingPaths);
    }

    @Override
    public String getMappingId() {
        return mappingId;
    }

    @Override
    public String getSourceTableName() {
        return sourceTableName;
    }

    @Override
    public String getSourceTableJsonFieldName() {
        return sourceTableJsonFieldName;
    }

    @Override
    public List<F> getSourceTableIdentityFields() {
        return sourceTableIdentityFields;
    }

    @Override
    public List<P> getSourceTablePartitionFields() {
        return sourceTablePartitionFields;
    }

    @Override
    public String getTargetTableName() {
        return targetTableName;
    }

    @Override
    public List<M> getMappings() {
        return mappings;
    }

    @Override
    public List<PathSegment> getMappingPaths() {
        return mappingPaths;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BaseContext)) {
            return false;
        }
        BaseContext<?, ?, ?, ?> that = (BaseContext<?, ?, ?, ?>) o;
        return getMappingId().equals(that.getMappingId())
                && getSourceTableName().equals(that.getSourceTableName())
                && getSourceTableJsonFieldName().equals(that.getSourceTableJsonFieldName())
                && getSourceTableIdentityFields().equals(that.getSourceTableIdentityFields())
                && Objects.equals(getSourceTablePartitionFields(), that.getSourceTablePartitionFields())
                && getTargetTableName().equals(that.getTargetTableName())
                && getMappings().equals(that.getMappings())
                && getMappingPaths().equals(that.getMappingPaths());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMappingId(),
                getSourceTableName(),
                getSourceTableJsonFieldName(),
                getSourceTableIdentityFields(),
                getSourceTablePartitionFields(),
                getTargetTableName(),
                getMappings(),
                getMappingPaths());
    }
}
