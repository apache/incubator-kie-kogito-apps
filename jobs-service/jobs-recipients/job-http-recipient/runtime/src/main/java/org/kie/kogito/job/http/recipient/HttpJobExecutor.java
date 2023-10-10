package org.kie.kogito.job.http.recipient;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.kie.kogito.job.recipient.common.http.HTTPRequest;
import org.kie.kogito.job.recipient.common.http.HTTPRequestExecutor;
import org.kie.kogito.jobs.service.api.recipient.http.HttpRecipient;
import org.kie.kogito.jobs.service.executor.JobExecutor;
import org.kie.kogito.jobs.service.model.JobDetails;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.vertx.mutiny.core.Vertx;

@ApplicationScoped
public class HttpJobExecutor extends HTTPRequestExecutor<HttpRecipient<?>> implements JobExecutor {

    @Inject
    public HttpJobExecutor(@ConfigProperty(name = "kogito.job.recipient.http.timeout-in-millis") long timeout,
            Vertx vertx,
            ObjectMapper objectMapper) {
        super(timeout, vertx, objectMapper);
    }

    @PostConstruct
    @Override
    public void initialize() {
        super.initialize();
    }

    @Override
    public Class<HttpRecipient> type() {
        return HttpRecipient.class;
    }

    @Override
    protected HttpRecipient<?> getRecipient(JobDetails job) {
        if (job.getRecipient().getRecipient() instanceof HttpRecipient) {
            return (HttpRecipient<?>) job.getRecipient().getRecipient();
        }
        throw new IllegalArgumentException("HttpRecipient is expected for job " + job);
    }

    @Override
    protected HTTPRequest buildRequest(HttpRecipient<?> recipient, String limit) {
        return HTTPRequest.builder()
                .url(recipient.getUrl())
                .method(recipient.getMethod())
                .headers(recipient.getHeaders())
                .queryParams(recipient.getQueryParams())
                //in case of repeatable jobs add the limit parameter, override if already present.
                .addQueryParam("limit", limit)
                .body(recipient.getPayload() != null ? recipient.getPayload().getData() : null)
                .build();
    }
}
