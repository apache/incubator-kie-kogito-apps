package org.kie.kogito.persistence.postgresql.reporting.database.sqlbuilders;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class PostgresTableSqlBuilderTest extends BaseSqlBuilderImplTest {

    private final PostgresTableSqlBuilder tableSqlBuilder = new PostgresTableSqlBuilder();

    @Override
    protected PostgresTableSqlBuilder getTableBuilder() {
        return tableSqlBuilder;
    }

    @Override
    protected String getCreateSql(final PostgresContext context) {
        return getTableBuilder().createTableSql(context);
    }

    @Override
    protected String getDestroySql(final PostgresContext context) {
        return getTableBuilder().dropTableSql(context);
    }

    @Override
    protected void assertCreateSql(final String sql) {
        assertNotNull(sql);
        assertSequentialContent(sql,
                "CREATE TABLE targetTableName",
                "id text",
                "field1 text",
                "field2 text",
                "field3 text",
                "field4 text");
    }

    @Override
    protected void assertDestroySql(final String sql) {
        assertNotNull(sql);
        assertSequentialContent(sql,
                "DROP TABLE IF EXISTS targetTableName");
    }
}
