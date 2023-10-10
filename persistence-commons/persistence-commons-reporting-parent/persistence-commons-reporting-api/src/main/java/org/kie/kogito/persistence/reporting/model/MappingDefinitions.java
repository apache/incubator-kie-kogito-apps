package org.kie.kogito.persistence.reporting.model;

import java.util.Collection;

public interface MappingDefinitions<T, F extends Field, P extends PartitionField, J extends JsonField<T>, M extends Mapping<T, J>, D extends MappingDefinition<T, F, P, J, M>> {

    Collection<D> getMappingDefinitions();
}
