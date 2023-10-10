package org.kie.kogito.it.jobs;

import org.junit.jupiter.api.BeforeEach;
import org.kie.kogito.KogitoApplication;
import org.kie.kogito.test.resources.JobServiceSpringBootTestResource;
import org.kie.kogito.test.resources.KogitoServiceRandomPortSpringBootTestResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import io.restassured.RestAssured;

import static org.kie.kogito.test.resources.JobServiceSpringBootTestResource.JOBS_SERVICE_URL;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = { KogitoApplication.class })
@ContextConfiguration(initializers = { KogitoServiceRandomPortSpringBootTestResource.class, JobServiceSpringBootTestResource.class })
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class ProcessAsyncIT extends BaseProcessAsyncIT {

    @Value("${server.port}")
    private int httpPort;

    @Value("${" + JOBS_SERVICE_URL + "}")
    private String jobServiceUrl;

    @BeforeEach
    public void setup() {
        RestAssured.port = httpPort;
        healthCheck();
    }

    @Override
    public String jobServiceUrl() {
        return jobServiceUrl;
    }
}
