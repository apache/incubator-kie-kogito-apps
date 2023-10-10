package org.kie.kogito.persistence.reporting.database.sqlbuilders;

import java.util.List;
import java.util.Map;

import org.kie.kogito.persistence.reporting.model.Field;
import org.kie.kogito.persistence.reporting.model.JsonField;
import org.kie.kogito.persistence.reporting.model.Mapping;
import org.kie.kogito.persistence.reporting.model.MappingDefinition;
import org.kie.kogito.persistence.reporting.model.PartitionField;
import org.kie.kogito.persistence.reporting.model.paths.PathSegment;

public interface Context<T, F extends Field, P extends PartitionField, J extends JsonField<T>, M extends Mapping<T, J>> extends MappingDefinition<T, F, P, J, M> {

    List<PathSegment> getMappingPaths();

    Map<String, String> getSourceTableFieldTypes();
}
