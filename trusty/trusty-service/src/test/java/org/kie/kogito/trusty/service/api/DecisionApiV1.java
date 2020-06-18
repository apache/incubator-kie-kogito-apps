package org.kie.kogito.trusty.service.api;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.kie.kogito.trusty.service.ITrustyService;
import org.kie.kogito.trusty.service.responses.ExecutionHeaderResponse;
import org.kie.kogito.trusty.storage.api.model.Decision;
import org.mockito.Mockito;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class DecisionApiV1 {

    @InjectMock
    ITrustyService executionService;

    @Test
    void GivenAValidRequest_WhenExecutionEndpointIsCalled_ThenTheDefaultValuesAreCorrect() {
        Decision decision = new Decision();
        decision.setExecutionId("executionId");
        decision.setExecutionTimestamp(1591692950000L);
        Mockito.when(executionService.getDecisionById("executionId")).thenReturn(decision);

        ExecutionHeaderResponse response = given().contentType(ContentType.JSON).when().get("/v1/executions/decisions/executionId").as(ExecutionHeaderResponse.class);

        Assertions.assertEquals("executionId", response.getExecutionId());
    }

    @Test
    void GivenAnInvalidRequest_WhenExecutionEndpointIsCalled_ThenBadRequestIsReturned() {
        Mockito.when(executionService.getDecisionById("executionId")).thenThrow(new RuntimeException("Execution does not exist."));

        given().contentType(ContentType.JSON).when().get("/v1/executions/decisions/executionId").then().statusCode(400);
    }
}
