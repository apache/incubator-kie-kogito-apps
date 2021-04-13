/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
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

package org.kie.kogito.taskassigning.service.event;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BufferedTaskAssigningServiceEventConsumerTest {

    private BufferedTaskAssigningServiceEventConsumer taskAssigningServiceEventConsumer;

    @Mock
    private Consumer<List<DataEvent<?>>> consumer;

    @Captor
    private ArgumentCaptor<List<DataEvent<?>>> eventsCaptor;

    @Mock
    private TaskDataEvent event1;

    @Mock
    private UserDataEvent event2;

    @BeforeEach
    void setUp() {
        taskAssigningServiceEventConsumer = new BufferedTaskAssigningServiceEventConsumer();
        taskAssigningServiceEventConsumer.setConsumer(consumer);
    }

    @Test
    void pause() {
        taskAssigningServiceEventConsumer.pause();
        taskAssigningServiceEventConsumer.accept(event1);
        taskAssigningServiceEventConsumer.accept(event2);
        verify(consumer, never()).accept(anyList());
    }

    @Test
    void resume() {
        taskAssigningServiceEventConsumer.pause();
        taskAssigningServiceEventConsumer.accept(event1);
        taskAssigningServiceEventConsumer.accept(event2);
        verify(consumer, never()).accept(anyList());
        taskAssigningServiceEventConsumer.resume();
        verify(consumer).accept(eventsCaptor.capture());
        assertThat(eventsCaptor.getValue()).isNotNull();
        assertThat(eventsCaptor.getValue()).containsExactlyElementsOf(Arrays.asList(event1, event2));
    }

    @Test
    void pollEvents() {
        taskAssigningServiceEventConsumer.pause();
        taskAssigningServiceEventConsumer.accept(event1);
        taskAssigningServiceEventConsumer.accept(event2);
        verify(consumer, never()).accept(anyList());
        List<DataEvent<?>> events = taskAssigningServiceEventConsumer.pollEvents();
        assertThat(events)
                .hasSize(2)
                .containsExactlyElementsOf(Arrays.asList(event1, event2));
        taskAssigningServiceEventConsumer.resume();
        verify(consumer, never()).accept(anyList());
    }

    @Test
    void queuedEvents() {
        taskAssigningServiceEventConsumer.pause();
        taskAssigningServiceEventConsumer.accept(event1);
        taskAssigningServiceEventConsumer.accept(event2);
        assertThat(taskAssigningServiceEventConsumer.queuedEvents()).isEqualTo(2);
    }
}