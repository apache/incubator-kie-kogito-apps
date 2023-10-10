package org.kie.kogito.index.storage;

import org.kie.kogito.index.model.Job;
import org.kie.kogito.index.model.ProcessDefinition;
import org.kie.kogito.index.model.ProcessInstance;
import org.kie.kogito.index.model.UserTaskInstance;
import org.kie.kogito.persistence.api.Storage;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface DataIndexStorageService {

    Storage<String, ProcessDefinition> getProcessDefinitionsCache();

    Storage<String, ProcessInstance> getProcessInstancesCache();

    Storage<String, UserTaskInstance> getUserTaskInstancesCache();

    Storage<String, Job> getJobsCache();

    Storage<String, ObjectNode> getDomainModelCache(String processId);

    String getDomainModelCacheName(String processId);

    Storage<String, String> getProcessIdModelCache();
}
