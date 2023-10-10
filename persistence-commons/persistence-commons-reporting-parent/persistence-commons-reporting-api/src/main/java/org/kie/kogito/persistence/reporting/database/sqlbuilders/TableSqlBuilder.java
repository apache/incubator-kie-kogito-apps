package org.kie.kogito.persistence.reporting.database.sqlbuilders;

import org.kie.kogito.persistence.reporting.model.Field;
import org.kie.kogito.persistence.reporting.model.JsonField;
import org.kie.kogito.persistence.reporting.model.Mapping;
import org.kie.kogito.persistence.reporting.model.PartitionField;

public interface TableSqlBuilder<T, F extends Field, P extends PartitionField, J extends JsonField<T>, M extends Mapping<T, J>, C extends Context<T, F, P, J, M>> {

    String createTableSql(final C context);

    String dropTableSql(final C context);
}
