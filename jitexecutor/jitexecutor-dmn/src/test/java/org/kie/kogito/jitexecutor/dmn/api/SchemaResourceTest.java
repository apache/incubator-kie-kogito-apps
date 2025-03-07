/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.kie.kogito.jitexecutor.dmn.api;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.kie.kogito.jitexecutor.dmn.TestingUtils.getModelFromIoUtils;

@QuarkusTest
public class SchemaResourceTest {

    @Test
    public void test() throws IOException {
        final String MODEL = getModelFromIoUtils("invalid_models/DMNv1_x/test.dmn");
        given()
                .contentType(ContentType.XML)
                .body(MODEL)
                .when().post("/jitdmn/schema")
                .then()
                .statusCode(200)
                .body(containsString("InputSet"), containsString("x-dmn-type"));
    }

    @Test
    public void testJitDmnValidate() throws IOException {
        final String MODEL = getModelFromIoUtils("invalid_models/DMNv1_5/DMN-Invalid.dmn");
        given()
                .contentType(ContentType.XML)
                .body(MODEL)
                .when().post("/jitdmn/schema")
                .then()
                .statusCode(400)
                .body(containsString("Error compiling FEEL expression 'Person Age >= 18' for name 'Can Drive?' on node 'Can Drive?': syntax error near 'Age'"));

    }
}
