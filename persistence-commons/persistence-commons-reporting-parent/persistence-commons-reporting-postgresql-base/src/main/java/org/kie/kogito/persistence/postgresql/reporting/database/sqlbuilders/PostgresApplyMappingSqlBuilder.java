package org.kie.kogito.persistence.postgresql.reporting.database.sqlbuilders;

import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;

import org.kie.kogito.persistence.postgresql.reporting.model.JsonType;
import org.kie.kogito.persistence.postgresql.reporting.model.PostgresField;
import org.kie.kogito.persistence.postgresql.reporting.model.PostgresJsonField;
import org.kie.kogito.persistence.postgresql.reporting.model.PostgresMapping;
import org.kie.kogito.persistence.postgresql.reporting.model.PostgresPartitionField;
import org.kie.kogito.persistence.reporting.database.sqlbuilders.ApplyMappingSqlBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class PostgresApplyMappingSqlBuilder implements ApplyMappingSqlBuilder<JsonType, PostgresField, PostgresPartitionField, PostgresJsonField, PostgresMapping, PostgresContext> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostgresApplyMappingSqlBuilder.class);

    private static final String UPDATE_TABLE_TEMPLATE = "UPDATE %s %n" +
            "SET %s %n" +
            "WHERE %n" +
            "%s;";

    @Override
    public String apply(final PostgresContext context) {
        final String sourceTableName = context.getSourceTableName();
        final List<PostgresField> identityFields = context.getSourceTableIdentityFields();
        final List<PostgresPartitionField> partitionFields = context.getSourceTablePartitionFields();

        final String sql = String.format(UPDATE_TABLE_TEMPLATE,
                sourceTableName,
                identityFields
                        .stream()
                        .map(PostgresApplyMappingSqlBuilder::buildIdentityFieldSql)
                        .collect(Collectors.joining(", " + String.format("%n"))),
                partitionFields
                        .stream()
                        .map(PostgresApplyMappingSqlBuilder::buildPartitionFieldSql)
                        .collect(Collectors.joining(" AND " + String.format("%n"))));

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(String.format("Apply Mapping SQL:%n%s", sql));
        }

        return sql;
    }

    private static String buildIdentityFieldSql(final PostgresField identifyField) {
        return String.format("%s = %s",
                identifyField.getFieldName(),
                identifyField.getFieldName());
    }

    private static String buildPartitionFieldSql(final PostgresPartitionField partitionField) {
        return String.format("%s = '%s'",
                partitionField.getFieldName(),
                partitionField.getFieldValue());
    }
}
