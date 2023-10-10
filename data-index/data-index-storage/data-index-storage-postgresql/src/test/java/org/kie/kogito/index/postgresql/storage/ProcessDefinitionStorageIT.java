package org.kie.kogito.index.postgresql.storage;

import java.util.Set;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.kie.kogito.index.model.ProcessDefinition;
import org.kie.kogito.index.postgresql.model.ProcessDefinitionEntity;
import org.kie.kogito.index.postgresql.model.ProcessDefinitionEntityRepository;
import org.kie.kogito.index.test.TestUtils;
import org.kie.kogito.persistence.api.StorageService;
import org.kie.kogito.testcontainers.quarkus.PostgreSqlQuarkusTestResource;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@QuarkusTestResource(PostgreSqlQuarkusTestResource.class)
class ProcessDefinitionStorageIT extends AbstractStorageIT<ProcessDefinitionEntity, ProcessDefinition> {

    @Inject
    ProcessDefinitionEntityRepository repository;

    @Inject
    StorageService storage;

    public ProcessDefinitionStorageIT() {
        super(ProcessDefinition.class);
    }

    @Override
    public ProcessDefinitionEntityStorage.RepositoryAdapter getRepository() {
        return new ProcessDefinitionEntityStorage.RepositoryAdapter(repository);
    }

    @Override
    public StorageService getStorage() {
        return storage;
    }

    @Test
    @Transactional
    void testProcessInstanceEntity() {
        String processId = "travels";
        String version = "1.0";
        ProcessDefinition pdv1 = TestUtils.createProcessDefinition(processId, version, Set.of("admin", "kogito"));
        ProcessDefinition pdv2 = TestUtils.createProcessDefinition(processId, version, Set.of("kogito"));
        testStorage(pdv1.getKey(), pdv1, pdv2);
    }

}
