package org.kie.kogito.swf.tools.deployment;

import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;

class ServerlessWorkflowQuarkusExtensionProcessor {

    private static final String FEATURE = "kogito-serverless-workflow-devui";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }
}
