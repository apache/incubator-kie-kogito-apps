/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.kie.kogito.addons.quarkus.jobs.service.embedded.deployment;

import java.util.List;

import org.jboss.jandex.DotName;
import org.jboss.jandex.IndexView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.quarkus.arc.deployment.ExcludedTypeBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.builditem.CombinedIndexBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.SystemPropertyBuildItem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.kie.kogito.addons.quarkus.jobs.service.embedded.stream.EventPublisherJobStreams.DATA_INDEX_EVENT_PUBLISHER;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class KogitoAddonsQuarkusJobsServiceEmbeddedProcessorTest {

    private KogitoAddonsQuarkusJobsServiceEmbeddedProcessor processor;
    @Mock
    private BuildProducer<SystemPropertyBuildItem> systemPropertyBuildItemBuildProducer;
    @Captor
    private ArgumentCaptor<SystemPropertyBuildItem> systemPropertyBuildItemCaptor;
    @Mock
    private BuildProducer<ExcludedTypeBuildItem> excludedBeansBuildProducer;
    @Captor
    private ArgumentCaptor<ExcludedTypeBuildItem> excludedTypeBuildItemCaptor;
    @Mock
    private IndexView indexView;
    @Mock
    private IndexView computingIndexView;

    @BeforeEach
    void setUp() {
        processor = new KogitoAddonsQuarkusJobsServiceEmbeddedProcessor();
    }

    @Test
    void feature() {
        FeatureBuildItem feature = processor.feature();
        assertThat(feature.getName()).isEqualTo("kogito-addons-quarkus-jobs-service-embedded");
    }

    @Test
    void buildConfiguration() {
        processor.buildConfiguration(systemPropertyBuildItemBuildProducer);
        verify(systemPropertyBuildItemBuildProducer, times(1)).produce(systemPropertyBuildItemCaptor.capture());
        List<SystemPropertyBuildItem> items = systemPropertyBuildItemCaptor.getAllValues();
        assertThat(items)
                .anyMatch(item -> "datasource.name".equals(item.getKey()) && ("jobs_service").equals(item.getValue()));
    }

    @Test
    void excludeEventPublisherJobStreams() {
        CombinedIndexBuildItem combinedIndexBuildItem = new CombinedIndexBuildItem(indexView, computingIndexView);
        DotName className = DotName.createSimple(DATA_INDEX_EVENT_PUBLISHER);
        doReturn(null).when(indexView).getClassByName(className);
        processor.excludeEventPublisherJobStreams(combinedIndexBuildItem, excludedBeansBuildProducer);
        verify(indexView).getClassByName(className);
        verify(excludedBeansBuildProducer).produce(excludedTypeBuildItemCaptor.capture());
        assertThat(excludedTypeBuildItemCaptor.getValue()).isNotNull();
        assertThat(excludedTypeBuildItemCaptor.getValue().getMatch()).isEqualTo(DATA_INDEX_EVENT_PUBLISHER);
    }
}
