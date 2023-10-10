package org.kie.kogito.persistence.postgresql.reporting.database.sqlbuilders;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class PostgresTriggerDeleteSqlBuilderTest extends BaseSqlBuilderImplTest {

    private final PostgresTriggerDeleteSqlBuilder triggerDeleteSqlBuilder = new PostgresTriggerDeleteSqlBuilder();

    @Override
    protected PostgresTriggerDeleteSqlBuilder getTriggerDeleteBuilder() {
        return triggerDeleteSqlBuilder;
    }

    @Override
    protected String getCreateSql(final PostgresContext context) {
        return getTriggerDeleteBuilder().createDeleteTriggerSql(context);
    }

    @Override
    protected String getDestroySql(final PostgresContext context) {
        return getTriggerDeleteBuilder().dropDeleteTriggerSql(context);
    }

    @Override
    protected void assertCreateSql(final String sql) {
        assertNotNull(sql);
        assertSequentialContent(sql,
                "CREATE TRIGGER trgDelete_mappingId_DELETES AFTER DELETE ON sourceTableName",
                "FOR EACH ROW",
                "EXECUTE PROCEDURE spDelete_mappingId_DELETES()",
                "CREATE TRIGGER trgDelete_mappingId_UPDATES BEFORE UPDATE ON sourceTableName",
                "FOR EACH ROW",
                "EXECUTE PROCEDURE spDelete_mappingId_UPDATES()");
    }

    @Override
    protected void assertDestroySql(final String sql) {
        assertNotNull(sql);
        assertSequentialContent(sql,
                "DROP TRIGGER IF EXISTS trgDelete_mappingId_DELETES ON sourceTableName",
                "DROP TRIGGER IF EXISTS trgDelete_mappingId_UPDATES ON sourceTableName");
    }

    @Test
    void testCreateDeleteTriggerFunctionSql() {
        final PostgresContext context = manager.createContext(DEFINITION);

        final String sql = getTriggerDeleteBuilder().createDeleteTriggerFunctionSql(context);

        assertNotNull(sql);
        assertSequentialContent(sql,
                "CREATE FUNCTION spDelete_mappingId_DELETES() RETURNS trigger AS",
                "DELETE FROM targetTableName",
                "WHERE",
                "id = OLD.id",
                "RETURN OLD;",
                "CREATE FUNCTION spDelete_mappingId_UPDATES() RETURNS trigger AS",
                "DELETE FROM targetTableName",
                "WHERE",
                "id = NEW.id",
                "RETURN NEW;");
    }

    @Test
    void testDropDeleteTriggerFunctionSql() {
        final PostgresContext context = manager.createContext(DEFINITION);

        final String sql = getTriggerDeleteBuilder().dropDeleteTriggerFunctionSql(context);

        assertNotNull(sql);
        assertSequentialContent(sql,
                "DROP FUNCTION IF EXISTS spDelete_mappingId_DELETES",
                "DROP FUNCTION IF EXISTS spDelete_mappingId_UPDATES");
    }
}
