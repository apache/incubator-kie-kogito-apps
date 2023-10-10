package org.kie.kogito.job.http.recipient;

import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;

@ConfigRoot(prefix = "kogito", name = "job.recipient.http", phase = ConfigPhase.RUN_TIME)
public class JobHttpRecipientRuntimeConfiguration {

    /**
     * Default timeout to execute HTTP requests for the HttpRecipient when the Job's timeout is not configured.
     */
    @ConfigItem(name = "timeout-in-millis", defaultValue = "180000")
    long timeoutInMillis;

    /**
     * Max accepted timeout to execute HTTP requests for the HttpRecipient when the Job's timeout is configured.
     * Attempts to surpass this value will result in a validation error at Job creation time.
     */
    @ConfigItem(name = "max-timeout-in-millis", defaultValue = "300000")
    long maxTimeoutInMillis;
}
