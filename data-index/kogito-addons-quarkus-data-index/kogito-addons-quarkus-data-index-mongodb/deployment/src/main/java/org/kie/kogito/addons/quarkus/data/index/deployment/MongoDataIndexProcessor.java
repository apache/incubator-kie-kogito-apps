package org.kie.kogito.addons.quarkus.data.index.deployment;

import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;

public class MongoDataIndexProcessor extends AbstractKogitoAddonsQuarkusDataIndexProcessor {

    private static final String FEATURE = "kogito-addons-quarkus-data-index-mongodb";

    @BuildStep
    public FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

}
