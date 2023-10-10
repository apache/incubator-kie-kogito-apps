package org.kie.kogito.persistence.reporting.database;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SchemaGenerationActionTest {

    @ParameterizedTest
    @MethodSource("parameters")
    void testConversion(final String jpaText,
            final SchemaGenerationAction expectedStrategy,
            final String expectedJpaText) {
        final SchemaGenerationAction actualStrategy = SchemaGenerationAction.fromString(jpaText);
        assertEquals(expectedStrategy, actualStrategy);
        assertEquals(expectedJpaText, actualStrategy.getActionString());
    }

    private static Stream<Arguments> parameters() {
        return Stream.of(
                Arguments.of("none", SchemaGenerationAction.NONE, "none"),
                Arguments.of("NONE", SchemaGenerationAction.NONE, "none"),
                Arguments.of("create", SchemaGenerationAction.CREATE, "create"),
                Arguments.of("CREATE", SchemaGenerationAction.CREATE, "create"),
                Arguments.of("drop-and-create", SchemaGenerationAction.DROP_AND_CREATE, "drop-and-create"),
                Arguments.of("DROP-AND-CREATE", SchemaGenerationAction.DROP_AND_CREATE, "drop-and-create"),
                Arguments.of("drop", SchemaGenerationAction.DROP, "drop"),
                Arguments.of("DROP", SchemaGenerationAction.DROP, "drop"),
                Arguments.of("unknown", SchemaGenerationAction.NONE, "none"));
    }
}
