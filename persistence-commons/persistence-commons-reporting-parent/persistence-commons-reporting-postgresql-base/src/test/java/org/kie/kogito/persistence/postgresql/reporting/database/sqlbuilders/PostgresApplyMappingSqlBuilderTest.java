package org.kie.kogito.persistence.postgresql.reporting.database.sqlbuilders;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class PostgresApplyMappingSqlBuilderTest extends BaseSqlBuilderImplTest {

    private final PostgresApplyMappingSqlBuilder applyMappingSqlBuilder = new PostgresApplyMappingSqlBuilder();

    @Override
    protected PostgresApplyMappingSqlBuilder getApplyMappingSqlBuilder() {
        return applyMappingSqlBuilder;
    }

    @Override
    protected String getCreateSql(final PostgresContext context) {
        return getApplyMappingSqlBuilder().apply(context);
    }

    @Override
    protected void assertCreateSql(final String sql) {
        assertNotNull(sql);
        assertSequentialContent(sql,
                "UPDATE sourceTableName ",
                "SET id = id, ",
                "key = key ",
                "WHERE ",
                "partition = 'chunk' AND ",
                "partition2 = 'chunk2");
    }

    @Override
    @Disabled("There is no semantic equivalent for this SqlBuilder.")
    void testDestroy() {
    }

    @Override
    protected String getDestroySql(final PostgresContext context) {
        return null;
    }

    @Override
    protected void assertDestroySql(final String sql) {
    }

}
