/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.kogito.runtime.tools.quarkus.extension.runtime;

import java.util.function.Supplier;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;

import org.kie.kogito.runtime.tools.quarkus.extension.runtime.dataindex.DataIndexService;
import org.kie.kogito.runtime.tools.quarkus.extension.runtime.dataindex.jobs.JobsInstancesAdapter;
import org.kie.kogito.runtime.tools.quarkus.extension.runtime.dataindex.jobs.JobsResponse;
import org.kie.kogito.runtime.tools.quarkus.extension.runtime.dataindex.processes.ProcessInstancesAdapter;
import org.kie.kogito.runtime.tools.quarkus.extension.runtime.dataindex.processes.ProcessesResponse;
import org.kie.kogito.runtime.tools.quarkus.extension.runtime.dataindex.tasks.TasksResponse;
import org.kie.kogito.runtime.tools.quarkus.extension.runtime.dataindex.tasks.UserTaskInstancesAdapter;

public class KogitoSupplier implements Supplier<EntitiesCounters> {

    public static final String ALL_TASKS_IDS_QUERY = "{ \"operationName\": \"getAllTasksIds\", \"query\": \"query getAllTasksIds{  UserTaskInstances{ id } }\" }";
    public static final String ALL_PROCESSES_IDS_QUERY = "{ \"operationName\": \"getAllProcessesIds\", \"query\": \"query getAllProcessesIds{  ProcessInstances{ id } }\" }";
    public static final String ALL_JOBS_IDS_QUERY = "{ \"operationName\": \"getAllJobsIds\", \"query\": \"query getAllJobsIds{  Jobs{ id } }\" }";

    @Override
    public EntitiesCounters get() {
        /*
         * DataIndexService dataIndexService = Arc.container().instance(DataIndexService.class, RestClient.LITERAL).get();
         * 
         * EntitiesCounters entitiesCounters = new EntitiesCounters(getTasksCount(dataIndexService),
         * getProcessesCount(dataIndexService),
         * getJobsCount(dataIndexService));
         * return entitiesCounters;
         */
        return new EntitiesCounters(0, 0, 0);
    }

    private int getTasksCount(final DataIndexService dataIndexService) {
        final String tasksQueryResponse = dataIndexService.query(ALL_TASKS_IDS_QUERY);

        Jsonb jsonb = JsonbBuilder.create(new JsonbConfig().withAdapters(new UserTaskInstancesAdapter()));
        TasksResponse tasksResponse = jsonb.fromJson(tasksQueryResponse,
                TasksResponse.class);

        return tasksResponse.getData().getUserTaskInstances().size();
    }

    private int getProcessesCount(final DataIndexService dataIndexService) {
        final String processesQueryResponse = dataIndexService.query(ALL_PROCESSES_IDS_QUERY);

        Jsonb jsonb = JsonbBuilder.create(new JsonbConfig().withAdapters(new ProcessInstancesAdapter()));
        ProcessesResponse processesResponse = jsonb.fromJson(processesQueryResponse,
                ProcessesResponse.class);

        return processesResponse.getData().getProcessInstances().size();
    }

    private int getJobsCount(final DataIndexService dataIndexService) {
        final String jobsQueryResponse = dataIndexService.query(ALL_JOBS_IDS_QUERY);

        Jsonb jsonb = JsonbBuilder.create(new JsonbConfig().withAdapters(new JobsInstancesAdapter()));
        JobsResponse jobsResponse = jsonb.fromJson(jobsQueryResponse,
                JobsResponse.class);

        return jobsResponse.getData().getJobs().size();
    }
}
