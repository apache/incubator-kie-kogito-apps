package org.kie.kogito.index.service.auth;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.oidc.TenantResolver;
import io.vertx.ext.web.RoutingContext;

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
