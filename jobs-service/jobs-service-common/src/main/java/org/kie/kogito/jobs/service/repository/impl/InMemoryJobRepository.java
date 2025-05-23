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
package org.kie.kogito.jobs.service.repository.impl;

import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Stream;

import org.kie.kogito.jobs.service.model.JobDetails;
import org.kie.kogito.jobs.service.model.JobStatus;
import org.kie.kogito.jobs.service.repository.JobRepository;
import org.kie.kogito.jobs.service.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.arc.DefaultBean;

import jakarta.enterprise.context.ApplicationScoped;

import static org.kie.kogito.jobs.service.utils.ModelUtil.jobWithCreatedAndLastUpdate;

@DefaultBean
@ApplicationScoped
public class InMemoryJobRepository extends AbstractJobRepository implements JobRepository {

    private Logger LOGGER = LoggerFactory.getLogger(InMemoryJobRepository.class);

    private static ConcurrentMap<String, JobDetails> jobMap = new ConcurrentHashMap<>();

    @Override
    public JobDetails doSave(JobDetails job) {
        LOGGER.trace("do save in memmory job service {}", job);
        JobDetails savedJobDetails = jobMap.compute(job.getId(), (jobId, currentJobDetails) -> {
            return jobWithCreatedAndLastUpdate(currentJobDetails == null, job);
        });
        return savedJobDetails;
    }

    @Override
    public JobDetails get(String key) {
        return jobMap.get(key);
    }

    @Override
    public Boolean exists(String key) {
        return jobMap.containsKey(key);
    }

    @Override
    public JobDetails delete(String key) {
        return jobMap.remove(key);
    }

    @Override
    public List<JobDetails> findByStatusBetweenDates(ZonedDateTime fromFireTime,
            ZonedDateTime toFireTime,
            JobStatus[] status,
            SortTerm[] orderBy) {
        Stream<JobDetails> unsortedResult = jobMap.values()
                .stream()
                .filter(j -> matchStatusFilter(j, status))
                .filter(j -> matchFireTimeFilter(j, fromFireTime, toFireTime));
        List<JobDetails> result = orderBy == null || orderBy.length == 0 ? unsortedResult.toList() : unsortedResult.sorted(orderByComparator(orderBy)).toList();
        return result;
    }

    private static boolean matchStatusFilter(JobDetails job, JobStatus[] status) {
        if (status == null || status.length == 0) {
            return true;
        }
        return Stream.of(status).anyMatch(s -> job.getStatus() == s);
    }

    private static boolean matchFireTimeFilter(JobDetails job, ZonedDateTime fromFireTime, ZonedDateTime toFireTime) {
        ZonedDateTime fireTime = DateUtil.fromDate(job.getTrigger().hasNextFireTime());
        return (fireTime.isEqual(fromFireTime) || fireTime.isAfter(fromFireTime)) &&
                (fireTime.isEqual(toFireTime) || fireTime.isBefore(toFireTime));
    }

    private static Comparator<JobDetails> orderByComparator(SortTerm[] orderBy) {
        Comparator<JobDetails> comparator = createOrderByFieldComparator(orderBy[0]);
        for (int i = 1; i < orderBy.length; i++) {
            comparator = comparator.thenComparing(createOrderByFieldComparator(orderBy[i]));
        }
        return comparator;
    }

    private static Comparator<JobDetails> createOrderByFieldComparator(SortTerm field) {
        Comparator<JobDetails> comparator;
        switch (field.getField()) {
            case FIRE_TIME:
                comparator = Comparator.comparingLong(jobDetails -> {
                    Date nextFireTime = jobDetails.getTrigger().hasNextFireTime();
                    return nextFireTime != null ? nextFireTime.getTime() : Long.MIN_VALUE;
                });
                break;
            case CREATED:
                comparator = Comparator.comparingLong(jobDetails -> {
                    ZonedDateTime created = jobDetails.getCreated();
                    return created != null ? created.toInstant().toEpochMilli() : Long.MIN_VALUE;
                });
                break;
            case ID:
                comparator = Comparator.comparing(JobDetails::getId);
                break;
            default:
                throw new IllegalArgumentException("No comparator is defined for field: " + field.getField());
        }
        return field.isAsc() ? comparator : comparator.reversed();
    }

}
