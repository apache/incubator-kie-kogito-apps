package org.kie.kogito.addons.quarkus.data.index.persistence.deployment;

import org.jboss.jandex.DotName;
import org.jboss.jandex.Type;
import org.kie.kogito.quarkus.addons.common.deployment.KogitoCapability;
import org.kie.kogito.quarkus.addons.common.deployment.OneOfCapabilityKogitoAddOnProcessor;

import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.builditem.nativeimage.ReflectiveHierarchyBuildItem;

public abstract class AbstractKogitoAddonsQuarkusDataIndexPersistenceProcessor extends OneOfCapabilityKogitoAddOnProcessor {

    AbstractKogitoAddonsQuarkusDataIndexPersistenceProcessor() {
        super(KogitoCapability.SERVERLESS_WORKFLOW, KogitoCapability.PROCESSES);
    }

    protected void reflectiveHierarchy(Class<?> clazz, BuildProducer<ReflectiveHierarchyBuildItem> reflectiveHierarchyClass) {
        DotName dotName = DotName.createSimple(clazz.getName());
        Type type = Type.create(dotName, Type.Kind.CLASS);
        reflectiveHierarchyClass.produce(new ReflectiveHierarchyBuildItem.Builder().type(type).build());
    }
}
