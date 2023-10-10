package org.kie.kogito.index.graphql;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import graphql.schema.GraphQLScalarType;

@ApplicationScoped
public class GraphQLScalarTypeProducer {

    private DateTimeCoercing dateTimeCoercing;

    @Inject
    public GraphQLScalarTypeProducer(DateTimeCoercing dateTimeCoercing) {
        this.dateTimeCoercing = dateTimeCoercing;
    }

    @Produces
    public GraphQLScalarType dateTimeScalar() {
        return GraphQLScalarType.newScalar()
                .name("DateTime")
                .description("An ISO-8601 compliant DateTime Scalar")
                .coercing(dateTimeCoercing)
                .build();
    }
}
