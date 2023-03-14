/*
 * Copyright 2023 Red Hat, Inc. and/or its affiliates.
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
package org.kie.kogito.addons.quarkus.jobs.service.embedded.deployment;

import javax.sql.DataSource;

import org.jboss.jandex.AnnotationTarget;
import org.jboss.jandex.AnnotationValue;
import org.kie.kogito.quarkus.addons.common.deployment.KogitoCapability;
import org.kie.kogito.quarkus.addons.common.deployment.OneOfCapabilityKogitoAddOnProcessor;

import io.quarkus.arc.deployment.AnnotationsTransformerBuildItem;
import io.quarkus.arc.processor.AnnotationsTransformer;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.SystemPropertyBuildItem;
import io.quarkus.reactive.datasource.ReactiveDataSource;
import io.vertx.mutiny.sqlclient.SqlClient;

import static org.kie.kogito.addons.quarkus.jobs.service.embedded.runtime.DataSourceConfigSourceFactory.DATA_SOURCE_NAME;

class KogitoAddonsQuarkusJobsServiceEmbeddedProcessor extends OneOfCapabilityKogitoAddOnProcessor {

    private static final String FEATURE = "kogito-addons-quarkus-jobs-service-embedded";
    private static final String JOBS_SERVICE_URL = "kogito.jobs-service.url";
    private static final String SERVICE_URL = "kogito.service.url";

    KogitoAddonsQuarkusJobsServiceEmbeddedProcessor() {
        super(KogitoCapability.SERVERLESS_WORKFLOW, KogitoCapability.PROCESSES);
    }

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    void buildConfiguration(BuildProducer<SystemPropertyBuildItem> systemProperties) {
        systemProperties.produce(new SystemPropertyBuildItem(SERVICE_URL, "http://${quarkus.http.host}:${quarkus.http.port}"));
        systemProperties.produce(new SystemPropertyBuildItem(JOBS_SERVICE_URL, "${" + SERVICE_URL + "}"));
        systemProperties.produce(new SystemPropertyBuildItem("quarkus.flyway.\"" + DATA_SOURCE_NAME + "\".locations", "db/migration"));
        systemProperties.produce(new SystemPropertyBuildItem("quarkus.datasource.devservices.enabled", "false"));
        //systemProperties.produce(new SystemPropertyBuildItem("quarkus.flyway.locations", "db/empty" ));
    }

    @BuildStep
    AnnotationsTransformerBuildItem transformDataSource() {
        return new AnnotationsTransformerBuildItem(new AnnotationsTransformer() {

            public boolean appliesTo(org.jboss.jandex.AnnotationTarget.Kind kind) {
                return kind == AnnotationTarget.Kind.FIELD;
            }

            public void transform(TransformationContext context) {
                try {
                    if (!(context.getTarget().kind() == AnnotationTarget.Kind.FIELD)) {
                        return;
                    }
                    if (SqlClient.class.isAssignableFrom(Class.forName(context.getTarget().asField().type().name().toString()))) {
                        context.transform().add(ReactiveDataSource.class, AnnotationValue.createStringValue("value", DATA_SOURCE_NAME)).done();
                    } else if (DataSource.class.isAssignableFrom(Class.forName(context.getTarget().asField().type().name().toString()))) {
                        context.transform().add(io.quarkus.agroal.DataSource.class, AnnotationValue.createStringValue("value", DATA_SOURCE_NAME)).done();
                    }
                } catch (ClassNotFoundException exception) {
                    //throw new RuntimeException("Error adding DataSource annotation", exception);
                }
            }
        });
    }
}
