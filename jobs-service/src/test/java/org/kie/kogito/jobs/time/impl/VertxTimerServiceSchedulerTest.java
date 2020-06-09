///*
// *  Copyright 2020 Red Hat, Inc. and/or its affiliates.
// *
// *  Licensed under the Apache License, Version 2.0 (the "License");
// *  you may not use this file except in compliance with the License.
// *  You may obtain a copy of the License at
// *
// *        http://www.apache.org/licenses/LICENSE-2.0
// *
// *  Unless required by applicable law or agreed to in writing, software
// *  distributed under the License is distributed on an "AS IS" BASIS,
// *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// *  See the License for the specific language governing permissions and
// *  limitations under the License.
// */
//
//package org.kie.kogito.jobs.time.impl;
//
//import java.time.Duration;
//import java.time.ZonedDateTime;
//import java.util.Date;
//
//import org.junit.Ignore;
//import org.junit.jupiter.api.Test;
//import org.kie.kogito.jobs.service.refactoring.job.HttpJob;
//import org.kie.kogito.jobs.service.refactoring.job.HttpJobContext;
//import org.kie.kogito.jobs.service.refactoring.job.JobDetails;
//import org.kie.kogito.jobs.service.refactoring.vertx.VertxTimerServiceScheduler;
//import org.kie.kogito.timer.TimerService;
//import org.kie.kogito.timer.impl.CronTrigger;
//import org.kie.kogito.timer.impl.IntervalTrigger;
//import org.kie.kogito.timer.impl.JDKTimerService;
//
//class VertxTimerServiceSchedulerTest {
//
//    TimerService timerService = new JDKTimerService();
//
//    @Ignore
//    @Test
//    public void testScheduler() throws InterruptedException {
//        CronTrigger trigger = new CronTrigger(0,
//                                              Date.from(ZonedDateTime.now().plusSeconds(1).toInstant()),
//                                              Date.from(ZonedDateTime.now().plusHours(1).toInstant()),
//                                              10,
//                                              "0/5 * * * * ?",
//                                              null,
//                                              null);
//
//        IntervalTrigger intervalTrigger = new IntervalTrigger(timerService.getCurrentTime(), new Date(), null, 100,
//                                                              10, 10, null, null);
//
//        timerService.scheduleJob(new HttpJob(),
//                                 new HttpJobContext(JobDetails.builder().build()),
//                                 trigger);
//
//        Thread.sleep(Duration.ofSeconds(150).toMillis());
//    }
//
//    @Ignore
//    @Test
//    public void testSchedulerVertx() throws InterruptedException {
//        timerService = new VertxTimerServiceScheduler();
//        CronTrigger trigger = new CronTrigger(0,
//                                              Date.from(ZonedDateTime.now().plusSeconds(1).toInstant()),
//                                              Date.from(ZonedDateTime.now().plusHours(1).toInstant()),
//                                              100,
//                                              "0/1 * * * * ?",
//                                              null,
//                                              null);
//
//        IntervalTrigger intervalTrigger = new IntervalTrigger(timerService.getCurrentTime(), new Date(), null, 100,
//                                                              10, 10, null, null);
//
//        timerService.scheduleJob(new HttpJob(),
//                                 new HttpJobContext(JobDetails.builder().build()),
//                                 trigger);
//
//        Thread.sleep(Duration.ofSeconds(150).toMillis());
//    }
//}