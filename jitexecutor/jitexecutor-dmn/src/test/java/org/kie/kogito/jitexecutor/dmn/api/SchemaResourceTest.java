package org.kie.kogito.jitexecutor.dmn.api;

import java.io.IOException;

import org.drools.util.IoUtils;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

@QuarkusTest
public class SchemaResourceTest {

    @Test
    public void test() throws IOException {
        final String MODEL = new String(IoUtils.readBytesFromInputStream(JITDMNResourceTest.class.getResourceAsStream("/test.dmn")));
        given()
                .contentType(ContentType.XML)
                .body(MODEL)
                .when().post("/jitdmn/schema")
                .then()
                .statusCode(200)
                .body(containsString("InputSet"), containsString("x-dmn-type"));
    }
}
