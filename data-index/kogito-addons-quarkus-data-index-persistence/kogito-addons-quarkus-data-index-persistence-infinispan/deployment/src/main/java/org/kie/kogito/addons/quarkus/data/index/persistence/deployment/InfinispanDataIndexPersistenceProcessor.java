package org.kie.kogito.addons.quarkus.data.index.persistence.deployment;

import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.nativeimage.NativeImageResourceBuildItem;
import io.quarkus.deployment.pkg.steps.NativeOrNativeSourcesBuild;

public class InfinispanDataIndexPersistenceProcessor extends AbstractKogitoAddonsQuarkusDataIndexPersistenceProcessor {

    private static final String FEATURE = "kogito-addons-quarkus-data-index-persistence-infinispan";

    @BuildStep
    public FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep(onlyIf = NativeOrNativeSourcesBuild.class)
    public void infinispanNativeResources(BuildProducer<NativeImageResourceBuildItem> resource) {
        resource.produce(new NativeImageResourceBuildItem("META-INF/kogito-index.proto"));
        resource.produce(new NativeImageResourceBuildItem("META-INF/kogito-types.proto"));
    }
}
