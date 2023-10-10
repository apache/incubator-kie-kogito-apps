package org.kie.kogito.jobs.service.resource;

import java.time.Duration;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;

@QuarkusIntegrationTest
class JobServiceManagementResourceIT {

    private static final String HEALTH_ENDPOINT = "/q/health/ready";
    public static final String MANAGEMENT_SHUTDOWN_ENDPOINT = "/management/shutdown";

    @Test
    public void testShutdown() {
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .get(HEALTH_ENDPOINT)
                .then()
                .statusCode(200);

        given()
                .when()
                .post(MANAGEMENT_SHUTDOWN_ENDPOINT)
                .then()
                .statusCode(200);

        Awaitility.await().atMost(Duration.ofSeconds(5)).untilAsserted(() -> given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .get(HEALTH_ENDPOINT)
                .then()
                .statusCode(503));
    }
}
