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

package org.kie.kogito.jitexecutor.dmn.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.drools.core.util.IoUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.kie.kogito.jitexecutor.dmn.requests.JITDMNPayload;
import org.kie.kogito.jitexecutor.dmn.requests.ResourceWithURI;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class JITDMNResourceMultipleModelsTest {

    private static final String URI1 = "/DMNv1_3/Chapter 11 Example.dmn";
    private static final String URI2 = "/DMNv1_3/Financial.dmn";

    private static ResourceWithURI model1;
    private static ResourceWithURI model2;

    @BeforeAll
    public static void setup() throws IOException {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        model1 = new ResourceWithURI(URI1, new String(IoUtils.readBytesFromInputStream(JITDMNResourceTest.class.getResourceAsStream(URI1))));
        model2 = new ResourceWithURI(URI2, new String(IoUtils.readBytesFromInputStream(JITDMNResourceTest.class.getResourceAsStream(URI2))));
    }

    @Test
    public void testjitEndpoint() {
        JITDMNPayload jitdmnpayload = new JITDMNPayload(URI1, List.of(model1, model2), buildContext());
        given()
                .contentType(ContentType.JSON)
                .body(jitdmnpayload)
                .when().post("/jitdmn")
                .then()
                .statusCode(200)
                .body("Strategy", is("THROUGH"));
    }

    @Test
    public void testjitdmnResultEndpoint() {
        JITDMNPayload jitdmnpayload = new JITDMNPayload(URI1, List.of(model1, model2), buildContext());
        given()
                .contentType(ContentType.JSON)
                .body(jitdmnpayload)
                .when().post("/jitdmn/dmnresult")
                .then()
                .statusCode(200)
                .body("dmnContext.Strategy", is("THROUGH"));
    }

    private Map<String, Object> buildContext() {
        Map<String, Object> context = new HashMap<>();
        context.put("Applicant data", Map.of("Age", 51,
                "MartitalStatus", "M", // typo is present in DMNv1.3
                "EmploymentStatus", "EMPLOYED",
                "ExistingCustomer", false,
                "Monthly", Map.of("Income", 100_000,
                        "Repayments", 2_500,
                        "Expenses", 10_000)));
        context.put("Bureau data", Map.of("Bankrupt", false,
                "CreditScore", 600));
        context.put("Requested product", Map.of("ProductType", "STANDARD LOAN",
                "Rate", 0.08d,
                "Term", 36,
                "Amount", 100_00));
        context.put("Supporting documents", null);
        return context;
    }
}
