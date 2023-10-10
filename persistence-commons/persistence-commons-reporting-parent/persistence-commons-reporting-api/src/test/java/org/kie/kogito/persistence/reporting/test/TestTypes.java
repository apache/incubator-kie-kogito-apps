package org.kie.kogito.persistence.reporting.test;

import org.kie.kogito.persistence.reporting.bootstrap.BootstrapLoader;
import org.kie.kogito.persistence.reporting.database.DatabaseManager;
import org.kie.kogito.persistence.reporting.database.sqlbuilders.ApplyMappingSqlBuilder;
import org.kie.kogito.persistence.reporting.database.sqlbuilders.Context;
import org.kie.kogito.persistence.reporting.database.sqlbuilders.IndexesSqlBuilder;
import org.kie.kogito.persistence.reporting.database.sqlbuilders.TableSqlBuilder;
import org.kie.kogito.persistence.reporting.database.sqlbuilders.TriggerDeleteSqlBuilder;
import org.kie.kogito.persistence.reporting.database.sqlbuilders.TriggerInsertSqlBuilder;
import org.kie.kogito.persistence.reporting.model.Field;
import org.kie.kogito.persistence.reporting.model.JsonField;
import org.kie.kogito.persistence.reporting.model.Mapping;
import org.kie.kogito.persistence.reporting.model.MappingDefinition;
import org.kie.kogito.persistence.reporting.model.MappingDefinitions;
import org.kie.kogito.persistence.reporting.model.PartitionField;
import org.kie.kogito.persistence.reporting.service.MappingService;

/**
 * Collection of interfaces extends those in the -api to hide generics for tests.
 */
public interface TestTypes {

    interface TestField extends Field {
    }

    interface TestJsonField extends JsonField<Object> {
    }

    interface TestPartitionField extends PartitionField {
    }

    interface TestMapping extends Mapping<Object, TestJsonField> {
    }

    interface TestMappingDefinition extends MappingDefinition<Object, TestField, TestPartitionField, TestJsonField, TestMapping> {
    }

    interface TestMappingDefinitions extends MappingDefinitions<Object, TestField, TestPartitionField, TestJsonField, TestMapping, TestMappingDefinition> {
    }

    interface TestContext extends Context<Object, TestField, TestPartitionField, TestJsonField, TestMapping> {
    }

    interface TestBootstrapLoader extends BootstrapLoader<Object, TestField, TestPartitionField, TestJsonField, TestMapping, TestMappingDefinition, TestMappingDefinitions> {
    }

    interface TestDatabaseManager extends DatabaseManager<Object, TestField, TestPartitionField, TestJsonField, TestMapping, TestMappingDefinition, TestContext> {
    }

    interface TestMappingService extends MappingService<Object, TestField, TestPartitionField, TestJsonField, TestMapping, TestMappingDefinition> {
    }

    interface TestIndexesSqlBuilder extends IndexesSqlBuilder<Object, TestField, TestPartitionField, TestJsonField, TestMapping, TestContext> {
    }

    interface TestTableSqlBuilder extends TableSqlBuilder<Object, TestField, TestPartitionField, TestJsonField, TestMapping, TestContext> {
    }

    interface TestTriggerDeleteSqlBuilder extends TriggerDeleteSqlBuilder<Object, TestField, TestPartitionField, TestJsonField, TestMapping, TestContext> {
    }

    interface TestTriggerInsertSqlBuilder extends TriggerInsertSqlBuilder<Object, TestField, TestPartitionField, TestJsonField, TestMapping, TestContext> {
    }

    interface TestApplyMappingSqlBuilder extends ApplyMappingSqlBuilder<Object, TestField, TestPartitionField, TestJsonField, TestMapping, TestContext> {
    }
}
