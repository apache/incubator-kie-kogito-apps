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
package org.kie.kogito.it.jobs;

import org.kie.kogito.test.resources.JobServiceTestResource;
import org.kie.kogito.testcontainers.quarkus.KafkaQuarkusTestResource;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusIntegrationTest;

import static org.kie.kogito.test.resources.JobServiceComposeQuarkusTestResource.JOBS_SERVICE_URL;

@QuarkusIntegrationTest
@QuarkusTestResource(KafkaQuarkusTestResource.class)
@JobServiceTestResource
class ProcessAsyncIT extends BaseProcessAsyncIT {

    @Override
    public String jobServiceUrl() {
        return System.getProperty(JOBS_SERVICE_URL);
    }
}
