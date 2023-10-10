package org.kie.kogito.addons.quarkus.data.index.deployment;

import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;

public class PostgreSQLDataIndexProcessor extends AbstractKogitoAddonsQuarkusDataIndexProcessor {

    private static final String FEATURE = "kogito-addons-quarkus-data-index-postgresql";

    @BuildStep
    public FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

}
