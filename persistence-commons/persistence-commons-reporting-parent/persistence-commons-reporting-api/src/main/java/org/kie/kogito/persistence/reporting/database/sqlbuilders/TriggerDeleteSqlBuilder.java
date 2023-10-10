package org.kie.kogito.persistence.reporting.database.sqlbuilders;

import org.kie.kogito.persistence.reporting.model.Field;
import org.kie.kogito.persistence.reporting.model.JsonField;
import org.kie.kogito.persistence.reporting.model.Mapping;
import org.kie.kogito.persistence.reporting.model.PartitionField;

public interface TriggerDeleteSqlBuilder<T, F extends Field, P extends PartitionField, J extends JsonField<T>, M extends Mapping<T, J>, C extends Context<T, F, P, J, M>> {

    String createDeleteTriggerFunctionSql(final C context);

    String createDeleteTriggerSql(final C context);

    String dropDeleteTriggerFunctionSql(final C context);

    String dropDeleteTriggerSql(final C context);

}
