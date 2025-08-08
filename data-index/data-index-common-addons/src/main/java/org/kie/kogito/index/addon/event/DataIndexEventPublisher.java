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
package org.kie.kogito.index.addon.event;

import java.util.Collection;

import org.kie.kogito.event.DataEvent;
import org.kie.kogito.event.EventPublisher;
import org.kie.kogito.index.service.IndexingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class DataIndexEventPublisher implements EventPublisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataIndexEventPublisher.class);

    private final IndexingService indexingService;

    @Inject
    public DataIndexEventPublisher(IndexingService indexingService) {
        this.indexingService = indexingService;
    }

    @Override
    @Transactional
    public void publish(DataEvent<?> event) {
        LOGGER.debug("Sending event to embedded data index: {}", event);
        indexingService.indexDataEvent(event);
    }

    @Override
    @Transactional
    public void publish(Collection<DataEvent<?>> events) {
        LOGGER.debug("Sending batch event to embedded data index: {}", events);
        indexingService.indexDataEvent(events);
    }
}
