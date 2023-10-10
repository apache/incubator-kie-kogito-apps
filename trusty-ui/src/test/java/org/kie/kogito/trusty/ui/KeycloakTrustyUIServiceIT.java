package org.kie.kogito.trusty.ui;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.keycloak.representations.AccessTokenResponse;
import org.kie.kogito.test.quarkus.QuarkusTestProperty;
import org.kie.kogito.testcontainers.KogitoKeycloakContainer;
import org.kie.kogito.testcontainers.quarkus.KeycloakQuarkusTestResource;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.ResourceArg;
import io.quarkus.test.junit.QuarkusTest;

import static io.restassured.RestAssured.given;
import static org.kie.kogito.testcontainers.quarkus.KeycloakQuarkusTestResource.KOGITO_OIDC_TENANTS;

@QuarkusTest
@QuarkusTestResource(value = KeycloakQuarkusTestResource.class, initArgs = { @ResourceArg(name = KOGITO_OIDC_TENANTS, value = "web-app-tenant") })
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KeycloakTrustyUIServiceIT {

    private static final String VALID_USER = "jdoe";
    private static final String TRUSTY_UI_ENDPOINT = "/";

    @QuarkusTestProperty(name = KeycloakQuarkusTestResource.KOGITO_KEYCLOAK_PROPERTY)
    String keycloakURL;

    @Test
    void shouldReturnUnauthorized() {
        given().get(TRUSTY_UI_ENDPOINT)
                .then().statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    void shouldReturnOkWhenValidUser() {
        given().auth().oauth2(getAccessToken(VALID_USER)).get(TRUSTY_UI_ENDPOINT)
                .then().statusCode(HttpStatus.SC_OK);
    }

    private String getAccessToken(String userName) {
        return given().param("grant_type", "password")
                .param("username", userName)
                .param("password", userName)
                .param("client_id", KogitoKeycloakContainer.CLIENT_ID)
                .param("client_secret", KogitoKeycloakContainer.CLIENT_SECRET)
                .when()
                .post(keycloakURL + "/protocol/openid-connect/token")
                .as(AccessTokenResponse.class).getToken();
    }
}
