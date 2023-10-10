package org.kie.kogito.index.storage;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.kie.kogito.index.model.Job;
import org.kie.kogito.index.model.ProcessDefinition;
import org.kie.kogito.index.model.ProcessInstance;
import org.kie.kogito.index.model.UserTaskInstance;
import org.kie.kogito.persistence.api.Storage;
import org.kie.kogito.persistence.api.StorageService;

import com.fasterxml.jackson.databind.node.ObjectNode;

import static org.kie.kogito.index.storage.Constants.JOBS_STORAGE;
import static org.kie.kogito.index.storage.Constants.PROCESS_DEFINITIONS_STORAGE;
import static org.kie.kogito.index.storage.Constants.PROCESS_ID_MODEL_STORAGE;
import static org.kie.kogito.index.storage.Constants.PROCESS_INSTANCES_STORAGE;
import static org.kie.kogito.index.storage.Constants.USER_TASK_INSTANCES_STORAGE;

@ApplicationScoped
public class DataIndexStorageServiceImpl implements DataIndexStorageService {

    @Inject
    StorageService cacheService;

    @Override
    public Storage<String, ProcessDefinition> getProcessDefinitionsCache() {
        return cacheService.getCache(PROCESS_DEFINITIONS_STORAGE, ProcessDefinition.class);
    }

    @Override
    public Storage<String, ProcessInstance> getProcessInstancesCache() {
        return cacheService.getCache(PROCESS_INSTANCES_STORAGE, ProcessInstance.class);
    }

    @Override
    public Storage<String, UserTaskInstance> getUserTaskInstancesCache() {
        return cacheService.getCache(USER_TASK_INSTANCES_STORAGE, UserTaskInstance.class);
    }

    @Override
    public Storage<String, Job> getJobsCache() {
        return cacheService.getCache(JOBS_STORAGE, Job.class);
    }

    @Override
    public Storage<String, ObjectNode> getDomainModelCache(String processId) {
        String rootType = getProcessIdModelCache().get(processId);
        return rootType == null ? null : cacheService.getCache(getDomainModelCacheName(processId), ObjectNode.class, rootType);
    }

    @Override
    public String getDomainModelCacheName(String processId) {
        return processId + "_domain";
    }

    @Override
    public Storage<String, String> getProcessIdModelCache() {
        return cacheService.getCache(PROCESS_ID_MODEL_STORAGE);
    }
}
