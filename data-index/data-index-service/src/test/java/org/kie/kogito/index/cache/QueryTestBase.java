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

package org.kie.kogito.index.cache;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.kie.kogito.index.model.ProcessInstance;
import org.kie.kogito.index.query.AttributeFilter;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.kie.kogito.index.TestUtils.getProcessInstance;
import static org.kie.kogito.index.model.ProcessInstanceState.ACTIVE;
import static org.kie.kogito.index.model.ProcessInstanceState.COMPLETED;
import static org.kie.kogito.index.query.QueryFilterFactory.between;
import static org.kie.kogito.index.query.QueryFilterFactory.contains;
import static org.kie.kogito.index.query.QueryFilterFactory.containsAll;
import static org.kie.kogito.index.query.QueryFilterFactory.containsAny;
import static org.kie.kogito.index.query.QueryFilterFactory.equalTo;
import static org.kie.kogito.index.query.QueryFilterFactory.greaterThan;
import static org.kie.kogito.index.query.QueryFilterFactory.greaterThanEqual;
import static org.kie.kogito.index.query.QueryFilterFactory.in;
import static org.kie.kogito.index.query.QueryFilterFactory.isNull;
import static org.kie.kogito.index.query.QueryFilterFactory.lessThan;
import static org.kie.kogito.index.query.QueryFilterFactory.lessThanEqual;
import static org.kie.kogito.index.query.QueryFilterFactory.notNull;

public class QueryTestBase {

    public static void setup(CacheService cacheService) {
        cacheService.getProcessInstancesCache().clear();
    }

    public static void testProcessInstanceQueries(CacheService cacheService) {
        String processId = "travels";
        String processInstanceId = UUID.randomUUID().toString();
        String subProcessId = processId + "_sub";
        String subProcessInstanceId = UUID.randomUUID().toString();
        ProcessInstance processInstance = getProcessInstance(processId, processInstanceId, ACTIVE.ordinal(), null, null);
        cacheService.getProcessInstancesCache().put(processInstanceId, processInstance);
        cacheService.getProcessInstancesCache().put(subProcessInstanceId, getProcessInstance(subProcessId, subProcessInstanceId, COMPLETED.ordinal(), processInstanceId, processId));

        queryAndAssert(cacheService, in("state", asList(ACTIVE.ordinal(), COMPLETED.ordinal())), processInstanceId, subProcessInstanceId);
        queryAndAssert(cacheService, equalTo("state", ACTIVE.ordinal()), processInstanceId);
        queryAndAssert(cacheService, greaterThan("state", ACTIVE.ordinal()), subProcessInstanceId);
        queryAndAssert(cacheService, greaterThanEqual("state", ACTIVE.ordinal()), processInstanceId, subProcessInstanceId);
        queryAndAssert(cacheService, lessThan("state", COMPLETED.ordinal()), processInstanceId);
        queryAndAssert(cacheService, lessThanEqual("state", COMPLETED.ordinal()), processInstanceId, subProcessInstanceId);
        queryAndAssert(cacheService, between("state", ACTIVE.ordinal(), COMPLETED.ordinal()), processInstanceId, subProcessInstanceId);
        queryAndAssert(cacheService, isNull("rootProcessInstanceId"), processInstanceId);
        queryAndAssert(cacheService, notNull("rootProcessInstanceId"), subProcessInstanceId);
        queryAndAssert(cacheService, in("id", asList(processInstanceId, subProcessInstanceId)), processInstanceId, subProcessInstanceId);
        queryAndAssert(cacheService, equalTo("rootProcessInstanceId", processInstanceId), subProcessInstanceId);
        queryAndAssert(cacheService, in("processId", asList(processId, subProcessId)), processInstanceId, subProcessInstanceId);
        queryAndAssert(cacheService, equalTo("processId", subProcessId), subProcessInstanceId);
        queryAndAssert(cacheService, contains("roles", "admin"), processInstanceId, subProcessInstanceId);
        queryAndAssert(cacheService, containsAny("roles", asList("admin", "kogito")), processInstanceId, subProcessInstanceId);
        queryAndAssert(cacheService, containsAll("roles", asList("admin", "kogito")));
        queryAndAssert(cacheService, isNull("roles"));
        queryAndAssert(cacheService, isNull("end"), processInstanceId);
        queryAndAssert(cacheService, lessThan("start", new Date().getTime()), processInstanceId, subProcessInstanceId);
        queryAndAssert(cacheService, lessThanEqual("start", new Date().getTime()), processInstanceId, subProcessInstanceId);
        queryAndAssert(cacheService, greaterThan("start", new Date().getTime()));
        queryAndAssert(cacheService, greaterThanEqual("start", new Date().getTime()));
        queryAndAssert(cacheService, equalTo("start", processInstance.getStart().toInstant().toEpochMilli()), processInstanceId);
    }

    private static void queryAndAssert(CacheService cacheService, AttributeFilter filter, String... ids) {
        List<ProcessInstance> instances = cacheService.getProcessInstancesCache().query().filter(singletonList(filter)).execute();
        assertThat(instances).hasSize(ids == null ? 0 : ids.length).extracting("id").containsExactlyInAnyOrder(ids);
    }
}
