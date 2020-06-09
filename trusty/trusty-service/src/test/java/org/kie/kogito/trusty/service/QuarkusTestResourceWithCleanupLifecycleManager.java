package org.kie.kogito.trusty.service;

import java.util.Arrays;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

public interface QuarkusTestResourceWithCleanupLifecycleManager extends QuarkusTestResourceLifecycleManager {

    @Override
    default void inject(Object testInstance) {
        QuarkusTestResource[] testResourceAnnotations = testInstance.getClass().getAnnotationsByType(QuarkusTestResource.class);
        if (testResourceAnnotations.length > 0) {
            if (Arrays.stream(testResourceAnnotations)
                    .anyMatch(it -> getClass().isAssignableFrom(it.value()))) {
                cleanup();
            }
        }
    }

    void cleanup();
}