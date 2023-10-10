package org.kie.kogito.persistence.mongodb;

import org.junit.jupiter.api.Test;
import org.kie.kogito.testcontainers.quarkus.MongoDBQuarkusTestResource;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
@QuarkusTestResource(MongoDBQuarkusTestResource.class)
class HealthCheckIT {

    @Test
    void testHealthCheck() {
        String status = "UP";
        String connection = "MongoDB connection health check";

        given()
                .when().get("/q/health/ready")
                .then()
                .statusCode(200)
                .body("status", equalTo(status))
                .body("checks[0].name", equalTo(connection))
                .body("checks[0].status", equalTo(status));
    }
}
