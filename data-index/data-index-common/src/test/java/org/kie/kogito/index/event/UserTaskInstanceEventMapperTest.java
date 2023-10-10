package org.kie.kogito.index.event;

import java.net.URI;
import java.util.UUID;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

class UserTaskInstanceEventMapperTest {

    private UserTaskInstanceEventMapper mapper = new UserTaskInstanceEventMapper();

    private static Stream<Arguments> provideEndpoint() {
        String pId = UUID.randomUUID().toString();
        String taskId = UUID.randomUUID().toString();
        return Stream.of(
                Arguments.of(URI.create("/travels"), pId, "task", taskId, format("/travels/%s/task/%s", pId, taskId)),
                Arguments.of(URI.create("http://localhost:8080/travels"), pId, "task", taskId, format("http://localhost:8080/travels/%s/task/%s", pId, taskId)),
                Arguments.of(URI.create("http://localhost:8080/orderItems"), pId, "Verify_order", taskId, format("http://localhost:8080/orderItems/%s/Verify_order/%s", pId, taskId)),
                Arguments.of(URI.create("/travels"), pId, "Apply for visa", taskId, format("/travels/%s/%s/%s", pId, "Apply%20for%20visa", taskId)));
    }

    @ParameterizedTest
    @MethodSource("provideEndpoint")
    void testUserTaskEndpoint(URI source, String pId, String taskName, String taskId, String expected) {
        assertThat(mapper.getEndpoint(source, pId, taskName, taskId)).isEqualTo(expected);
    }
}
