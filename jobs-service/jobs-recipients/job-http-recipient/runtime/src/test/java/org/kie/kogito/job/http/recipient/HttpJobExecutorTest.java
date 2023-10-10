package org.kie.kogito.job.http.recipient;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

import org.kie.kogito.job.recipient.common.http.HTTPRequestExecutorTest;
import org.kie.kogito.jobs.service.api.recipient.http.HttpRecipient;
import org.kie.kogito.jobs.service.api.recipient.http.HttpRecipientStringPayloadData;
import org.kie.kogito.jobs.service.model.JobDetails;
import org.kie.kogito.jobs.service.model.RecipientInstance;
import org.kie.kogito.jobs.service.utils.DateUtil;
import org.kie.kogito.timer.impl.SimpleTimerTrigger;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.vertx.mutiny.core.Vertx;

import static org.assertj.core.api.Assertions.assertThat;

class HttpJobExecutorTest extends HTTPRequestExecutorTest<HttpRecipient<?>, HttpJobExecutor> {

    @Override
    protected HttpJobExecutor createExecutor(long timeout, Vertx vertx, ObjectMapper objectMapper) {
        return new HttpJobExecutor(timeout, vertx, objectMapper);
    }

    @Override
    protected void assertExecuteConditions() {
        assertThat(queryParamsCaptor.getValue())
                .hasSize(1)
                .containsEntry("limit", "0");
        assertCommonBuffer();
    }

    @Override
    protected void assertExecuteWithErrorConditions() {
        assertExecuteConditions();
    }

    @Override
    protected void assertExecutePeriodicConditions() {
        assertThat(queryParamsCaptor.getValue())
                .hasSize(1)
                .containsEntry("limit", "10");
        assertCommonBuffer();
    }

    private void assertCommonBuffer() {
        assertThat(bufferCaptor.getValue()).isNotNull()
                .hasToString(JOB_DATA);
    }

    protected JobDetails createSimpleJob() {
        HttpRecipient<?> recipient = HttpRecipient.builder().forStringPayload()
                .payload(HttpRecipientStringPayloadData.from(JOB_DATA))
                .url(ENDPOINT)
                .build();

        return JobDetails.builder()
                .recipient(new RecipientInstance(recipient))
                .id(JOB_ID)
                .build();
    }

    @Override
    protected JobDetails createPeriodicJob() {
        HttpRecipient<?> recipient = HttpRecipient.builder().forStringPayload()
                .payload(HttpRecipientStringPayloadData.from(JOB_DATA))
                .url(ENDPOINT)
                .build();
        return JobDetails.builder()
                .id(JOB_ID)
                .recipient(new RecipientInstance(recipient))
                .trigger(new SimpleTimerTrigger(DateUtil.toDate(OffsetDateTime.now()), 1, ChronoUnit.MILLIS, 10, null))
                .build();
    }
}
