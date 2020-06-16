/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
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

package org.kie.kogito.trusty.service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.kie.kogito.persistence.api.Storage;
import org.kie.kogito.persistence.api.query.AttributeFilter;
import org.kie.kogito.persistence.api.query.QueryFilterFactory;
import org.kie.kogito.trusty.storage.api.TrustyStorageService;
import org.kie.kogito.trusty.storage.api.model.Decision;
import org.kie.kogito.trusty.storage.api.model.Execution;

@ApplicationScoped
public class TrustyService implements ITrustyService {

    @Inject
    TrustyStorageService storageService;

    @Override
    public List<Execution> getExecutionHeaders(OffsetDateTime from, OffsetDateTime to, int limit, int offset, String prefix) {
        Storage<String, Decision> storage = storageService.getDecisionsStorage();
        List<AttributeFilter<?>> filters = new ArrayList<>();
        filters.add(QueryFilterFactory.like("executionId", prefix + "*"));
        filters.add(QueryFilterFactory.greaterThanEqual("executionTimestamp", from.toInstant().toEpochMilli()));
        filters.add(QueryFilterFactory.lessThanEqual("executionTimestamp", to.toInstant().toEpochMilli()));
        return new ArrayList<>(storage.query().limit(limit).offset(offset).filter(filters).execute());
    }

    @Override
    public void storeDecision(String executionId, Decision decision) {
        Storage<String, Decision> storage = storageService.getDecisionsStorage();
        storage.put(executionId, decision);
    }
}
