package org.kie.kogito.job.http.recipient;

import java.net.MalformedURLException;
import java.net.URL;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.kie.kogito.internal.utils.ConversionUtils;
import org.kie.kogito.jobs.service.api.Recipient;
import org.kie.kogito.jobs.service.api.recipient.http.HttpRecipient;
import org.kie.kogito.jobs.service.utils.ModelUtil;
import org.kie.kogito.jobs.service.validation.RecipientValidator;
import org.kie.kogito.jobs.service.validation.ValidationException;
import org.kie.kogito.jobs.service.validation.ValidatorContext;

@ApplicationScoped
public class HttpRecipientValidator implements RecipientValidator {

    private long maxTimeoutInMillis;

    public HttpRecipientValidator(@ConfigProperty(name = "kogito.job.recipient.http.max-timeout-in-millis") long maxTimeoutInMillis) {
        this.maxTimeoutInMillis = maxTimeoutInMillis;
    }

    @Override
    public boolean accept(Recipient<?> recipient) {
        return recipient instanceof HttpRecipient;
    }

    @Override
    public void validate(Recipient<?> recipient, ValidatorContext context) {
        if (!(recipient instanceof HttpRecipient)) {
            throw new ValidationException("Recipient must be a non-null instance of: " + HttpRecipient.class + ".");
        }
        HttpRecipient<?> httpRecipient = (HttpRecipient<?>) recipient;
        if (ConversionUtils.isEmpty(httpRecipient.getUrl())) {
            throw new ValidationException("HttpRecipient url must have a non empty value.");
        }
        try {
            new URL(httpRecipient.getUrl());
        } catch (MalformedURLException e) {
            throw new ValidationException("HttpRecipient must have a valid url.", e);
        }
        if (context.getJob() != null) {
            Long timeoutInMillis = ModelUtil.getExecutionTimeoutInMillis(context.getJob());
            if (timeoutInMillis != null && timeoutInMillis > maxTimeoutInMillis) {
                throw new ValidationException("Job executionTimeout configuration can not exceed the HttpRecipient max-timeout-in-millis: " + maxTimeoutInMillis +
                        ", but is: " + timeoutInMillis + ".");
            }
        }
    }
}
