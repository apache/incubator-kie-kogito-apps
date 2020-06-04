package org.kie.kogito.index;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.kie.kogito.index.model.Job;
import org.kie.kogito.index.model.ProcessInstance;
import org.kie.kogito.index.model.UserTaskInstance;
import org.kie.kogito.storage.api.Cache;

public interface IDataIndexStorageExtension {

    Cache<String, String> getProtobufCache();

    Cache<String, ProcessInstance> getProcessInstancesCache();

    Cache<String, UserTaskInstance> getUserTaskInstancesCache();

    Cache<String, Job> getJobsCache();

    Cache<String, ObjectNode> getDomainModelCache(String processId);

    Cache<String, String> getProcessIdModelCache();
}
