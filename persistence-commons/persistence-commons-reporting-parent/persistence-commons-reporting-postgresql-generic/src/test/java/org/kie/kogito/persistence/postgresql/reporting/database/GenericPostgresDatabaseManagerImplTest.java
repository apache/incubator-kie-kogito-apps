package org.kie.kogito.persistence.postgresql.reporting.database;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kie.kogito.persistence.postgresql.model.CacheEntityRepository;
import org.kie.kogito.persistence.postgresql.reporting.database.sqlbuilders.PostgresApplyMappingSqlBuilder;
import org.kie.kogito.persistence.postgresql.reporting.database.sqlbuilders.PostgresIndexesSqlBuilder;
import org.kie.kogito.persistence.postgresql.reporting.database.sqlbuilders.PostgresTableSqlBuilder;
import org.kie.kogito.persistence.postgresql.reporting.database.sqlbuilders.PostgresTriggerDeleteSqlBuilder;
import org.kie.kogito.persistence.postgresql.reporting.database.sqlbuilders.PostgresTriggerInsertSqlBuilder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GenericPostgresDatabaseManagerImplTest {

    @Mock
    private CacheEntityRepository repository;

    @Mock
    private EntityManager entityManager;

    @Mock
    private PostgresIndexesSqlBuilder indexesSqlBuilder;

    @Mock
    private PostgresTableSqlBuilder tableSqlBuilder;

    @Mock
    private PostgresTriggerDeleteSqlBuilder triggerDeleteSqlBuilder;

    @Mock
    private PostgresTriggerInsertSqlBuilder triggerInsertSqlBuilder;

    @Mock
    private PostgresApplyMappingSqlBuilder applyMappingSqlBuilder;

    private GenericPostgresDatabaseManagerImpl manager;

    @BeforeEach
    public void setup() {
        this.manager = new GenericPostgresDatabaseManagerImpl(repository,
                indexesSqlBuilder,
                tableSqlBuilder,
                triggerDeleteSqlBuilder,
                triggerInsertSqlBuilder,
                applyMappingSqlBuilder);
    }

    @Test
    void testGetEntityManager() {
        when(repository.getEntityManager()).thenReturn(entityManager);

        assertEquals(entityManager,
                manager.getEntityManager("does-not-matter"));
    }

}
