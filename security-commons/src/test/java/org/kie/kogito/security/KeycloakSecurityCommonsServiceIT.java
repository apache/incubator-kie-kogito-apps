package org.kie.kogito.security;

import org.junit.jupiter.api.Test;
import org.keycloak.representations.AccessTokenResponse;
import org.kie.kogito.test.quarkus.QuarkusTestProperty;
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

    @QuarkusTestProperty(name = KeycloakQuarkusTestResource.KOGITO_KEYCLOAK_PROPERTY)
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
