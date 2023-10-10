package org.kie.kogito.persistence.reporting.bootstrap;

import java.util.Optional;

import org.kie.kogito.persistence.reporting.model.Field;
import org.kie.kogito.persistence.reporting.model.JsonField;
import org.kie.kogito.persistence.reporting.model.Mapping;
import org.kie.kogito.persistence.reporting.model.MappingDefinition;
import org.kie.kogito.persistence.reporting.model.MappingDefinitions;
import org.kie.kogito.persistence.reporting.model.PartitionField;

public interface BootstrapLoader<T, F extends Field, P extends PartitionField, J extends JsonField<T>, M extends Mapping<T, J>, D extends MappingDefinition<T, F, P, J, M>, S extends MappingDefinitions<T, F, P, J, M, D>> {

    /**
     * Loads the Mapping Definitions present at start-up in "bootstrap.json".
     *
     * @return Mapping Definitions or empty.
     */
    Optional<S> load();

    /**
     * Returns the type of mapping definitions.
     * 
     * @return
     */
    Class<S> getMappingDefinitionsType();
}
