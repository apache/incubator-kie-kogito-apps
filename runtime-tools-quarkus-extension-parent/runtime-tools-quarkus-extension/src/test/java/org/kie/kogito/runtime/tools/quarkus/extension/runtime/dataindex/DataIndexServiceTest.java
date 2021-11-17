/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.kogito.runtime.tools.quarkus.extension.runtime.dataindex;

import org.junit.jupiter.api.Test;
import org.kie.kogito.runtime.tools.quarkus.extension.runtime.forms.FormsStorage;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Mockito.when;

@QuarkusTest
public class DataIndexServiceTest {

    public static final String ERROR_RESPONSE = "something wrong happened!";
    public static final String PROCESS_INSTANCE_RESPONSE =
            "{\"data\":{\"ProcessInstances\":[{\"id\":\"2aa76968-5087-433a-a61f-9d3ca2feb270\"},{\"id\":\"2652e27e-127f-4b2a-961b-718a50d97212\"},{\"id\":\"5da599ad-ae7c-492f-b2c1-4a4a559dd64c\"},{\"id\":\"12ee3d38-2467-4cc0-89e6-1e6547f95cd8\"},{\"id\":\"edd5d47a-2fd1-4f8b-87a0-a8ab3f938993\"}]}}";
    public static final String EMPTY_PROCESS_INSTANCE_RESPONSE = "{\"data\":{\"ProcessInstances\":[]}}";
    public static final String USER_TASK_RESPONSE =
            "{\"data\":{\"UserTaskInstances\":[{\"id\":\"a859c055-3301-487a-a252-95bc35849d5c\"},{\"id\":\"cfe73332-6942-44f0-b00b-7f63349c7465\"},{\"id\":\"3ebb40a3-9ed6-4be9-bae7-5cb036870327\"},{\"id\":\"04e200d7-c191-4b29-9f6f-09c556ff5a97\"},{\"id\":\"4ab9ddff-6edb-44fa-9ed4-2b4fc797c23d\"}]}}";
    public static final String EMPTY_USER_TASK_RESPONSE = "{\"data\":{\"UserTaskInstances\":[]}}";
    public static final String JOBS_RESPONSE =
            "{\"data\":{\"Jobs\":[{\"id\":\"a859c055-3301-487a-a252-95bc35849d5c\"},{\"id\":\"cfe73332-6942-44f0-b00b-7f63349c7465\"},{\"id\":\"3ebb40a3-9ed6-4be9-bae7-5cb036870327\"},{\"id\":\"04e200d7-c191-4b29-9f6f-09c556ff5a97\"},{\"id\":\"4ab9ddff-6edb-44fa-9ed4-2b4fc797c23d\"}]}}";
    public static final String EMPTY_JOBS_RESPONSE = "{\"data\":{\"Jobs\":[]}}";

    @InjectMock
    private static DataIndexClient dataIndexClient;

    @InjectMock
    private FormsStorage formsStorage;

    @Test
    public void testProcessInstancesCount() {
        when(dataIndexClient.query(DataIndexService.ALL_PROCESS_INSTANCES_IDS_QUERY)).thenReturn(PROCESS_INSTANCE_RESPONSE);

        given()
                .when().get("/dataindex/processInstances/count")
                .then()
                .statusCode(200)
                .body(equalTo("5"));
    }

    @Test
    public void testEmptyProcessInstancesCount() {
        when(dataIndexClient.query(DataIndexService.ALL_PROCESS_INSTANCES_IDS_QUERY)).thenReturn(EMPTY_PROCESS_INSTANCE_RESPONSE);

        given()
                .when().get("/dataindex/processInstances/count")
                .then()
                .statusCode(200)
                .body(equalTo("0"));
    }

    @Test
    public void testProcessInstancestCountError() {
        when(dataIndexClient.query(DataIndexService.ALL_PROCESS_INSTANCES_IDS_QUERY)).thenReturn(ERROR_RESPONSE);

        given()
                .when().get("/dataindex/processInstances/count")
                .then()
                .statusCode(500);
    }

    @Test
    public void testJobsCount() {
        when(dataIndexClient.query(DataIndexService.ALL_JOBS_IDS_QUERY)).thenReturn(JOBS_RESPONSE);

        given()
                .when().get("/dataindex/jobs/count")
                .then()
                .statusCode(200)
                .body(equalTo("5"));
    }

    @Test
    public void testEmptyJobsCount() {
        when(dataIndexClient.query(DataIndexService.ALL_JOBS_IDS_QUERY)).thenReturn(EMPTY_JOBS_RESPONSE);

        given()
                .when().get("/dataindex/jobs/count")
                .then()
                .statusCode(200)
                .body(equalTo("0"));
    }

    @Test
    public void testJobsCountError() {
        when(dataIndexClient.query(DataIndexService.ALL_JOBS_IDS_QUERY)).thenReturn(ERROR_RESPONSE);

        given()
                .when().get("/dataindex/jobs/count")
                .then()
                .statusCode(500);
    }

    @Test
    public void testTasksCount() {
        when(dataIndexClient.query(DataIndexService.ALL_TASKS_IDS_QUERY)).thenReturn(USER_TASK_RESPONSE);

        given()
                .when().get("/dataindex/tasks/count")
                .then()
                .statusCode(200)
                .body(equalTo("5"));
    }

    @Test
    public void testEmptyTasksCount() {
        when(dataIndexClient.query(DataIndexService.ALL_TASKS_IDS_QUERY)).thenReturn(EMPTY_USER_TASK_RESPONSE);

        given()
                .when().get("/dataindex/tasks/count")
                .then()
                .statusCode(200)
                .body(equalTo("0"));
    }

    @Test
    public void testTasksCountError() {
        when(dataIndexClient.query(DataIndexService.ALL_TASKS_IDS_QUERY)).thenReturn(ERROR_RESPONSE);

        given()
                .when().get("/dataindex/tasks/count")
                .then()
                .statusCode(500);
    }

    @Test
    public void testFormsCount() {
        when(formsStorage.getFormsCount()).thenReturn(5);

        given()
                .when().get("/dataindex/forms/count")
                .then()
                .statusCode(200)
                .body(equalTo("5"));
    }

    @Test
    public void testFormsCountError() {
        when(formsStorage.getFormsCount()).thenThrow(new RuntimeException("something went wrong!"));

        given()
                .when().get("/dataindex/forms/count")
                .then()
                .statusCode(500);
    }
}
