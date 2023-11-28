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
package org.kie.kogito.jobs.embedded;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.kie.kogito.jobs.JobsService;
import org.kie.kogito.jobs.ProcessInstanceJobDescription;
import org.kie.kogito.jobs.ProcessJobDescription;
import org.kie.kogito.jobs.api.JobCallbackResourceDef;
import org.kie.kogito.jobs.service.adapter.JobDetailsAdapter;
import org.kie.kogito.jobs.service.api.Job;
import org.kie.kogito.jobs.service.model.JobDetails;
import org.kie.kogito.jobs.service.model.JobStatus;
import org.kie.kogito.jobs.service.scheduler.ReactiveJobScheduler;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class EmbeddedJobsService implements JobsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmbeddedJobsService.class);

    @Inject
    ReactiveJobScheduler scheduler;

    public EmbeddedJobsService() {
        LOGGER.info("Starting Embedded Job Service");
    }

    @Override
    public String scheduleProcessJob(ProcessJobDescription description) {
        LOGGER.debug("ScheduleProcessJob: {} not supported", description);
        return null;
    }

    private class BlockingJobSubscriber implements Subscriber<JobDetails> {

        private CountDownLatch latch;
        private String outcome;
        private Throwable exception;

        public BlockingJobSubscriber() {
            latch = new CountDownLatch(1);
        }

        @Override
        public void onSubscribe(Subscription s) {
            s.request(1L);
        }

        @Override
        public void onNext(JobDetails t) {
            LOGGER.info("BlockingJobSubscriber::onNext {}", t);
            outcome = t.getId();
        }

        @Override
        public void onError(Throwable t) {
            exception = t;
            LOGGER.info("BlockingJobSubscriber::onError {}", t);
            latch.countDown();
        }

        @Override
        public void onComplete() {
            LOGGER.info("BlockingJobSubscriber::onComplete");
            latch.countDown();
        }

        public String get() {
            try {
                latch.await();
                if (exception != null) {
                    throw new RuntimeException(exception);
                }
                LOGGER.info("BlockingJobSubscriber::get {}", outcome);
                return outcome;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    @Override
    public String scheduleProcessInstanceJob(ProcessInstanceJobDescription description) {
        LOGGER.info("Embedded ScheduleProcessJob: {}", description);
        Job job = Job.builder()
                .id(description.id())
                .correlationId(description.id())
                .recipient(new InVMRecipient(new InVMPayloadData(description)))
                .schedule(JobCallbackResourceDef.buildSchedule(description))
                .build();

        JobDetails jobDetails = JobDetailsAdapter.from(job);
        BlockingJobSubscriber subscriber = new BlockingJobSubscriber();
        scheduler.schedule(jobDetails).subscribe(subscriber);
        String outcome = subscriber.get();
        LOGGER.info("Embedded ScheduleProcessJob: {} scheduled", outcome);
        return outcome;
    }

    @Override
    public boolean cancelJob(String jobId) {
        try {
            LOGGER.info("Embedded cancelJob: {}", jobId);
            return JobStatus.CANCELED.equals(scheduler.cancel(jobId).toCompletableFuture().get().getStatus());
        } catch (InterruptedException | ExecutionException e) {
            return false;
        }
    }

}
