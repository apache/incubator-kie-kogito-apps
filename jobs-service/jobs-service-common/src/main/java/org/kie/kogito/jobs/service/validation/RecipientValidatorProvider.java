package org.kie.kogito.jobs.service.validation;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

@ApplicationScoped
public class RecipientValidatorProvider {
    private final List<RecipientValidator> validators;

    @Inject
    public RecipientValidatorProvider(Instance<RecipientValidator> validators) {
        this.validators = Stream.ofNullable(validators)
                .flatMap(Instance::stream)
                .collect(Collectors.toList());
    }

    public Optional<RecipientValidator> getValidator(org.kie.kogito.jobs.service.api.Recipient<?> recipient) {
        return validators.stream()
                .filter(validator -> validator.accept(recipient))
                .findFirst();
    }
}
