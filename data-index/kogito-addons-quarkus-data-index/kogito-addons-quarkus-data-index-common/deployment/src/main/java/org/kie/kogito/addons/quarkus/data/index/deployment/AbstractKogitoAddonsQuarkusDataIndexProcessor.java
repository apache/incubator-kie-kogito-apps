/*
 * Copyright 2022 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.kogito.addons.quarkus.data.index.deployment;

import org.kie.kogito.index.config.DataIndexBuildConfig;
import org.kie.kogito.index.vertx.VertxGraphiQLSetup;
import org.kie.kogito.quarkus.addons.common.deployment.KogitoCapability;
import org.kie.kogito.quarkus.addons.common.deployment.OneOfCapabilityKogitoAddOnProcessor;

import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.arc.processor.DotNames;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.LaunchModeBuildItem;

abstract class AbstractKogitoAddonsQuarkusDataIndexProcessor extends OneOfCapabilityKogitoAddOnProcessor {

    private final String feature;

    AbstractKogitoAddonsQuarkusDataIndexProcessor(String feature) {
        super(KogitoCapability.SERVERLESS_WORKFLOW, KogitoCapability.PROCESSES);
        this.feature = feature;
    }

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(feature);
    }

    @BuildStep
    void processGraphiql(BuildProducer<AdditionalBeanBuildItem> additionalBean, LaunchModeBuildItem launchMode, DataIndexBuildConfig config) {
        if (shouldInclude(launchMode, config)) {
            additionalBean.produce(AdditionalBeanBuildItem.builder().addBeanClass(VertxGraphiQLSetup.class).setUnremovable().setDefaultScope(DotNames.APPLICATION_SCOPED).build());
        }
    }

    private static boolean shouldInclude(LaunchModeBuildItem launchMode, DataIndexBuildConfig config) {
        return launchMode.getLaunchMode().isDevOrTest() || config.graphqlUIEnabled;
    }
}
