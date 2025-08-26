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
package org.kie.kogito.app.jobs.integrations;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.kie.kogito.app.jobs.api.JobDescriptionMerger;
import org.kie.kogito.jobs.DurationExpirationTime;
import org.kie.kogito.jobs.ExpirationTime;
import org.kie.kogito.jobs.JobDescription;
import org.kie.kogito.jobs.service.utils.DateUtil;
import org.kie.kogito.timer.Trigger;
import org.kie.kogito.timer.impl.SimpleTimerTrigger;

public class JobDescriptionHelper {

    public static ExpirationTime toExpirationTime(Trigger trigger) {
        if (trigger instanceof SimpleTimerTrigger simpleTimerTrigger) {
            ZonedDateTime zoneDateTime = DateUtil.fromDate(simpleTimerTrigger.hasNextFireTime());
            ZonedDateTime now = DateUtil.now();
            Long delay = ChronoUnit.MILLIS.between(now, zoneDateTime);
            DurationExpirationTime time = DurationExpirationTime.repeat(Math.max(delay, 0L), simpleTimerTrigger.getPeriod(), simpleTimerTrigger.getRepeatCount());
            return time;
        }
        throw new IllegalArgumentException("this type of trigger is not supported " + trigger.getClass().getName());
    }

    public static JobDescription newJobDescription(JobDescription jobDescription, Trigger trigger) {
        List<JobDescriptionMerger> mergers = List.of(new ProcessInstanceJobDescriptionMerger(), new ProcessJobDescriptionMerger(), new UserTaskInstanceJobDescriptorMerger());
        return mergers.stream().filter(merger -> merger.accept(jobDescription)).map(merger -> merger.mergeTrigger(jobDescription, trigger)).findFirst().orElseThrow();
    }
}
