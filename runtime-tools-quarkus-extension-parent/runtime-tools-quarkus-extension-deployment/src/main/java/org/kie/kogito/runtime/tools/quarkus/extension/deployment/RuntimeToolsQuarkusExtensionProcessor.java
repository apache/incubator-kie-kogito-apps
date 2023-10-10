package org.kie.kogito.runtime.tools.quarkus.extension.deployment;

import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;

class RuntimeToolsQuarkusExtensionProcessor {

    private static final String FEATURE = "kogito-processes-devui";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }
}
