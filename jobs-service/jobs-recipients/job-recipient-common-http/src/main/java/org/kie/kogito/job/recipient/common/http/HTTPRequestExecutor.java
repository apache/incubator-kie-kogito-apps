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
package org.kie.kogito.job.recipient.common.http;

import java.net.URI;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.kie.kogito.job.recipient.common.http.converters.HttpConverters;
import org.kie.kogito.jobs.api.URIBuilder;
import org.kie.kogito.jobs.service.api.Recipient;
import org.kie.kogito.jobs.service.exception.JobExecutionException;
import org.kie.kogito.jobs.service.model.JobDetails;
import org.kie.kogito.jobs.service.model.JobExecutionResponse;
import org.kie.kogito.timer.impl.IntervalTrigger;
import org.kie.kogito.timer.impl.SimpleTimerTrigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;

import jakarta.ws.rs.core.Response;

public abstract class HTTPRequestExecutor<R extends Recipient<?>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(HTTPRequestExecutor.class);

    protected long timeout;

    protected Vertx vertx;

    protected WebClient client;

    protected ObjectMapper objectMapper;

    protected HTTPRequestExecutor() {
    }

    protected HTTPRequestExecutor(long timeout, Vertx vertx, ObjectMapper objectMapper) {
        this.timeout = timeout;
        this.vertx = vertx;
        this.objectMapper = objectMapper;
    }

    protected void initialize() {
        this.client = createClient();
    }

    /**
     * facilitates tests.
     */
    public WebClient createClient() {
        return WebClient.create(vertx);
    }

    public JobExecutionResponse execute(JobDetails jobDetails) {
        try {
            R recipient = getRecipient(jobDetails);
            String limit = getLimit(jobDetails);
            HTTPRequest request = buildRequest(recipient, limit);
            long requestTimeout = getTimeoutInMillis(jobDetails);
            HttpResponse<?> response = executeRequest(request, requestTimeout);
            JobExecutionResponse jobExecutionResponse = JobExecutionResponse.builder()
                    .message(response.bodyAsString())
                    .code(String.valueOf(response.statusCode()))
                    .now()
                    .jobId(jobDetails.getId())
                    .build();

            return this.handleResponse(jobExecutionResponse);
        } catch (Throwable unexpected) {
            LOGGER.error("Executing error for {}", jobDetails.getId(), unexpected);
            return null;
        }
    }

    protected <T extends JobExecutionResponse> T handleResponse(T response) {
        LOGGER.debug("Handle response {}", response);

        Integer code = Integer.valueOf(response.getCode());
        if (Response.Status.Family.SUCCESSFUL.equals(Response.Status.Family.familyOf(code))) {
            LOGGER.debug("Success executing job {}.", response);
            return response;
        } else {
            throw new JobExecutionException(response.getJobId(), "Response error when executing HTTP request for " + response);
        }

    }

    protected abstract R getRecipient(JobDetails job);

    protected abstract HTTPRequest buildRequest(R recipient, String limit);

    protected HttpResponse<Buffer> executeRequest(HTTPRequest request, long timeout) throws Exception {
        LOGGER.trace("Executing request {}", request);
        URI uri = URIBuilder.toURI(request.getUrl());
        HttpRequest<Buffer> clientRequest = client.request(HttpConverters.convertHttpMethod(request.getMethod()),
                uri.getPort(),
                uri.getHost(),
                uri.getPath()).timeout(timeout);
        clientRequest.queryParams().addAll(filterEntries(request.getQueryParams()));
        clientRequest.headers().addAll(filterEntries(request.getHeaders()));

        CompletionStage<HttpResponse<Buffer>> completionStage = null;
        if (request.getBody() != null) {
            completionStage = clientRequest.sendBuffer(buildBuffer(request.getBody())).toCompletionStage();
        } else {
            completionStage = clientRequest.send().toCompletionStage();
        }

        return completionStage.toCompletableFuture().get(timeout, TimeUnit.SECONDS);

    }

    protected Buffer buildBuffer(Object body) {
        if (body instanceof String) {
            return Buffer.buffer((String) body);
        } else if (body instanceof byte[]) {
            return Buffer.buffer(((byte[]) body));
        } else if (body instanceof JsonNode) {
            try {
                return Buffer.buffer(objectMapper.writeValueAsBytes(body));
            } catch (Exception e) {
                throw new RuntimeException("Failed to encode body as JSON: " + e.getMessage(), e);
            }
        }
        throw new IllegalArgumentException("Unexpected body type: " + body.getClass());
    }

    protected String getLimit(JobDetails job) {
        if (job.getTrigger() instanceof SimpleTimerTrigger) {
            return String.valueOf(getRepeatableJobCountDown((SimpleTimerTrigger) job.getTrigger()));
        }
        if (job.getTrigger() instanceof IntervalTrigger) {
            return String.valueOf(getRepeatableJobCountDown((IntervalTrigger) job.getTrigger()));
        }
        return "0";
    }

    protected long getTimeoutInMillis(JobDetails job) {
        if (job.getExecutionTimeout() == null) {
            return timeout;
        }
        ChronoUnit timeoutUnit = job.getExecutionTimeoutUnit() != null ? job.getExecutionTimeoutUnit() : ChronoUnit.MILLIS;
        return timeoutUnit == ChronoUnit.MILLIS ? job.getExecutionTimeout() : timeoutUnit.getDuration().multipliedBy(job.getExecutionTimeout()).toMillis();
    }

    protected int getRepeatableJobCountDown(IntervalTrigger trigger) {
        return trigger.getRepeatLimit() - trigger.getRepeatCount() - 1;//since the repeatCount is updated only after this call when persisting the job.
    }

    protected int getRepeatableJobCountDown(SimpleTimerTrigger trigger) {
        // The SimpleTimerTrigger stops when the (desired repetitions - actual executed repetitions) == 0.
        return trigger.getRepeatCount() - trigger.getCurrentRepeatCount();
    }

    protected static <K, V> Map<K, V> filterEntries(Map<K, V> source) {
        if (source == null) {
            return Collections.emptyMap();
        }
        return source.entrySet()
                .stream()
                .filter(entry -> entry.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
