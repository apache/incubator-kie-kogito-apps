package org.kie.kogito.persistence.postgresql.reporting.database.sqlbuilders;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class PostgresTriggerInsertSqlBuilderTest extends BaseSqlBuilderImplTest {

    private final PostgresTriggerInsertSqlBuilder triggerInsertSqlBuilder = new PostgresTriggerInsertSqlBuilder();

    @Override
    protected PostgresTriggerInsertSqlBuilder getTriggerInsertBuilder() {
        return triggerInsertSqlBuilder;
    }

    @Override
    protected String getCreateSql(final PostgresContext context) {
        return getTriggerInsertBuilder().createInsertTriggerSql(context);
    }

    @Override
    protected String getDestroySql(final PostgresContext context) {
        return getTriggerInsertBuilder().dropInsertTriggerSql(context);
    }

    @Override
    protected void assertCreateSql(final String sql) {
        assertNotNull(sql);
        assertSequentialContent(sql,
                "CREATE TRIGGER trgInsert_mappingId AFTER INSERT OR UPDATE ON sourceTableName",
                "FOR EACH ROW",
                "EXECUTE PROCEDURE spInsert_mappingId()");
    }

    @Override
    protected void assertDestroySql(final String sql) {
        assertNotNull(sql);
        assertSequentialContent(sql,
                "DROP TRIGGER IF EXISTS trgInsert_mappingId ON sourceTableName");
    }

    @Test
    void testCreateInsertTriggerFunctionSql() {
        final PostgresContext context = manager.createContext(DEFINITION);

        final String sql = getTriggerInsertBuilder().createInsertTriggerFunctionSql(context);

        assertNotNull(sql);
        assertSequentialContent(sql,
                "CREATE FUNCTION spInsert_mappingId() RETURNS trigger A",
                "INSERT INTO targetTableName",
                "SELECT",
                "NEW.id",
                "(NEW.sourceTableJsonFieldName->>'root')\\:\\:text as field1",
                "(NEW.sourceTableJsonFieldName->'root'->>'child')\\:\\:text as field2",
                "(g0->>'child')\\:\\:text as field3",
                "(NEW.sourceTableJsonFieldName->'root'->'child'->>'sibling')\\:\\:text as field4",
                "FROM",
                "  jsonb_array_elements(",
                "case jsonb_typeof(NEW.sourceTableJsonFieldName->'collection')",
                "when 'array' then NEW.sourceTableJsonFieldName->'collection'",
                "else jsonb_build_array(NEW.sourceTableJsonFieldName->'collection')",
                "end",
                ") g0");
    }

    @Test
    void testDropInsertTriggerFunctionSql() {
        final PostgresContext context = manager.createContext(DEFINITION);

        final String sql = getTriggerInsertBuilder().dropInsertTriggerFunctionSql(context);

        assertNotNull(sql);
        assertSequentialContent(sql,
                "DROP FUNCTION IF EXISTS spInsert_mappingId");
    }
}
