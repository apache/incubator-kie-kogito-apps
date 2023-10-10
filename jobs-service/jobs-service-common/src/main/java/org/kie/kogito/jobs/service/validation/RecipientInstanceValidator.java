package org.kie.kogito.jobs.service.validation;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.kie.kogito.jobs.service.exception.JobValidationException;
import org.kie.kogito.jobs.service.model.Recipient;

@ApplicationScoped
public class RecipientInstanceValidator {

    private final RecipientValidatorProvider recipientValidatorProvider;

    @Inject
    public RecipientInstanceValidator(RecipientValidatorProvider recipientValidatorProvider) {
        this.recipientValidatorProvider = recipientValidatorProvider;
    }

    public void validate(Recipient recipient) {
        if (recipient == null) {
            throw new JobValidationException(Recipient.class.getName() + " instance can not be null.");
        }
        if (recipient.getRecipient() == null) {
            throw new JobValidationException(org.kie.kogito.jobs.service.api.Recipient.class + " instance can not be null.");
        }
        recipientValidatorProvider.getValidator(recipient.getRecipient())
                .ifPresent(validator -> validator.validate(recipient.getRecipient(), new ValidatorContext()));
    }
}
