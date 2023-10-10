package org.kie.kogito.index.graphql;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;

import graphql.execution.instrumentation.SimpleInstrumentation;
import graphql.execution.instrumentation.parameters.InstrumentationExecutionParameters;
import graphql.execution.instrumentation.parameters.InstrumentationFieldFetchParameters;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.PropertyDataFetcher;

@ApplicationScoped
public class GraphQLInstrumentation extends SimpleInstrumentation {

    @Inject
    GraphQLSchemaManager manager;

    @Override
    public DataFetcher<?> instrumentDataFetcher(DataFetcher<?> dataFetcher, InstrumentationFieldFetchParameters parameters) {
        if (parameters.getEnvironment().getSource() instanceof JsonNode && dataFetcher instanceof PropertyDataFetcher) {
            return new JsonPropertyDataFetcher();
        } else {
            return dataFetcher;
        }
    }

    @Override
    public GraphQLSchema instrumentSchema(GraphQLSchema schema, InstrumentationExecutionParameters parameters) {
        return manager.getGraphQLSchema();
    }
}
