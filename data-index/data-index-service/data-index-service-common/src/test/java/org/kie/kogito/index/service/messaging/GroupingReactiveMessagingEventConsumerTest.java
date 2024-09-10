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

package org.kie.kogito.index.service.messaging;

import java.util.Arrays;
import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kie.kogito.event.DataEvent;
import org.kie.kogito.event.process.ProcessDefinitionDataEvent;
import org.kie.kogito.event.process.ProcessInstanceDataEvent;
import org.kie.kogito.event.usertask.UserTaskInstanceDataEvent;
import org.kie.kogito.index.event.KogitoJobCloudEvent;
import org.kie.kogito.index.model.Job;
import org.kie.kogito.index.service.IndexingService;
import org.kie.kogito.index.storage.DataIndexStorageService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import io.smallrye.mutiny.Uni;

import jakarta.enterprise.event.Event;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroupingReactiveMessagingEventConsumerTest {

    @Mock
    DataIndexStorageService dataIndexStorageService;

    @Mock
    IndexingService indexingService;

    @Mock
    Event<DataEvent<?>> eventPublisher;

    @InjectMocks
    GroupingReactiveMessagingEventConsumer consumer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testOnProcessInstanceEvent() {
        // Arrange
        ProcessInstanceDataEvent<?> event1 = mock(ProcessInstanceDataEvent.class);
        ProcessInstanceDataEvent<?> event2 = mock(ProcessInstanceDataEvent.class);
        Collection<ProcessInstanceDataEvent<?>> events = Arrays.asList(event1, event2);

        // Act
        Uni<Void> result = consumer.onProcessInstanceEvent(events);

        // Assert
        verify(indexingService, times(1)).indexProcessInstanceEvent(event1);
        verify(indexingService, times(1)).indexProcessInstanceEvent(event2);
        verify(eventPublisher, times(1)).fire(event1);
        verify(eventPublisher, times(1)).fire(event2);
        result.await().indefinitely(); // Ensure Uni completes without error
    }

    @Test
    void testOnUserTaskInstanceEvent() {
        // Arrange
        UserTaskInstanceDataEvent<?> event1 = mock(UserTaskInstanceDataEvent.class);
        UserTaskInstanceDataEvent<?> event2 = mock(UserTaskInstanceDataEvent.class);
        Collection<UserTaskInstanceDataEvent<?>> events = Arrays.asList(event1, event2);

        // Act
        Uni<Void> result = consumer.onUserTaskInstanceEvent(events);

        // Assert
        verify(indexingService, times(1)).indexUserTaskInstanceEvent(event1);
        verify(indexingService, times(1)).indexUserTaskInstanceEvent(event2);
        verify(eventPublisher, times(1)).fire(event1);
        verify(eventPublisher, times(1)).fire(event2);
        result.await().indefinitely(); // Ensure Uni completes without error
    }

    @Test
    void testOnJobEvent() {
        // Arrange
        KogitoJobCloudEvent event = mock(KogitoJobCloudEvent.class);
        Job mockJob = mock(Job.class); // Mock the Job object
        when(event.getData()).thenReturn(mockJob);

        // Act
        Uni<Void> result = consumer.onJobEvent(event);

        // Await the result before assertions
        result.await().indefinitely(); // Ensure Uni completes without error

        // Assert
        verify(indexingService, times(1)).indexJob(mockJob); // Perform the verification after Uni completes
    }

    @Test
    void testOnProcessDefinitionDataEvent() {
        // Arrange
        ProcessDefinitionDataEvent event1 = mock(ProcessDefinitionDataEvent.class);
        ProcessDefinitionDataEvent event2 = mock(ProcessDefinitionDataEvent.class);
        Collection<ProcessDefinitionDataEvent> events = Arrays.asList(event1, event2);

        // Act
        Uni<Void> result = consumer.onProcessDefinitionDataEvent(events);

        // Assert
        verify(indexingService, times(1)).indexProcessDefinition(event1);
        verify(indexingService, times(1)).indexProcessDefinition(event2);
        verify(eventPublisher, times(1)).fire(event1);
        verify(eventPublisher, times(1)).fire(event2);
        result.await().indefinitely(); // Ensure Uni completes without error
    }

    @Test
    void testErrorHandlingInOnProcessInstanceEvent() {
        // Arrange
        ProcessInstanceDataEvent<?> event = mock(ProcessInstanceDataEvent.class);
        Collection<ProcessInstanceDataEvent<?>> events = Arrays.asList(event);
        doThrow(new RuntimeException("On purpose! Indexing failed")).when(indexingService).indexProcessInstanceEvent(event);

        // Act
        Uni<Void> result = consumer.onProcessInstanceEvent(events);

        // Assert
        verify(indexingService, times(1)).indexProcessInstanceEvent(event);
        verify(eventPublisher, never()).fire(event); // Event should not be published if indexing fails
        result.await().indefinitely(); // Ensure Uni completes without error
    }

    @Test
    void testErrorHanlingInOnUserTaskInstanceEvent() {
        // Arrange
        UserTaskInstanceDataEvent<?> event = mock(UserTaskInstanceDataEvent.class);
        Collection<UserTaskInstanceDataEvent<?>> events = Arrays.asList(event);
        doThrow(new RuntimeException("On purpose! Indexing failed")).when(indexingService).indexUserTaskInstanceEvent(event);

        // Act
        Uni<Void> result = consumer.onUserTaskInstanceEvent(events);

        // Assert
        verify(indexingService, times(1)).indexUserTaskInstanceEvent(event);
        verify(eventPublisher, never()).fire(event); // Event should not be published if indexing fails
        result.await().indefinitely(); // Ensure Uni completes without error
    }

    @Test
    void testErrorHandlingInOnProcessDefinitionDataEvent() {
        // Arrange
        ProcessDefinitionDataEvent event = mock(ProcessDefinitionDataEvent.class);
        Collection<ProcessDefinitionDataEvent> events = Arrays.asList(event);
        doThrow(new RuntimeException("On purpose! Indexing failed")).when(indexingService).indexProcessDefinition(event);

        // Act
        Uni<Void> result = consumer.onProcessDefinitionDataEvent(events);

        // Assert
        verify(indexingService, times(1)).indexProcessDefinition(event);
        verify(eventPublisher, never()).fire(event);
        result.await().indefinitely(); // Ensure Uni completes without error
    }
}
