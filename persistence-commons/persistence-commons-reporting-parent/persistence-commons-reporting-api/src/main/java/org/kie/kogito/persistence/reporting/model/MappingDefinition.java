package org.kie.kogito.persistence.reporting.model;

import java.util.List;

public interface MappingDefinition<T, F extends Field, P extends PartitionField, J extends JsonField<T>, M extends Mapping<T, J>> {

    String getMappingId();

    String getSourceTableName();

    String getSourceTableJsonFieldName();

    List<F> getSourceTableIdentityFields();

    List<P> getSourceTablePartitionFields();

    String getTargetTableName();

    List<M> getFieldMappings();
}
