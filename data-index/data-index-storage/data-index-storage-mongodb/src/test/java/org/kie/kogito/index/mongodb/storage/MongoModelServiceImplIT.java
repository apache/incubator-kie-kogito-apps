package org.kie.kogito.index.mongodb.storage;

import javax.inject.Inject;

import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.kogito.index.model.Job;
import org.kie.kogito.index.model.ProcessDefinition;
import org.kie.kogito.index.model.ProcessInstance;
import org.kie.kogito.index.model.UserTaskInstance;
import org.kie.kogito.index.mongodb.mock.MockIndexCreateOrUpdateEventListener;
import org.kie.kogito.index.mongodb.model.DomainEntityMapper;
import org.kie.kogito.index.mongodb.model.JobEntity;
import org.kie.kogito.index.mongodb.model.JobEntityMapper;
import org.kie.kogito.index.mongodb.model.ProcessDefinitionEntity;
import org.kie.kogito.index.mongodb.model.ProcessDefinitionEntityMapper;
import org.kie.kogito.index.mongodb.model.ProcessIdEntity;
import org.kie.kogito.index.mongodb.model.ProcessIdEntityMapper;
import org.kie.kogito.index.mongodb.model.ProcessInstanceEntity;
import org.kie.kogito.index.mongodb.model.ProcessInstanceEntityMapper;
import org.kie.kogito.index.mongodb.model.UserTaskInstanceEntity;
import org.kie.kogito.index.mongodb.model.UserTaskInstanceEntityMapper;
import org.kie.kogito.testcontainers.quarkus.MongoDBQuarkusTestResource;

import com.fasterxml.jackson.databind.node.ObjectNode;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.kie.kogito.index.storage.Constants.JOBS_STORAGE;
import static org.kie.kogito.index.storage.Constants.PROCESS_DEFINITIONS_STORAGE;
import static org.kie.kogito.index.storage.Constants.PROCESS_ID_MODEL_STORAGE;
import static org.kie.kogito.index.storage.Constants.PROCESS_INSTANCES_STORAGE;
import static org.kie.kogito.index.storage.Constants.USER_TASK_INSTANCES_STORAGE;

@QuarkusTest
@QuarkusTestResource(MongoDBQuarkusTestResource.class)
class MongoModelServiceImplIT {

    @Inject
    MockIndexCreateOrUpdateEventListener mockIndexCreateOrUpdateEventListener;

    @Inject
    MongoModelServiceImpl mongoModelServiceImpl;

    @BeforeEach
    void setup() {
        // Make sure MongoModelServiceImpl is initialized
        assertNotNull(mongoModelServiceImpl.getEntityMapper(PROCESS_INSTANCES_STORAGE));
        mockIndexCreateOrUpdateEventListener.reset();
    }

    @AfterEach
    void tearDown() {
        mockIndexCreateOrUpdateEventListener.reset();
    }

    @Test
    void testInit() {
        mongoModelServiceImpl.init();

        mockIndexCreateOrUpdateEventListener.assertFire(PROCESS_DEFINITIONS_STORAGE, ProcessDefinition.class.getName());
        mockIndexCreateOrUpdateEventListener.assertFire(PROCESS_INSTANCES_STORAGE, ProcessInstance.class.getName());
        mockIndexCreateOrUpdateEventListener.assertFire(USER_TASK_INSTANCES_STORAGE, UserTaskInstance.class.getName());
        mockIndexCreateOrUpdateEventListener.assertFire(JOBS_STORAGE, Job.class.getName());
    }

    @Test
    void testGetEntityMapper() {
        assertTrue(mongoModelServiceImpl.<Job, JobEntity> getEntityMapper(JOBS_STORAGE) instanceof JobEntityMapper);
        assertTrue(mongoModelServiceImpl.<ProcessDefinition, ProcessDefinitionEntity> getEntityMapper(PROCESS_DEFINITIONS_STORAGE) instanceof ProcessDefinitionEntityMapper);
        assertTrue(mongoModelServiceImpl.<ProcessInstance, ProcessInstanceEntity> getEntityMapper(PROCESS_INSTANCES_STORAGE) instanceof ProcessInstanceEntityMapper);
        assertTrue(mongoModelServiceImpl.<UserTaskInstance, UserTaskInstanceEntity> getEntityMapper(USER_TASK_INSTANCES_STORAGE) instanceof UserTaskInstanceEntityMapper);
        assertTrue(mongoModelServiceImpl.<String, ProcessIdEntity> getEntityMapper(PROCESS_ID_MODEL_STORAGE) instanceof ProcessIdEntityMapper);
        assertTrue(mongoModelServiceImpl.<ObjectNode, Document> getEntityMapper("test_domain") instanceof DomainEntityMapper);
    }
}
