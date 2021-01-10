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

package org.kie.kogito.jitexecutor.dmnexecutor;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
public class SchemaResourceTest {

    @Test
    public void test() {
        given()
        .contentType(ContentType.XML)
        .body(MODEL)
        .when().post("/schema")
        .then()
        .statusCode(200)
        .body(containsString("InputSet"), containsString("x-dmn-type"))
        ;
    }
    
    private static final String MODEL = "<?xml version=\"1.0\" ?>\r\n<dmn:definitions xmlns:dmn=\"http://www.omg.org/spec/DMN/20180521/MODEL/\" xmlns=\"xls2dmn_4f6c1154-6315-413b-9976-f1a81d9f6ef9\" xmlns:di=\"http://www.omg.org/spec/DMN/20180521/DI/\" xmlns:feel=\"http://www.omg.org/spec/DMN/20180521/FEEL/\" xmlns:dmndi=\"http://www.omg.org/spec/DMN/20180521/DMNDI/\" xmlns:dc=\"http://www.omg.org/spec/DMN/20180521/DC/\" name=\"xls2dmn\" expressionLanguage=\"http://www.omg.org/spec/DMN/20180521/FEEL/\" typeLanguage=\"http://www.omg.org/spec/DMN/20180521/FEEL/\" namespace=\"xls2dmn_4f6c1154-6315-413b-9976-f1a81d9f6ef9\" exporter=\"kie-dmn-xls2dmn\">\r\n  <dmn:inputData id=\"id_FICO_32Score\" name=\"FICO Score\">\r\n    <dmn:variable id=\"idvar_FICO_32Score\" name=\"FICO Score\"></dmn:variable>\r\n  </dmn:inputData>\r\n  <dmn:inputData id=\"id_DTI_32Ratio\" name=\"DTI Ratio\">\r\n    <dmn:variable id=\"idvar_DTI_32Ratio\" name=\"DTI Ratio\"></dmn:variable>\r\n  </dmn:inputData>\r\n  <dmn:inputData id=\"id_PITI_32Ratio\" name=\"PITI Ratio\">\r\n    <dmn:variable id=\"idvar_PITI_32Ratio\" name=\"PITI Ratio\"></dmn:variable>\r\n  </dmn:inputData>\r\n  <dmn:decision id=\"d_Loan_32Approval\" name=\"Loan Approval\">\r\n    <dmn:variable id=\"dvar_Loan_32Approval\" name=\"Loan Approval\"></dmn:variable>\r\n    <dmn:informationRequirement>\r\n      <dmn:requiredInput href=\"#id_FICO_32Score\"></dmn:requiredInput>\r\n    </dmn:informationRequirement>\r\n    <dmn:informationRequirement>\r\n      <dmn:requiredDecision href=\"#d_DTI_32Rating\"></dmn:requiredDecision>\r\n    </dmn:informationRequirement>\r\n    <dmn:informationRequirement>\r\n      <dmn:requiredDecision href=\"#d_PITI_32Rating\"></dmn:requiredDecision>\r\n    </dmn:informationRequirement>\r\n    <dmn:decisionTable id=\"ddt_Loan_32Approval\" hitPolicy=\"ANY\" preferredOrientation=\"Rule-as-Row\" outputLabel=\"Loan Approval\">\r\n      <dmn:input label=\"FICO Score\">\r\n        <dmn:inputExpression>\r\n          <dmn:text>FICO Score</dmn:text>\r\n        </dmn:inputExpression>\r\n      </dmn:input>\r\n      <dmn:input label=\"DTI Rating\">\r\n        <dmn:inputExpression>\r\n          <dmn:text>DTI Rating</dmn:text>\r\n        </dmn:inputExpression>\r\n      </dmn:input>\r\n      <dmn:input label=\"PITI Rating\">\r\n        <dmn:inputExpression>\r\n          <dmn:text>PITI Rating</dmn:text>\r\n        </dmn:inputExpression>\r\n      </dmn:input>\r\n      <dmn:output></dmn:output>\r\n      <dmn:rule>\r\n        <dmn:inputEntry>\r\n          <dmn:text>&lt;=750</dmn:text>\r\n        </dmn:inputEntry>\r\n        <dmn:inputEntry>\r\n          <dmn:text>-</dmn:text>\r\n        </dmn:inputEntry>\r\n        <dmn:inputEntry>\r\n          <dmn:text>-</dmn:text>\r\n        </dmn:inputEntry>\r\n        <dmn:outputEntry>\r\n          <dmn:text>\"Not approved\"</dmn:text>\r\n        </dmn:outputEntry>\r\n      </dmn:rule>\r\n      <dmn:rule>\r\n        <dmn:inputEntry>\r\n          <dmn:text>-</dmn:text>\r\n        </dmn:inputEntry>\r\n        <dmn:inputEntry>\r\n          <dmn:text>\"Bad\"</dmn:text>\r\n        </dmn:inputEntry>\r\n        <dmn:inputEntry>\r\n          <dmn:text>-</dmn:text>\r\n        </dmn:inputEntry>\r\n        <dmn:outputEntry>\r\n          <dmn:text>\"Not approved\"</dmn:text>\r\n        </dmn:outputEntry>\r\n      </dmn:rule>\r\n      <dmn:rule>\r\n        <dmn:inputEntry>\r\n          <dmn:text>-</dmn:text>\r\n        </dmn:inputEntry>\r\n        <dmn:inputEntry>\r\n          <dmn:text>-</dmn:text>\r\n        </dmn:inputEntry>\r\n        <dmn:inputEntry>\r\n          <dmn:text>\"Bad\"</dmn:text>\r\n        </dmn:inputEntry>\r\n        <dmn:outputEntry>\r\n          <dmn:text>\"Not approved\"</dmn:text>\r\n        </dmn:outputEntry>\r\n      </dmn:rule>\r\n      <dmn:rule>\r\n        <dmn:inputEntry>\r\n          <dmn:text>&gt;750</dmn:text>\r\n        </dmn:inputEntry>\r\n        <dmn:inputEntry>\r\n          <dmn:text>\"Good\"</dmn:text>\r\n        </dmn:inputEntry>\r\n        <dmn:inputEntry>\r\n          <dmn:text>\"Good\"</dmn:text>\r\n        </dmn:inputEntry>\r\n        <dmn:outputEntry>\r\n          <dmn:text>\"Approved\"</dmn:text>\r\n        </dmn:outputEntry>\r\n      </dmn:rule>\r\n    </dmn:decisionTable>\r\n  </dmn:decision>\r\n  <dmn:decision id=\"d_DTI_32Rating\" name=\"DTI Rating\">\r\n    <dmn:variable id=\"dvar_DTI_32Rating\" name=\"DTI Rating\"></dmn:variable>\r\n    <dmn:informationRequirement>\r\n      <dmn:requiredInput href=\"#id_DTI_32Ratio\"></dmn:requiredInput>\r\n    </dmn:informationRequirement>\r\n    <dmn:decisionTable id=\"ddt_DTI_32Rating\" hitPolicy=\"ANY\" preferredOrientation=\"Rule-as-Row\" outputLabel=\"DTI Rating\">\r\n      <dmn:input label=\"DTI Ratio\">\r\n        <dmn:inputExpression>\r\n          <dmn:text>DTI Ratio</dmn:text>\r\n        </dmn:inputExpression>\r\n      </dmn:input>\r\n      <dmn:output></dmn:output>\r\n      <dmn:rule>\r\n        <dmn:inputEntry>\r\n          <dmn:text>&lt;=0.20</dmn:text>\r\n        </dmn:inputEntry>\r\n        <dmn:outputEntry>\r\n          <dmn:text>\"Good\"</dmn:text>\r\n        </dmn:outputEntry>\r\n      </dmn:rule>\r\n      <dmn:rule>\r\n        <dmn:inputEntry>\r\n          <dmn:text>&gt;0.20</dmn:text>\r\n        </dmn:inputEntry>\r\n        <dmn:outputEntry>\r\n          <dmn:text>\"Bad\"</dmn:text>\r\n        </dmn:outputEntry>\r\n      </dmn:rule>\r\n    </dmn:decisionTable>\r\n  </dmn:decision>\r\n  <dmn:decision id=\"d_PITI_32Rating\" name=\"PITI Rating\">\r\n    <dmn:variable id=\"dvar_PITI_32Rating\" name=\"PITI Rating\"></dmn:variable>\r\n    <dmn:informationRequirement>\r\n      <dmn:requiredInput href=\"#id_PITI_32Ratio\"></dmn:requiredInput>\r\n    </dmn:informationRequirement>\r\n    <dmn:decisionTable id=\"ddt_PITI_32Rating\" hitPolicy=\"ANY\" preferredOrientation=\"Rule-as-Row\" outputLabel=\"PITI Rating\">\r\n      <dmn:input label=\"PITI Ratio\">\r\n        <dmn:inputExpression>\r\n          <dmn:text>PITI Ratio</dmn:text>\r\n        </dmn:inputExpression>\r\n      </dmn:input>\r\n      <dmn:output></dmn:output>\r\n      <dmn:rule>\r\n        <dmn:inputEntry>\r\n          <dmn:text>&lt;=0.28</dmn:text>\r\n        </dmn:inputEntry>\r\n        <dmn:outputEntry>\r\n          <dmn:text>\"Good\"</dmn:text>\r\n        </dmn:outputEntry>\r\n      </dmn:rule>\r\n      <dmn:rule>\r\n        <dmn:inputEntry>\r\n          <dmn:text>&gt;0.28</dmn:text>\r\n        </dmn:inputEntry>\r\n        <dmn:outputEntry>\r\n          <dmn:text>\"Bad\"</dmn:text>\r\n        </dmn:outputEntry>\r\n      </dmn:rule>\r\n    </dmn:decisionTable>\r\n  </dmn:decision>\r\n</dmn:definitions>\r\n";
}