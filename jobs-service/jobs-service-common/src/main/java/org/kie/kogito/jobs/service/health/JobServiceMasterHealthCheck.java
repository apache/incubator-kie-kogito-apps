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

package org.kie.kogito.jobs.service.health;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Readiness;
import org.kie.kogito.jobs.service.runtime.JobServiceInstanceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Readiness
@ApplicationScoped
public class JobServiceMasterHealthCheck implements HealthCheck {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobServiceMasterHealthCheck.class);

    @Inject
    private JobServiceInstanceManager manager;

    @Override
    public HealthCheckResponse call() {
        final HealthCheckResponseBuilder responseBuilder = HealthCheckResponse.named("Master Instance");
        if (manager.isMaster()) {
            return responseBuilder.up().build();
        }
        return responseBuilder.down().build();
    }
}
