package org.kie.kogito.jobs.service.health;

import java.util.concurrent.TimeUnit;

import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;

public class HealthCheckUtils {

    public static final String HEALTH_ENDPOINT = "/q/health";
    private static final int OK = 200;

    private HealthCheckUtils() {
    }

    /**
     * Helper method that can be used along the tests to ensure jobs service ready health check passes before executing
     * other tests or invocations.
     */
    public static void awaitReadyHealthCheck(int timeout, TimeUnit timeUnit) {
        //health check - wait to be ready
        await()
                .atMost(1, MINUTES)
                .pollInterval(1, SECONDS)
                .untilAsserted(() -> given()
                        .contentType(ContentType.JSON)
                        .accept(ContentType.JSON)
                        .get(HEALTH_ENDPOINT)
                        .then()
                        .statusCode(OK));
    }
}
