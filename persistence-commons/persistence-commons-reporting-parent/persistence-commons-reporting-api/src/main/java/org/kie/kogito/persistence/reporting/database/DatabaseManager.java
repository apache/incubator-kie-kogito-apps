package org.kie.kogito.persistence.reporting.database;

import org.kie.kogito.persistence.reporting.database.sqlbuilders.Context;
import org.kie.kogito.persistence.reporting.model.Field;
import org.kie.kogito.persistence.reporting.model.JsonField;
import org.kie.kogito.persistence.reporting.model.Mapping;
import org.kie.kogito.persistence.reporting.model.MappingDefinition;
import org.kie.kogito.persistence.reporting.model.PartitionField;

public interface DatabaseManager<T, F extends Field, P extends PartitionField, J extends JsonField<T>, M extends Mapping<T, J>, D extends MappingDefinition<T, F, P, J, M>, C extends Context<T, F, P, J, M>> {

    C createContext(final D mappingDefinition);

    /**
     * Creates the database artifacts for a Mapping Definition.
     *
     * @param mappingDefinition The Mapping Definition for which database artifacts need to be created.
     */
    void createArtifacts(final D mappingDefinition);

    /**
     * Destroys the database artifacts for a Mapping Definition.
     *
     * @param mappingDefinition The Mapping Definition for which database artifacts need to be destroyed.
     */
    void destroyArtifacts(final D mappingDefinition);
}
