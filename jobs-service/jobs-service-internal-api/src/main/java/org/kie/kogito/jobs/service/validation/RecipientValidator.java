package org.kie.kogito.jobs.service.validation;

import org.kie.kogito.jobs.service.api.Recipient;

public interface RecipientValidator {
    boolean accept(Recipient<?> recipient);

    void validate(Recipient<?> recipient, ValidatorContext context);
}
