package org.kie.kogito.persistence.reporting.database.sqlbuilders;

import org.kie.kogito.persistence.reporting.model.Field;
import org.kie.kogito.persistence.reporting.model.JsonField;
import org.kie.kogito.persistence.reporting.model.Mapping;
import org.kie.kogito.persistence.reporting.model.PartitionField;

public interface TriggerInsertSqlBuilder<T, F extends Field, P extends PartitionField, J extends JsonField<T>, M extends Mapping<T, J>, C extends Context<T, F, P, J, M>> {

    String createInsertTriggerFunctionSql(final C context);

    String createInsertTriggerSql(final C context);

    String dropInsertTriggerFunctionSql(final C context);

    String dropInsertTriggerSql(final C context);
}
