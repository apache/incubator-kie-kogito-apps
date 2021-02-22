/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
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
package org.kie.kogito.resources;

import org.kie.kogito.testcontainers.JobServiceContainer;

/**
 * Infinispan spring boot resource that works within the test lifecycle.
 *
 */
public class JobServiceSpringBootTestResource extends ConditionalSpringBootTestResource {

    public static final String JOBS_SERVICE_URL = "kogito.jobs-service.url";

    public JobServiceSpringBootTestResource() {
        super(new JobServiceContainer());
    }

    @Override
    protected String getKogitoProperty() {
        return JOBS_SERVICE_URL;
    }

    @Override
    protected String getKogitoPropertyValue() {
        return "http://localhost:" + getTestResource().getMappedPort();
    }

    public static class Conditional extends JobServiceSpringBootTestResource {

        public Conditional() {
            super();
            enableConditional();
        }
    }
}
