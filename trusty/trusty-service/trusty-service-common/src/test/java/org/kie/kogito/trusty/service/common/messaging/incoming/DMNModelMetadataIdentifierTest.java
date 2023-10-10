package org.kie.kogito.trusty.service.common.messaging.incoming;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.kie.kogito.trusty.storage.api.model.decision.DMNModelMetadata;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DMNModelMetadataIdentifierTest {

    private static Stream<Arguments> provideParametersForModelIdCreator() {
        return Stream.of(
                Arguments.of(null, null, null, "name", "namespace", "name:namespace"),
                Arguments.of("ignore", null, null, "name", "namespace", "name:namespace"),
                Arguments.of(null, "ignore", null, "name", "namespace", "name:namespace"),
                Arguments.of(null, null, "ignore", "name", "namespace", "name:namespace"),
                Arguments.of(null, null, null, "name", null, "name:"),
                Arguments.of(null, null, null, null, "namespace", ":namespace"));
    }

    @ParameterizedTest
    @MethodSource("provideParametersForModelIdCreator")
    void isBlank_ShouldReturnTrueForNullOrBlankStrings(final String groupId,
            final String artifactId,
            final String version,
            final String name,
            final String namespace,
            final String expected) {
        DMNModelMetadata modelIdentifier = new DMNModelMetadata(groupId,
                artifactId,
                version,
                "dmnVersion",
                name,
                namespace);
        assertEquals(expected, modelIdentifier.getIdentifier());
    }
}
