package org.kie.kogito.job.sink.recipient;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.kie.kogito.internal.utils.ConversionUtils;
import org.kie.kogito.jobs.service.api.Recipient;
import org.kie.kogito.jobs.service.api.recipient.sink.SinkRecipient;
import org.kie.kogito.jobs.service.api.utils.EventUtils;
import org.kie.kogito.jobs.service.utils.ModelUtil;
import org.kie.kogito.jobs.service.validation.RecipientValidator;
import org.kie.kogito.jobs.service.validation.ValidationException;
import org.kie.kogito.jobs.service.validation.ValidatorContext;

@ApplicationScoped
public class SinkRecipientValidator implements RecipientValidator {

    private long maxTimeoutInMillis;

    public SinkRecipientValidator(@ConfigProperty(name = "kogito.job.recipient.sink.max-timeout-in-millis") long maxTimeoutInMillis) {
        this.maxTimeoutInMillis = maxTimeoutInMillis;
    }

    @Override
    public boolean accept(Recipient<?> recipient) {
        return recipient instanceof SinkRecipient;
    }

    @Override
    public void validate(Recipient<?> recipient, ValidatorContext context) {
        if (!(recipient instanceof SinkRecipient)) {
            throw new ValidationException("Recipient must be a non-null instance of: " + SinkRecipient.class + ".");
        }
        SinkRecipient<?> sinkRecipient = (SinkRecipient<?>) recipient;
        if (ConversionUtils.isEmpty(sinkRecipient.getSinkUrl())) {
            throw new ValidationException("SinkRecipient sinkUrl must have a non empty value.");
        }
        try {
            new URL(sinkRecipient.getSinkUrl());
        } catch (MalformedURLException e) {
            throw new ValidationException("SinkRecipient must have a valid url.", e);
        }
        if (Objects.isNull(sinkRecipient.getContentMode())) {
            throw new ValidationException("SinkRecipient contentMode must have a non null value.");
        }
        if (Objects.isNull(sinkRecipient.getCeSpecVersion())) {
            throw new ValidationException("SinkRecipient ce-specversion must have a non null value.");
        }
        if (ConversionUtils.isEmpty(sinkRecipient.getCeType())) {
            throw new ValidationException("SinkRecipient ce-type must have a non empty value.");
        }
        if (Objects.isNull(sinkRecipient.getCeSource())) {
            throw new ValidationException("SinkRecipient ce-source must have a non null value.");
        }
        sinkRecipient.getCeExtensions().keySet()
                .forEach(EventUtils::validateExtensionName);
        if (context.getJob() != null) {
            Long timeoutInMillis = ModelUtil.getExecutionTimeoutInMillis(context.getJob());
            if (timeoutInMillis != null && timeoutInMillis > maxTimeoutInMillis) {
                throw new ValidationException("Job executionTimeout configuration can not exceed the SinkRecipient max-timeout-in-millis: "
                        + maxTimeoutInMillis + ", but is: " + timeoutInMillis + ".");
            }
        }
    }
}
