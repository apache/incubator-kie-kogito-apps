package org.kie.kogito.job.sink.recipient;

import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;

@ConfigRoot(prefix = "kogito", name = "job.recipient.sink", phase = ConfigPhase.RUN_TIME)
public class JobSinkRecipientRuntimeConfiguration {

    /**
     * Default timeout to execute HTTP requests for the SinkRecipient when the Job's timeout is not configured.
     */
    @ConfigItem(name = "timeout-in-millis", defaultValue = "5000")
    long timeoutInMillis;

    /**
     * Max accepted timeout to execute HTTP requests for the SinkRecipient when the Job's timeout is configured.
     * Attempts to surpass this value will result in a validation error at Job creation time.
     */
    @ConfigItem(name = "max-timeout-in-millis", defaultValue = "60000")
    long maxTimeoutInMillis;
}
