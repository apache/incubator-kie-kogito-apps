package org.kie.kogito.job.http.recipient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.kogito.jobs.service.api.Job;
import org.kie.kogito.jobs.service.api.TemporalUnit;
import org.kie.kogito.jobs.service.api.recipient.http.HttpRecipient;
import org.kie.kogito.jobs.service.validation.ValidatorContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HttpRecipientValidatorTest {

    private static final long MAX_TIMEOUT = 3000;

    public static final String URL = "http://my_url";

    private HttpRecipientValidator validator;

    private HttpRecipient<?> recipient;

    @BeforeEach
    public void setUp() {
        validator = new HttpRecipientValidator(MAX_TIMEOUT);
        recipient = new HttpRecipient<>();
        recipient.setUrl(URL);
    }

    @Test
    void acceptNonNull() {
        assertThat(validator.accept(recipient)).isTrue();
    }

    @Test
    void acceptNull() {
        assertThat(validator.accept(null)).isFalse();
    }

    @Test
    void validateSuccessful() {
        assertThatNoException().isThrownBy(() -> validator.validate(recipient, new ValidatorContext()));
    }

    @Test
    void validateNull() {
        recipient = null;
        testUnsuccessfulValidation("Recipient must be a non-null instance of", new ValidatorContext());
    }

    @Test
    void validateNullURL() {
        recipient.setUrl(null);
        testUnsuccessfulValidation("HttpRecipient url must have a non empty value.", new ValidatorContext());
    }

    @Test
    void validateMalformedURL() {
        recipient.setUrl("bad url");
        testUnsuccessfulValidation("HttpRecipient must have a valid url.", new ValidatorContext());
    }

    @Test
    void validateJobExecutionTimeoutOK() {
        Job job = Job.builder()
                .executionTimeout(3L)
                .executionTimeoutUnit(TemporalUnit.SECONDS)
                .build();
        validator.validate(recipient, new ValidatorContext(job));
    }

    @Test
    void validateJobExecutionTimeoutExceedsMaxTimeoutMillis() {
        Job job = Job.builder()
                .executionTimeout(MAX_TIMEOUT + 1)
                .build();
        testUnsuccessfulValidation("Job executionTimeout configuration can not exceed the HttpRecipient max-timeout-in-millis",
                new ValidatorContext(job));
    }

    @Test
    void validateJobExecutionTimeoutExceedsMaxTimeoutSeconds() {
        Job job = Job.builder()
                .executionTimeout(MAX_TIMEOUT)
                .executionTimeoutUnit(TemporalUnit.SECONDS)
                .build();
        testUnsuccessfulValidation("Job executionTimeout configuration can not exceed the HttpRecipient max-timeout-in-millis",
                new ValidatorContext(job));
    }

    private void testUnsuccessfulValidation(String expectedError, ValidatorContext context) {
        assertThatThrownBy(() -> validator.validate(recipient, context))
                .hasMessageStartingWith(expectedError);
    }
}
