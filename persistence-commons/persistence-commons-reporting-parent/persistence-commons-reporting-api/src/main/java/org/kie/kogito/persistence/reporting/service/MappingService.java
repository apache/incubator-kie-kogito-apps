package org.kie.kogito.persistence.reporting.service;

import java.util.List;

import org.kie.kogito.persistence.reporting.model.Field;
import org.kie.kogito.persistence.reporting.model.JsonField;
import org.kie.kogito.persistence.reporting.model.Mapping;
import org.kie.kogito.persistence.reporting.model.MappingDefinition;
import org.kie.kogito.persistence.reporting.model.PartitionField;

public interface MappingService<T, F extends Field, P extends PartitionField, J extends JsonField<T>, M extends Mapping<T, J>, D extends MappingDefinition<T, F, P, J, M>> {

    List<D> getAllMappingDefinitions();

    D getMappingDefinitionById(final String mappingId);

    void saveMappingDefinition(final D definition);

    D deleteMappingDefinitionById(final String mappingId);
}
