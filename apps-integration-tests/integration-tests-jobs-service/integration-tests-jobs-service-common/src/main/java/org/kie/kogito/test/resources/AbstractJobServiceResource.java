/*
 * Copyright 2022 Red Hat, Inc. and/or its affiliates.
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

package org.kie.kogito.test.resources;

import java.util.HashMap;
import java.util.Map;

import org.kie.kogito.testcontainers.JobServiceContainer;

public abstract class AbstractJobServiceResource implements TestResource {

    protected final JobServiceContainer jobService;
    protected final Map<String, String> properties = new HashMap<>();

    public AbstractJobServiceResource() {
        this(new JobServiceContainer());
    }

    public AbstractJobServiceResource(JobServiceContainer jobService) {
        this.jobService = jobService;
    }

    @Override
    public String getResourceName() {
        return jobService.getResourceName();
    }

    @Override
    public int getMappedPort() {
        return jobService.getMappedPort();
    }

    public Map<String, String> getProperties() {
        return properties;
    }
}
