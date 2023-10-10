package org.kie.kogito.index.graphql.query;

import java.util.HashMap;
import java.util.Map;

import graphql.schema.GraphQLInputObjectType;

public class GraphQLQueryParserRegistry {

    private static final GraphQLQueryParserRegistry REGISTRY = new GraphQLQueryParserRegistry();

    private final Map<String, GraphQLQueryParser> queryParser = new HashMap<>();

    private GraphQLQueryParserRegistry() {
    }

    public static GraphQLQueryParserRegistry get() {
        return REGISTRY;
    }

    public void registerParser(GraphQLInputObjectType type) {
        queryParser.put(type.getName(), new GraphQLQueryMapper().apply(type));
    }

    public void registerParsers(GraphQLInputObjectType... types) {
        for (GraphQLInputObjectType type : types) {
            registerParser(type);
        }
    }

    public GraphQLQueryParser getParser(String name) {
        return queryParser.get(name);
    }
}
