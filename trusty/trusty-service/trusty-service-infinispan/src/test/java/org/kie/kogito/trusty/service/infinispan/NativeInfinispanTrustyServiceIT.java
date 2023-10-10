package org.kie.kogito.trusty.service.infinispan;

import org.junit.jupiter.api.Test;
import org.kie.kogito.testcontainers.quarkus.InfinispanQuarkusTestResource;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.NativeImageTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@NativeImageTest
@QuarkusTestResource(InfinispanQuarkusTestResource.class)
public class NativeInfinispanTrustyServiceIT {

    @Test
    public void testImageGeneration() {
        given()
                .when().get("/q/health/live")
                .then()
                .statusCode(200)
                .body("status", is("UP"));
    }

}
