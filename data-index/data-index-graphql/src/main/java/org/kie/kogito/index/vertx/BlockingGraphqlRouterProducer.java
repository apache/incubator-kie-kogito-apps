package org.kie.kogito.index.vertx;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.quarkus.arc.properties.IfBuildProperty;
import io.quarkus.vertx.web.Route;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.graphql.ApolloWSHandler;
import io.vertx.ext.web.handler.graphql.GraphQLHandler;
import io.vertx.ext.web.handler.graphql.GraphQLHandlerOptions;

import graphql.GraphQL;

import static io.quarkus.vertx.web.Route.HttpMethod.GET;
import static io.quarkus.vertx.web.Route.HttpMethod.POST;

@ApplicationScoped
@IfBuildProperty(name = "kogito.data-index.blocking", stringValue = "true")
public class BlockingGraphqlRouterProducer {

    @Inject
    GraphQL graphQL;

    GraphQLHandler graphQLHandler;

    ApolloWSHandler apolloWSHandler;

    @PostConstruct
    public void init() {
        graphQLHandler = GraphQLHandler.create(graphQL, new GraphQLHandlerOptions());
        apolloWSHandler = ApolloWSHandler.create(graphQL);
    }

    @Route(path = "/graphql", type = Route.HandlerType.BLOCKING, order = 1, methods = { GET })
    public void blockingApolloWSHandlerGet(RoutingContext rc) {
        apolloWSHandler.handle(rc);
    }

    @Route(path = "/graphql", type = Route.HandlerType.BLOCKING, order = 1, methods = { POST })
    public void blockingApolloWSHandlerPost(RoutingContext rc) {
        apolloWSHandler.handle(rc);
    }

    @Route(path = "/graphql", type = Route.HandlerType.BLOCKING, order = 2, methods = { GET })
    public void blockingGraphQLHandlerGet(RoutingContext rc) {
        graphQLHandler.handle(rc);
    }

    @Route(path = "/graphql", type = Route.HandlerType.BLOCKING, order = 2, methods = { POST })
    public void blockingGraphQLHandlerPost(RoutingContext rc) {
        graphQLHandler.handle(rc);
    }

}
