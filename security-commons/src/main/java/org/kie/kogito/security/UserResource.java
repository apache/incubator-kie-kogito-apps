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

import java.util.Collections;
import java.util.Set;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.cache.NoCache;

import io.quarkus.security.Authenticated;
import io.quarkus.security.credential.TokenCredential;
import io.quarkus.security.identity.SecurityIdentity;

@Path(UserResource.USER_PATH)
@Authenticated
public class UserResource {

    public static final String USER_PATH = "/api/user";

    @Inject
    SecurityIdentity identity;

    @GET
    @Path("/me")
    @Produces(MediaType.APPLICATION_JSON)
    @NoCache
    public User me() {
        return new User(identity);
    }

    protected void setSecurityIdentity(SecurityIdentity securityIdentity) {
        this.identity = securityIdentity;
    }

    public static class User {

        private String userName = "Anonymous";
        private Set<String> roles = Collections.emptySet();
        private String token = "";

        User(SecurityIdentity identity) {
            if (identity != null &&
                    identity.getPrincipal() != null &&
                    identity.getCredential(TokenCredential.class) != null) {
                this.userName = identity.getPrincipal().getName();
                this.roles = identity.getRoles();
                this.token = identity.getCredential(TokenCredential.class).getToken();
            }
        }

        public String getUserName() {
            return userName;
        }

        public Set<String> getRoles() {
            return roles;
        }

        public String getToken() {
            return token;
        }
    }
}
