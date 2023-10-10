package org.kie.kogito.test.resources;

import java.util.Map;

import org.kie.kogito.testcontainers.JobServiceContainer;

import static java.util.Collections.singletonMap;

/**
 * Infinispan spring boot resource that works within the test lifecycle.
 *
 */
public class JobServiceSpringBootTestResource extends ConditionalSpringBootTestResource<JobServiceContainer> {

    public static final String JOBS_SERVICE_URL = "kogito.jobs-service.url";

    public JobServiceSpringBootTestResource() {
        super(new JobServiceContainer());
    }

    @Override
    protected Map<String, String> getProperties() {
        return singletonMap(JOBS_SERVICE_URL, "http://localhost:" + getTestResource().getMappedPort());
    }

    public static class Conditional extends JobServiceSpringBootTestResource {

        public Conditional() {
            super();
            enableConditional();
        }
    }
}
