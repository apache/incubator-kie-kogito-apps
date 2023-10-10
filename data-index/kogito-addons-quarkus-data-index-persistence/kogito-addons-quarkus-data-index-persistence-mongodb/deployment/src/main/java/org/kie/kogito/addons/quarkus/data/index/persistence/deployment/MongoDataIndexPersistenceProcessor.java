package org.kie.kogito.addons.quarkus.data.index.persistence.deployment;

import org.kie.kogito.index.mongodb.model.ProcessDefinitionEntity;
import org.kie.kogito.index.mongodb.model.ProcessInstanceEntity;
import org.kie.kogito.index.mongodb.model.UserTaskInstanceEntity;

import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.nativeimage.ReflectiveHierarchyBuildItem;
import io.quarkus.deployment.pkg.steps.NativeOrNativeSourcesBuild;

public class MongoDataIndexPersistenceProcessor extends AbstractKogitoAddonsQuarkusDataIndexPersistenceProcessor {

    private static final String FEATURE = "kogito-addons-quarkus-data-index-persistence-mongodb";

    @BuildStep
    public FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep(onlyIf = NativeOrNativeSourcesBuild.class)
    public void mongoNativeResources(BuildProducer<ReflectiveHierarchyBuildItem> reflectiveHierarchyClass) {
        reflectiveHierarchy(ProcessDefinitionEntity.class, reflectiveHierarchyClass);
        reflectiveHierarchy(ProcessInstanceEntity.class, reflectiveHierarchyClass);
        reflectiveHierarchy(UserTaskInstanceEntity.class, reflectiveHierarchyClass);
    }

}
