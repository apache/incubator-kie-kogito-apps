package org.kie.kogito.trusty.service.api;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class ExplainabilityApiV1IT {

    @Test
    void testFeatureImportance() {
        // TODO: implement this
        given().filter(new ResponseLoggingFilter())
                .when().get("/executions/decisions/ID/featureImportance")
                .then().statusCode(200);
    }
}
