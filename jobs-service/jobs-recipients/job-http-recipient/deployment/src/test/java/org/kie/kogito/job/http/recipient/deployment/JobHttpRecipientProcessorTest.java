package org.kie.kogito.job.http.recipient.deployment;

import org.junit.jupiter.api.Test;
import org.kie.kogito.job.http.recipient.HttpJobExecutor;
import org.kie.kogito.job.http.recipient.HttpRecipientValidator;
import org.kie.kogito.jobs.service.api.TemporalUnit;
import org.kie.kogito.jobs.service.api.recipient.http.HttpRecipient;
import org.kie.kogito.jobs.service.api.recipient.http.HttpRecipientBinaryPayloadData;
import org.kie.kogito.jobs.service.api.recipient.http.HttpRecipientJsonPayloadData;
import org.kie.kogito.jobs.service.api.recipient.http.HttpRecipientPayloadData;
import org.kie.kogito.jobs.service.api.recipient.http.HttpRecipientStringPayloadData;
import org.kie.kogito.jobs.service.api.schedule.cron.CronSchedule;
import org.kie.kogito.jobs.service.api.schedule.timer.TimerSchedule;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.builditem.AdditionalIndexedClassesBuildItem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

class JobHttpRecipientProcessorTest {

    private final JobHttpRecipientProcessor processor = new JobHttpRecipientProcessor();

    @Test
    void feature() {
        assertThat(processor.feature().getName()).isEqualTo("job-http-recipient");
    }

    @Test
    void additionalBeans() {
        AdditionalBeanBuildItem additionalBeans = processor.additionalBeans();
        assertThat(additionalBeans.getBeanClasses()).containsExactlyInAnyOrder(
                HttpJobExecutor.class.getName(),
                HttpRecipientValidator.class.getName());
    }

    @Test
    @SuppressWarnings("unchecked")
    void contributeClassesToIndex() {
        BuildProducer<AdditionalIndexedClassesBuildItem> producer = Mockito.mock(BuildProducer.class);
        ArgumentCaptor<AdditionalIndexedClassesBuildItem> captor = ArgumentCaptor.forClass(AdditionalIndexedClassesBuildItem.class);
        processor.contributeClassesToIndex(producer);
        verify(producer).produce(captor.capture());
        AdditionalIndexedClassesBuildItem buildItem = captor.getValue();
        assertThat(buildItem).isNotNull();
        assertThat(buildItem.getClassesToIndex()).containsExactlyInAnyOrder(
                HttpRecipient.class.getName(),
                HttpRecipientPayloadData.class.getName(),
                HttpRecipientStringPayloadData.class.getName(),
                HttpRecipientBinaryPayloadData.class.getName(),
                HttpRecipientJsonPayloadData.class.getName(),
                CronSchedule.class.getName(),
                TimerSchedule.class.getName(),
                TemporalUnit.class.getName());
    }
}
