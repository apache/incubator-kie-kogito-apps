package org.kie.kogito.index.graphql;

import org.junit.jupiter.api.Test;

import graphql.schema.GraphQLScalarType;

import static org.assertj.core.api.Assertions.assertThat;

class DateTimeScalarTypeProducerTest {

    GraphQLScalarType dateTimeScalar = new GraphQLScalarTypeProducer(new DefaultDateTimeCoercing()).dateTimeScalar();

    @Test
    void testScalarType() {
        assertThat(dateTimeScalar.getName()).isEqualTo("DateTime");
        assertThat(dateTimeScalar.getDescription()).isEqualTo("An ISO-8601 compliant DateTime Scalar");
        assertThat(dateTimeScalar.getCoercing()).isNotNull();
    }
}
