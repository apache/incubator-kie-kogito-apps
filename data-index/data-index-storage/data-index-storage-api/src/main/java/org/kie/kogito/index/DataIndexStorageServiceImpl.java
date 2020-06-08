package org.kie.kogito.index;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.kie.kogito.index.model.Job;
import org.kie.kogito.index.model.ProcessInstance;
import org.kie.kogito.index.model.UserTaskInstance;
import org.kie.kogito.storage.api.Storage;
import org.kie.kogito.storage.api.StorageService;

@ApplicationScoped
public class DataIndexStorageServiceImpl implements DataIndexStorageService {

    private static final String PROCESS_INSTANCES_CACHE = "processinstances";
    private static final String USER_TASK_INSTANCES_CACHE = "usertaskinstances";
    private static final String JOBS_CACHE = "jobs";
    private static final String PROCESS_ID_MODEL_CACHE = "processidmodel";

    @Inject
    StorageService cacheService;

    @Override
    public Storage<String, String> getProtobufCache() {
        return cacheService.getProtobufCache();
    }

    @Override
    public Storage<String, ProcessInstance> getProcessInstancesCache() {
        return cacheService.getCache(PROCESS_INSTANCES_CACHE, ProcessInstance.class);
    }

    @Override
    public Storage<String, UserTaskInstance> getUserTaskInstancesCache() {
        return  cacheService.getCache(USER_TASK_INSTANCES_CACHE, UserTaskInstance.class);
    }

    @Override
    public Storage<String, Job> getJobsCache() {
        return cacheService.getCache(JOBS_CACHE, Job.class);
    }

    @Override
    public Storage<String, ObjectNode> getDomainModelCache(String processId) {
        return cacheService.getDomainModelCache(PROCESS_ID_MODEL_CACHE, processId);
    }

    @Override
    public Storage<String, String> getProcessIdModelCache() {
        return cacheService.getModelCacheByType(PROCESS_ID_MODEL_CACHE);
    }
}
