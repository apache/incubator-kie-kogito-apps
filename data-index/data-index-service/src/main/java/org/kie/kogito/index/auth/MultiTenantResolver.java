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

package org.kie.kogito.index.auth;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.quarkus.oidc.TenantResolver;
import io.vertx.ext.web.RoutingContext;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class MultiTenantResolver implements TenantResolver {

    @Inject
    @ConfigProperty(name = "kogito.data-index.vertx-graphql.ui.path", defaultValue = "/graphiql")
    String graphUIPath;

    @Inject
    @ConfigProperty(name = "kogito.data-index.vertx-graphql.ui.tenant", defaultValue = "web-app-tenant")
    String graphUITenantId;

    @Override
    public String resolve(RoutingContext context) {
        if (context.request().path().equals("/") || context.request().path().startsWith(graphUIPath)) {
            return graphUITenantId;
        }
        return null;
    }
}
