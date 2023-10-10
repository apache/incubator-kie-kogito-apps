package org.kie.kogito.index.graphql;

import java.util.function.Consumer;

import graphql.schema.GraphQLSchema;

public interface GraphQLSchemaManager {

    GraphQLSchema getGraphQLSchema();

    void transform(Consumer<GraphQLSchema.Builder> builder);
}
