package org.kie.kogito.explainability.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.kie.kogito.explainability.api.ExplainabilityRequestDto;
import org.kie.kogito.explainability.api.ExplainabilityResultDto;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class ExplainabilityApiV1Test {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testEndpointWithRequest() throws JsonProcessingException {
        String executionId = "test";
        String body = MAPPER.writeValueAsString(new ExplainabilityRequestDto(executionId));

        ExplainabilityResultDto result = given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/v1/explain")
                .as(ExplainabilityResultDto.class);

        assertEquals(executionId, result.getExecutionId());
    }
}
