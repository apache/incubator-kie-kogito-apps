package org.kie.kogito.index.graphql;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import graphql.GraphQL;

@ApplicationScoped
public class GraphQLProducer {

    @Inject
    GraphQLInstrumentation instrumentation;

    @Inject
    GraphQLSchemaManager manager;

    @Produces
    public GraphQL createGraphQL() {
        return GraphQL.newGraphQL(manager.getGraphQLSchema())
                .instrumentation(instrumentation)
                .build();
    }
}
