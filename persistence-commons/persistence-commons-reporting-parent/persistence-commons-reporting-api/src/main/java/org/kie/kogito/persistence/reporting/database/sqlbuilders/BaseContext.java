package org.kie.kogito.persistence.reporting.database.sqlbuilders;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.kie.kogito.persistence.reporting.model.BaseMappingDefinition;
import org.kie.kogito.persistence.reporting.model.Field;
import org.kie.kogito.persistence.reporting.model.JsonField;
import org.kie.kogito.persistence.reporting.model.Mapping;
import org.kie.kogito.persistence.reporting.model.PartitionField;
import org.kie.kogito.persistence.reporting.model.paths.PathSegment;

public abstract class BaseContext<T, F extends Field, P extends PartitionField, J extends JsonField<T>, M extends Mapping<T, J>> extends BaseMappingDefinition<T, F, P, J, M>
        implements Context<T, F, P, J, M> {

    private final List<PathSegment> mappingPaths;
    private final Map<String, String> sourceTableFieldTypes;

    protected BaseContext(final String mappingId,
            final String sourceTableName,
            final String sourceTableJsonFieldName,
            final List<F> sourceTableIdentityFields,
            final List<P> sourceTablePartitionFields,
            final String targetTableName,
            final List<M> mappings,
            final List<PathSegment> mappingPaths,
            final Map<String, String> sourceTableFieldTypes) {
        super(mappingId,
                sourceTableName,
                sourceTableJsonFieldName,
                sourceTableIdentityFields,
                sourceTablePartitionFields,
                targetTableName,
                mappings);
        this.mappingPaths = Objects.requireNonNull(mappingPaths);
        this.sourceTableFieldTypes = Objects.requireNonNull(sourceTableFieldTypes);
    }

    @Override
    public List<PathSegment> getMappingPaths() {
        return mappingPaths;
    }

    @Override
    public Map<String, String> getSourceTableFieldTypes() {
        return sourceTableFieldTypes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BaseContext)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        BaseContext<?, ?, ?, ?, ?> that = (BaseContext<?, ?, ?, ?, ?>) o;
        return getMappingPaths().equals(that.getMappingPaths())
                && getSourceTableFieldTypes().equals(that.getSourceTableFieldTypes());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(),
                getMappingPaths(),
                getSourceTableFieldTypes());
    }
}
