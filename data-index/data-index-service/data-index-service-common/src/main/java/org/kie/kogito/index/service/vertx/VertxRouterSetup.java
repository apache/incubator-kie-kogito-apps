package org.kie.kogito.index.service.vertx;

import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.oidc.AccessTokenCredential;
import io.quarkus.vertx.http.runtime.security.QuarkusHttpUser;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpHeaders;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.FaviconHandler;
import io.vertx.ext.web.handler.LoggerHandler;
import io.vertx.ext.web.handler.graphql.GraphiQLHandler;
import io.vertx.ext.web.handler.graphql.GraphiQLHandlerOptions;

@ApplicationScoped
public class VertxRouterSetup {

    @Inject
    @ConfigProperty(name = "quarkus.oidc.enabled", defaultValue = "false")
    Boolean authEnabled;

    @Inject
    @ConfigProperty(name = "kogito.data-index.vertx-graphql.ui.path", defaultValue = "/graphiql")
    String graphUIPath;

    @Inject
    Vertx vertx;

    void setupRouter(@Observes Router router) {
        router.route().handler(LoggerHandler.create());
        GraphiQLHandler graphiQLHandler = GraphiQLHandler.create(new GraphiQLHandlerOptions().setEnabled(true));
        if (Boolean.TRUE.equals(authEnabled)) {
            addGraphiqlRequestHeader(graphiQLHandler);
        }
        router.route().handler(BodyHandler.create());
        router.route(graphUIPath + "/*").handler(graphiQLHandler);
        router.route("/").handler(ctx -> ctx.response().putHeader("location", graphUIPath + "/").setStatusCode(302).end());
        router.route().handler(FaviconHandler.create(vertx));
    }

    protected void addGraphiqlRequestHeader(GraphiQLHandler graphiQLHandler) {
        graphiQLHandler.graphiQLRequestHeaders(rc -> MultiMap.caseInsensitiveMultiMap().add(HttpHeaders.AUTHORIZATION, "Bearer " + getCurrentAccessToken(rc)));
    }

    private String getCurrentAccessToken(RoutingContext routingContext) {
        return Optional.ofNullable(routingContext.user())
                .map(user -> ((QuarkusHttpUser) user).getSecurityIdentity())
                .map(identity -> identity.getCredential(AccessTokenCredential.class))
                .map(AccessTokenCredential::getToken)
                .orElse("");
    }
}
