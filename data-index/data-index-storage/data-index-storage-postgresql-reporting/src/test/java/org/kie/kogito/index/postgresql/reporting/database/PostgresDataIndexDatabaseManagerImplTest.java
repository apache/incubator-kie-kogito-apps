package org.kie.kogito.index.postgresql.reporting.database;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kie.kogito.index.postgresql.model.JobEntityRepository;
import org.kie.kogito.index.postgresql.model.ProcessInstanceEntityRepository;
import org.kie.kogito.index.postgresql.model.UserTaskInstanceEntityRepository;
import org.kie.kogito.persistence.postgresql.reporting.database.sqlbuilders.PostgresApplyMappingSqlBuilder;
import org.kie.kogito.persistence.postgresql.reporting.database.sqlbuilders.PostgresIndexesSqlBuilder;
import org.kie.kogito.persistence.postgresql.reporting.database.sqlbuilders.PostgresTableSqlBuilder;
import org.kie.kogito.persistence.postgresql.reporting.database.sqlbuilders.PostgresTriggerDeleteSqlBuilder;
import org.kie.kogito.persistence.postgresql.reporting.database.sqlbuilders.PostgresTriggerInsertSqlBuilder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostgresDataIndexDatabaseManagerImplTest {

    @Mock
    private ProcessInstanceEntityRepository processInstanceEntityRepository;

    @Mock
    private UserTaskInstanceEntityRepository userTaskInstanceEntityRepository;

    @Mock
    private JobEntityRepository jobEntityRepository;

    @Mock
    private EntityManager entityManager;

    private PostgresDataIndexDatabaseManagerImpl manager;

    @BeforeEach
    void setup() {
        manager = new PostgresDataIndexDatabaseManagerImpl(processInstanceEntityRepository,
                userTaskInstanceEntityRepository,
                jobEntityRepository,
                new PostgresIndexesSqlBuilder(),
                new PostgresTableSqlBuilder(),
                new PostgresTriggerDeleteSqlBuilder(),
                new PostgresTriggerInsertSqlBuilder(),
                new PostgresApplyMappingSqlBuilder());
    }

    @Test
    void testGetEntityManager_Processes() {
        when(processInstanceEntityRepository.getEntityManager()).thenReturn(entityManager);
        assertEquals(entityManager,
                manager.getEntityManager("processes"));
    }

    @Test
    void testGetEntityManager_UserTasks() {
        when(userTaskInstanceEntityRepository.getEntityManager()).thenReturn(entityManager);
        assertEquals(entityManager,
                manager.getEntityManager("tasks"));
    }

    @Test
    void testGetEntityManager_Jobs() {
        when(jobEntityRepository.getEntityManager()).thenReturn(entityManager);
        assertEquals(entityManager,
                manager.getEntityManager("jobs"));
    }

    @Test
    void testGetEntityManager_Unknown() {
        assertThrows(IllegalArgumentException.class,
                () -> manager.getEntityManager("unknown"));
    }

}
