package org.kie.kogito.addons.quarkus.data.index.persistence.deployment;

import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.nativeimage.NativeImageResourcePatternsBuildItem;
import io.quarkus.deployment.pkg.steps.NativeOrNativeSourcesBuild;

public class PostgreSQLDataIndexPersistenceProcessor extends AbstractKogitoAddonsQuarkusDataIndexPersistenceProcessor {

    private static final String FEATURE = "kogito-addons-quarkus-data-index-persistence-postgresql";

    @BuildStep
    public FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep(onlyIf = NativeOrNativeSourcesBuild.class)
    public void addNativeResources(BuildProducer<NativeImageResourcePatternsBuildItem> resources) {
        resources.produce(new NativeImageResourcePatternsBuildItem.Builder().includeGlob("sql/*.sql").build());
    }

}
