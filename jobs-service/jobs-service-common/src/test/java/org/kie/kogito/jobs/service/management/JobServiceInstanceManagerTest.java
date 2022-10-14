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
package org.kie.kogito.jobs.service.management;

import java.time.OffsetDateTime;
import java.util.function.Function;

import javax.enterprise.event.Event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kie.kogito.jobs.service.model.JobServiceManagementInfo;
import org.kie.kogito.jobs.service.repository.JobServiceManagementRepository;
import org.kie.kogito.jobs.service.repository.impl.DefaultJobServiceManagementRepository;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.smallrye.reactive.messaging.kafka.KafkaConnector;
import io.vertx.mutiny.core.TimeoutStream;
import io.vertx.mutiny.core.Vertx;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class JobServiceInstanceManagerTest {

    @InjectMocks
    @Spy
    JobServiceInstanceManager tested;

    @Spy
    Vertx vertx = Vertx.vertx();

    @Spy
    JobServiceManagementRepository repository = new DefaultJobServiceManagementRepository();

    @Mock
    KafkaConnector kafkaConnector;

    @Mock
    Event<MessagingChangeEvent> messagingChangeEventEvent;

    @Captor
    ArgumentCaptor<JobServiceManagementInfo> infoCaptor;

    @Mock
    ShutdownEvent shutdownEvent;

    @Mock
    StartupEvent startupEvent;

    @BeforeEach
    void setUp() {
        tested.heartbeatExpirationInSeconds = 1;
        tested.leaderCheckIntervalInSeconds = 1;
        tested.heardBeatIntervalInSeconds = 1;
    }

    @Test
    void startup() {
        tested.startup(startupEvent);

        assertThat(tested.getCurrentInfo()).isNotNull();
        verify(tested, times(1)).tryBecomeLeader(infoCaptor.capture(), any(TimeoutStream.class), any(TimeoutStream.class));
        assertThat(infoCaptor.getValue()).isEqualTo(tested.getCurrentInfo());
        assertThat(tested.getHeartbeat()).isNotNull();
        assertThat(tested.getCheckLeader()).isNotNull();
    }

    @Test
    void onShutdown() {
        tested.startup(startupEvent);
        tested.onShutdown(shutdownEvent);

        verify(tested, times(1)).release(infoCaptor.capture());
        assertThat(infoCaptor.getValue()).isEqualTo(tested.getCurrentInfo());
        verify(repository, times(1)).set(new JobServiceManagementInfo());
    }

    @Test
    void tryBecomeLeaderSuccess() {
        JobServiceManagementInfo info = new JobServiceManagementInfo("id", "token", OffsetDateTime.now());
        ArgumentCaptor<Function<JobServiceManagementInfo, JobServiceManagementInfo>> updateFunction = ArgumentCaptor.forClass(Function.class);

        TimeoutStream checkLeader = vertx.timerStream(1);
        TimeoutStream heartbeat = vertx.timerStream(1);
        tested.tryBecomeLeader(info, checkLeader, heartbeat).await().indefinitely();
        verify(repository).getAndUpdate(anyString(), updateFunction.capture());
        assertThat(tested.isLeader()).isTrue();
    }

    @Test
    void tryBecomeLeaderFail() {
        JobServiceManagementInfo info = new JobServiceManagementInfo("id", "token", OffsetDateTime.now());
        JobServiceManagementInfo info2 = new JobServiceManagementInfo("id2", "token2", OffsetDateTime.now());
        repository.set(info).await().indefinitely();
        ArgumentCaptor<Function<JobServiceManagementInfo, JobServiceManagementInfo>> updateFunction = ArgumentCaptor.forClass(Function.class);

        TimeoutStream checkLeader = vertx.timerStream(1);
        TimeoutStream heartbeat = vertx.timerStream(1);
        tested.tryBecomeLeader(info2, checkLeader, heartbeat).await().indefinitely();
        verify(repository).getAndUpdate(anyString(), updateFunction.capture());
        assertThat(tested.isLeader()).isFalse();
    }

    @Test
    void heartbeatNotLeader() {
        JobServiceManagementInfo info = new JobServiceManagementInfo("id", "token", OffsetDateTime.now());
        tested.heartbeat(info).await().indefinitely();
        verify(repository, never()).heartbeat(info);
    }

    @Test
    void heartbeatLeader() {
        tested.startup(startupEvent);
        tested.heartbeat(tested.getCurrentInfo()).await().indefinitely();
        verify(repository).heartbeat(tested.getCurrentInfo());
    }
}
