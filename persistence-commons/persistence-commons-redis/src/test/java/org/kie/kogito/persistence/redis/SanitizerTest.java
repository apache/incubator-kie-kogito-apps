package org.kie.kogito.persistence.redis;

import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class SanitizerTest {

    @ParameterizedTest
    @MethodSource("provideSanitizeTestCases")
    public void sanitizeTest(String input, String expected) {
        String sanitized = (String) Sanitizer.sanitize(input);
        Assertions.assertEquals(expected, sanitized);
    }

    private static Stream<Arguments> provideSanitizeTestCases() {
        return Stream.of(
                Arguments.of("Hello_Jacopo*", "Hello_Jacopo*"),
                Arguments.of("Hello-Jac-opo*", "Hello\\-Jac\\-opo*"),
                Arguments.of(",.<>{}[]\"':;!@#$%^&()-+=~ ", "\\,\\.\\<\\>\\{\\}\\[\\]\\\"\\'\\:\\;\\!\\@\\#\\$\\%\\^\\&\\(\\)\\-\\+\\=\\~\\ "));
    }
}
