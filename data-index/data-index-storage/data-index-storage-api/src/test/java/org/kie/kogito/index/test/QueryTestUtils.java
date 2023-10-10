package org.kie.kogito.index.test;

import java.util.List;
import java.util.function.BiConsumer;

import com.fasterxml.jackson.databind.node.ObjectNode;

import static org.assertj.core.api.Assertions.assertThat;

public class QueryTestUtils {

    public static <V> BiConsumer<List<V>, String[]> assertWithIdInOrder() {
        return (instances, ids) -> assertThat(instances).hasSize(ids == null ? 0 : ids.length).extracting("id").containsExactly(ids);
    }

    public static <V> BiConsumer<List<V>, String[]> assertWithId() {
        return (instances, ids) -> assertThat(instances).hasSize(ids == null ? 0 : ids.length).extracting("id").containsExactlyInAnyOrder(ids);
    }

    public static BiConsumer<List<String>, String[]> assertWithStringInOrder() {
        return (instances, ids) -> assertThat(instances).hasSize(ids == null ? 0 : ids.length).containsExactly(ids);
    }

    public static BiConsumer<List<String>, String[]> assertWithString() {
        return (instances, ids) -> assertThat(instances).hasSize(ids == null ? 0 : ids.length).containsExactlyInAnyOrder(ids);
    }

    public static BiConsumer<List<ObjectNode>, String[]> assertWithObjectNodeInOrder() {
        return (instances, ids) -> assertThat(instances).hasSize(ids == null ? 0 : ids.length).extracting(n -> n.get("id").asText()).containsExactly(ids);
    }

    public static BiConsumer<List<ObjectNode>, String[]> assertWithObjectNode() {
        return (instances, ids) -> assertThat(instances).hasSize(ids == null ? 0 : ids.length).extracting(n -> n.get("id").asText()).containsExactlyInAnyOrder(ids);
    }
}
