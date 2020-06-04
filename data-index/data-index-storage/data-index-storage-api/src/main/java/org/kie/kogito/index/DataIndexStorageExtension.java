package org.kie.kogito.index;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.kie.kogito.index.model.Job;
import org.kie.kogito.index.model.ProcessInstance;
import org.kie.kogito.index.model.UserTaskInstance;
import org.kie.kogito.storage.api.Cache;
import org.kie.kogito.storage.api.CacheService;

public class DataIndexStorageExtension implements IDataIndexStorageExtension {

    private static final String PROCESS_INSTANCES_CACHE = "processinstances";
    private static final String USER_TASK_INSTANCES_CACHE = "usertaskinstances";
    private static final String JOBS_CACHE = "jobs";
    private static final String PROCESS_ID_MODEL_CACHE = "processidmodel";

    @Inject
    CacheService cacheService;

    @Override
    public Cache<String, ProcessInstance> getProcessInstancesCache() {
        return cacheService.getCache(PROCESS_INSTANCES_CACHE, ProcessInstance.class);
    }

    @Override
    public Cache<String, UserTaskInstance> getUserTaskInstancesCache() {
        return  cacheService.getCache(USER_TASK_INSTANCES_CACHE, UserTaskInstance.class);
    }

    @Override
    public Cache<String, Job> getJobsCache() {
        return cacheService.getCache(JOBS_CACHE, Job.class);
    }

    @Override
    public Cache<String, ObjectNode> getDomainModelCache(String processId) {
        return cacheService.getDomainModelCache(processId);
    }

    @Override
    public Cache<String, String> getProcessIdModelCache() {
        return cacheService.getProcessIdModelCache();
    }
}
