package org.kie.kogito.trusty.service.api;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.kie.kogito.trusty.service.ITrustyService;
import org.kie.kogito.trusty.service.models.Execution;
import org.kie.kogito.trusty.service.models.ExecutionTypeEnum;
import org.kie.kogito.trusty.service.responses.ExecutionHeaderResponse;
import org.mockito.Mockito;

import static io.restassured.RestAssured.given;

@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DecisionsApiV1Test {

    private static final DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    @InjectMock
    ITrustyService executionService;

    @Test
    public void GivenAnExecution_WhenDecisionDetailEndpointIsCalled_ThenTheExecutionDetailIsProperlyReturned() throws ParseException {
        Execution execution = new Execution("test1", sdf.parse("2020-01-01T00:00:00Z"), true, "name", "model", ExecutionTypeEnum.DECISION);
        Mockito.when(executionService.getExecutionById("test1")).thenReturn(Optional.of(execution));
        ExecutionHeaderResponse response = given().contentType(ContentType.JSON).when().get("/v1/executions/decisions/test1").as(ExecutionHeaderResponse.class);
        Assertions.assertNotNull(response);
        Assertions.assertEquals("test1", response.getExecutionId());
        Assertions.assertTrue(response.hasSucceeded());
    }

    @Test
    public void GivenNoExecutions_WhenDecisionDetailEndpointIsCalled_ThenBadRequestIsReturned() throws ParseException {
        Mockito.when(executionService.getExecutionById("test1")).thenReturn(Optional.empty());
        given().contentType(ContentType.JSON).when().get("/v1/executions/decisions/test1").then().statusCode(400);
    }
}
