package org.kie.kogito.job.sink.recipient.deployment;

import org.junit.jupiter.api.Test;
import org.kie.kogito.job.sink.recipient.SinkJobExecutor;
import org.kie.kogito.job.sink.recipient.SinkRecipientValidator;
import org.kie.kogito.jobs.service.api.TemporalUnit;
import org.kie.kogito.jobs.service.api.recipient.sink.SinkRecipient;
import org.kie.kogito.jobs.service.api.recipient.sink.SinkRecipientBinaryPayloadData;
import org.kie.kogito.jobs.service.api.recipient.sink.SinkRecipientJsonPayloadData;
import org.kie.kogito.jobs.service.api.recipient.sink.SinkRecipientPayloadData;
import org.kie.kogito.jobs.service.api.schedule.cron.CronSchedule;
import org.kie.kogito.jobs.service.api.schedule.timer.TimerSchedule;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import io.cloudevents.SpecVersion;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.builditem.AdditionalIndexedClassesBuildItem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

class JobSinkRecipientProcessorTest {

    private final JobSinkRecipientProcessor processor = new JobSinkRecipientProcessor();

    @Test
    void feature() {
        assertThat(processor.feature().getName()).isEqualTo("job-sink-recipient");
    }

    @Test
    void additionalBeans() {
        AdditionalBeanBuildItem additionalBeans = processor.additionalBeans();
        assertThat(additionalBeans.getBeanClasses()).containsExactlyInAnyOrder(
                SinkJobExecutor.class.getName(),
                SinkRecipientValidator.class.getName());
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
                SinkRecipient.class.getName(),
                SinkRecipientPayloadData.class.getName(),
                SinkRecipientBinaryPayloadData.class.getName(),
                SinkRecipientJsonPayloadData.class.getName(),
                SinkRecipient.ContentMode.class.getName(),
                CronSchedule.class.getName(),
                TimerSchedule.class.getName(),
                TemporalUnit.class.getName(),
                SpecVersion.class.getName());
    }
}
