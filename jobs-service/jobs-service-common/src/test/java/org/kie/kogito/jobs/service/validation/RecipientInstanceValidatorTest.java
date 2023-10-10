package org.kie.kogito.jobs.service.validation;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kie.kogito.jobs.service.model.Recipient;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RecipientInstanceValidatorTest {

    @Mock
    private RecipientValidatorProvider recipientValidatorProvider;

    @Mock
    private RecipientValidatorProviderTest.RecipientValidator1 validator1;

    @Mock
    private RecipientValidatorProviderTest.Recipient1 recipient1;

    @Mock
    private Recipient recipient;

    private RecipientInstanceValidator recipientInstanceValidator;

    @BeforeEach
    void setUp() {
        recipientInstanceValidator = new RecipientInstanceValidator(recipientValidatorProvider);
        lenient().doReturn(Optional.of(validator1)).when(recipientValidatorProvider).getValidator(recipient1);
    }

    @Test
    void validateNull() {
        validateWithError(null, Recipient.class.getName());
    }

    @Test
    void validateNullRecipient() {
        doReturn(null).when(recipient).getRecipient();
        validateWithError(recipient, org.kie.kogito.jobs.service.api.Recipient.class + " instance can not be null");
    }

    @Test
    void validateSuccessful() {
        doReturn(recipient1).when(recipient).getRecipient();
        recipientInstanceValidator.validate(recipient);
        verify(validator1).validate(eq(recipient1), any());
    }

    @Test
    void validateUnSuccessful() {
        String error = "Validation has failed!";
        doThrow(new RuntimeException(error)).when(validator1).validate(eq(recipient1), any());
        doReturn(recipient1).when(recipient).getRecipient();
        validateWithError(recipient, error);
        verify(validator1).validate(eq(recipient1), any());
    }

    private void validateWithError(Recipient recipient, String expectedMessage) {
        assertThatThrownBy(() -> recipientInstanceValidator.validate(recipient)).hasMessageStartingWith(expectedMessage);
    }
}
