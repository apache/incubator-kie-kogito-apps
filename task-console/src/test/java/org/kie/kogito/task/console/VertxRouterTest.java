package org.kie.kogito.task.console;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

import static io.restassured.RestAssured.given;

@QuarkusTest
class VertxRouterTest {

    @Test
    void testHandlePath() {

        given().when().get("/TaskInbox")
                .then()
                .statusCode(200);

        given().when().get("/TaskDetails/a1e139d5-4e77-48c9-84ae-34578e904e5a")
                .then()
                .statusCode(200);

        given().when().get("/Another")
                .then()
                .statusCode(404);
    }
}
