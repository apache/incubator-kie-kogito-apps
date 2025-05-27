/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.kie.kogito.jobs.service.executor;

import org.kie.kogito.jobs.service.model.JobDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

@ApplicationScoped
public class DefaultJobExecutorResolver implements JobExecutorResolver {

    private static Logger LOGGER = LoggerFactory.getLogger(DefaultJobExecutorResolver.class);

    private Instance<JobExecutor> executors;

    @Inject
    public DefaultJobExecutorResolver(Instance<JobExecutor> executors) {
        this.executors = executors;
        for (JobExecutor registeredJobExecutor : executors) {
            LOGGER.info("Registering job executor: {}", registeredJobExecutor.getClass().getName());
        }
        LOGGER.info("Number of job executors registered: {}", executors.stream().count());
    }

    @Override
    public JobExecutor get(JobDetails jobDetails) {
        return executors.stream()
                .filter(executor -> executor.accept(jobDetails))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No JobExecutor found for " + jobDetails));
    }
}
