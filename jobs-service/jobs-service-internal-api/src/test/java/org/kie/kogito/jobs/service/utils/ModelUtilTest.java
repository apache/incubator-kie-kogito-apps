package org.kie.kogito.jobs.service.utils;

import org.junit.jupiter.api.Test;
import org.kie.kogito.jobs.service.api.Job;
import org.kie.kogito.jobs.service.api.TemporalUnit;

import static org.assertj.core.api.Assertions.assertThat;

class ModelUtilTest {

    private Job job = Job.builder().build();

    @Test
    void getExecutionTimeoutInMillisNoValue() {
        assertThat(ModelUtil.getExecutionTimeoutInMillis(job)).isNull();
    }

    @Test
    void getExecutionTimeoutInMillisWithValue() {
        job.setExecutionTimeout(10L);
        assertThat(ModelUtil.getExecutionTimeoutInMillis(job)).isEqualTo(10L);
    }

    @Test
    void getExecutionTimeoutInMillisWithValueAndUnit() {
        job.setExecutionTimeout(5L);
        job.setExecutionTimeoutUnit(TemporalUnit.MINUTES);
        assertThat(ModelUtil.getExecutionTimeoutInMillis(job)).isEqualTo(300000L);
    }
}
