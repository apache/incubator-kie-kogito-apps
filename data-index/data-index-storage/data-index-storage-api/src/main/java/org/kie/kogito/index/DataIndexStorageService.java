package org.kie.kogito.index;

import java.util.Map;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.kie.kogito.index.model.Job;
import org.kie.kogito.index.model.ProcessInstance;
import org.kie.kogito.index.model.UserTaskInstance;
import org.kie.kogito.storage.api.Storage;

public interface DataIndexStorageService {

    Map<String, String> getProtobufCache();

    Storage<String, ProcessInstance> getProcessInstancesCache();

    Storage<String, UserTaskInstance> getUserTaskInstancesCache();

    Storage<String, Job> getJobsCache();

    Storage<String, ObjectNode> getDomainModelCache(String processId);

    Storage<String, String> getProcessIdModelCache();
}
