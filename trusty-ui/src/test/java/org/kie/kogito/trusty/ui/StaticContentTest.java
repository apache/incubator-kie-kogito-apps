package org.kie.kogito.trusty.ui;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.vertx.core.http.HttpHeaders;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

@QuarkusTest
public class StaticContentTest {

    @Test
    public void testIndexHtml() {
        given().contentType(ContentType.JSON).when().get("/").then()
                .statusCode(200)
                .body(containsString("<title>Kogito - TrustyAI</title>"));
    }

    @Test
    public void testHeaders() {
        given().contentType(ContentType.JSON).when().get("/").then()
                .statusCode(200)
                .header(HttpHeaders.CACHE_CONTROL.toString(), "no-cache")
                .header(HttpHeaders.CONTENT_TYPE.toString(), "text/html;charset=utf8");
    }

    @Test
    public void testHandlePath() {
        given().when().get("/audit")
                .then()
                .statusCode(200);

        given().when().get("/audit/decision/9cf2179f-4fed-4793-b674-a19c45e6cbff/outcomes")
                .then()
                .statusCode(200);
    }
}
