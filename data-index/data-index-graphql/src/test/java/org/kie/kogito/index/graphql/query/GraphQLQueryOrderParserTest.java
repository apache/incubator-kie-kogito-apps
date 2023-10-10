package org.kie.kogito.index.graphql.query;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.kie.kogito.persistence.api.query.SortDirection;

import graphql.execution.MergedField;
import graphql.language.Argument;
import graphql.language.EnumValue;
import graphql.language.ObjectField;
import graphql.language.ObjectValue;
import graphql.language.VariableReference;
import graphql.schema.DataFetchingEnvironment;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.kie.kogito.persistence.api.query.QueryFilterFactory.orderBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GraphQLQueryOrderParserTest {

    private static DataFetchingEnvironment mockDataFetchingEnvironment(List<Argument> arguments, Map<String, Object> variables) {
        DataFetchingEnvironment env = mock(DataFetchingEnvironment.class);
        MergedField mergedFiled = mock(MergedField.class);
        when(mergedFiled.getArguments()).thenReturn(arguments);
        when(env.getMergedField()).thenReturn(mergedFiled);
        when(env.getVariables()).thenReturn(variables);
        return env;
    }

    @Test
    void testNull() {
        assertThat(new GraphQLQueryOrderByParser().apply(null)).isEmpty();
    }

    @Test
    void testEmpty() {
        DataFetchingEnvironment env = mockDataFetchingEnvironment(emptyList(), emptyMap());

        assertThat(new GraphQLQueryOrderByParser().apply(env)).isEmpty();
    }

    @Test
    void testNonMatchingArgument() {
        DataFetchingEnvironment env = mockDataFetchingEnvironment(singletonList(Argument.newArgument().name("where").build()), emptyMap());

        assertThat(new GraphQLQueryOrderByParser().apply(env)).isEmpty();
    }

    @Test
    void testSortSingleArgument() {
        DataFetchingEnvironment env = mockDataFetchingEnvironment(singletonList(
                Argument.newArgument().name("orderBy").value(
                        ObjectValue.newObjectValue().objectField(
                                ObjectField.newObjectField().name("start").value(EnumValue.newEnumValue("ASC").build()).build()).build())
                        .build()),
                emptyMap());

        assertThat(new GraphQLQueryOrderByParser().apply(env))
                .hasSize(1)
                .first()
                .matches(s -> s.getAttribute().equals("start"))
                .matches(s -> SortDirection.ASC == s.getSort());
    }

    @Test
    void testSortUsingVariable() {
        DataFetchingEnvironment env = mockDataFetchingEnvironment(singletonList(
                Argument.newArgument().name("orderBy").value(
                        VariableReference.newVariableReference().name("orderBy").build()).build()),
                singletonMap("orderBy", singletonMap("flight", singletonMap("start", "ASC"))));

        assertThat(new GraphQLQueryOrderByParser().apply(env))
                .hasSize(1)
                .first()
                .matches(s -> s.getAttribute().equals("flight.start"))
                .matches(s -> SortDirection.ASC == s.getSort());
    }

    @Test
    void testSortArgumentOrder() {
        DataFetchingEnvironment env = mockDataFetchingEnvironment(singletonList(
                Argument.newArgument().name("orderBy").value(
                        ObjectValue.newObjectValue()
                                .objectFields(
                                        Arrays.asList(
                                                ObjectField.newObjectField().name("start").value(EnumValue.newEnumValue("ASC").build()).build(),
                                                ObjectField.newObjectField().name("end").value(EnumValue.newEnumValue("DESC").build()).build()))
                                .build()

                ).build()), emptyMap());

        assertThat(new GraphQLQueryOrderByParser().apply(env))
                .hasSize(2)
                .containsExactly(
                        orderBy("start", SortDirection.ASC),
                        orderBy("end", SortDirection.DESC));
    }

    @Test
    void testSortArgumentUsingChildEntity() {
        DataFetchingEnvironment env = mockDataFetchingEnvironment(singletonList(
                Argument.newArgument().name("orderBy").value(
                        ObjectValue.newObjectValue()
                                .objectFields(
                                        Arrays.asList(
                                                ObjectField.newObjectField().name("nodes").value(
                                                        ObjectValue.newObjectValue().objectField(
                                                                ObjectField.newObjectField().name("name").value(EnumValue.newEnumValue("DESC").build()).build()).build())
                                                        .build(),
                                                ObjectField.newObjectField().name("start").value(EnumValue.newEnumValue("ASC").build()).build()))
                                .build()

                ).build()), emptyMap());

        assertThat(new GraphQLQueryOrderByParser().apply(env))
                .hasSize(2)
                .containsExactly(
                        orderBy("nodes.name", SortDirection.DESC),
                        orderBy("start", SortDirection.ASC));
    }
}
