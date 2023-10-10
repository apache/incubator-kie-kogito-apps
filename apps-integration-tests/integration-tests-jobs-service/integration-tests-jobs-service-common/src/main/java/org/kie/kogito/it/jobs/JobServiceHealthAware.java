package org.kie.kogito.it.jobs;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;

import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static org.awaitility.Awaitility.await;

public interface JobServiceHealthAware {

    @BeforeEach
    default void healthCheck() {
        //health check - wait to be ready
        await()
                .atMost(5, TimeUnit.MINUTES)
                .pollInterval(1, TimeUnit.SECONDS)
                .untilAsserted(() -> given()
                        .contentType(ContentType.JSON)
                        .accept(ContentType.JSON)
                        .baseUri(jobServiceUrl())
                        .get("/q/health")
                        .then()
                        .statusCode(200));
    }

    String jobServiceUrl();
}
