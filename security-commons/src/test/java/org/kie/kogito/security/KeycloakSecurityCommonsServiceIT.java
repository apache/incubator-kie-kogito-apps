/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
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
package org.kie.kogito.security;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.Test;
import org.keycloak.representations.AccessTokenResponse;
import org.kie.kogito.testcontainers.KogitoKeycloakContainer;
import org.kie.kogito.testcontainers.quarkus.KeycloakQuarkusTestResource;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;

@QuarkusTest
@QuarkusTestResource(KeycloakQuarkusTestResource.class)
class KeycloakSecurityCommonsServiceIT {

    public static final int OK_CODE = 200;
    public static final int FORBIDDEN_CODE = 401;

    @ConfigProperty(name = KeycloakQuarkusTestResource.KOGITO_KEYCLOAK_PROPERTY)
    String keycloakURL;

    @Test
    void meTest() throws Exception {
        given().auth().oauth2(getAccessToken("jdoe"))
                .when()
                .get(UserResource.USER_PATH + "/me")
                .then()
                .statusCode(OK_CODE)
                .body("userName", is("jdoe"))
                .body("roles", hasSize(2));

        given().auth().oauth2(getAccessToken("alice"))
                .when()
                .get(UserResource.USER_PATH + "/me")
                .then()
                .statusCode(OK_CODE)
                .body("userName", is("alice"))
                .body("roles", hasSize(1));

        given().when()
                .get(UserResource.USER_PATH + "/me")
                .then()
                .statusCode(FORBIDDEN_CODE);
    }

    private String getAccessToken(String userName) {
        return given()
                .param("grant_type", "password")
                .param("username", userName)
                .param("password", userName)
                .param("client_id", KogitoKeycloakContainer.CLIENT_ID)
                .param("client_secret", KogitoKeycloakContainer.CLIENT_SECRET)
                .when()
                .post(keycloakURL + "/protocol/openid-connect/token")
                .as(AccessTokenResponse.class).getToken();
    }
}
