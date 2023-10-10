package org.kie.kogito.index;

import org.junit.jupiter.api.BeforeEach;
import org.kie.kogito.KogitoApplication;
import org.kie.kogito.index.spring.DataIndexMongoDBSpringTestResource;
import org.kie.kogito.index.spring.KogitoServiceRandomPortSpringTestResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import io.restassured.RestAssured;

import static org.kie.kogito.index.test.Constants.KOGITO_DATA_INDEX_SERVICE_URL;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = { KogitoApplication.class })
@ContextConfiguration(initializers = { KogitoServiceRandomPortSpringTestResource.class, DataIndexMongoDBSpringTestResource.class })
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class ProcessDataIndexMongoDBIT extends AbstractProcessDataIndexIT {

    @LocalServerPort
    private int httpPort;

    @Value("${" + KOGITO_DATA_INDEX_SERVICE_URL + "}")
    private String dataIndexUrl;

    @Override
    public String getDataIndexURL() {
        return dataIndexUrl;
    }

    @BeforeEach
    public void setup() {
        RestAssured.port = httpPort;
    }

}
