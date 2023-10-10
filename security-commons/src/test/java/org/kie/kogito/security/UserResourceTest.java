package org.kie.kogito.security;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import io.quarkus.security.credential.TokenCredential;
import io.quarkus.security.identity.SecurityIdentity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserResourceTest {

    @Test
    void meTest() {
        UserResource userResourceTest = new UserResource();

        String userName = "testName";
        String testToken = "testToken";
        Set roles = new HashSet<String>();
        roles.add("role1");
        Principal mockPrincipal = mock(Principal.class);
        TokenCredential mockCredential = mock(TokenCredential.class);
        SecurityIdentity securityIdentity = mock(SecurityIdentity.class);
        userResourceTest.setSecurityIdentity(securityIdentity);

        when(mockPrincipal.getName()).thenReturn(userName);
        when(securityIdentity.getPrincipal()).thenReturn(mockPrincipal);
        when(securityIdentity.getRoles()).thenReturn(roles);
        when(securityIdentity.getCredential(TokenCredential.class)).thenReturn(mockCredential);
        when(mockCredential.getToken()).thenReturn(testToken);

        UserResource.User u = userResourceTest.me();
        assertEquals(userName, u.getUserName());
        assertEquals(roles, u.getRoles());
        assertEquals(testToken, u.getToken());
    }

}
