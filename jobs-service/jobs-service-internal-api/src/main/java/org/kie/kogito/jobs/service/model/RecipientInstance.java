package org.kie.kogito.jobs.service.model;

import java.util.Objects;

/**
 * Class is used to wrap the public API recipient and do the interface with the internal API and persistence.
 */
public class RecipientInstance implements Recipient {

    private final org.kie.kogito.jobs.service.api.Recipient<?> recipient;

    public RecipientInstance(org.kie.kogito.jobs.service.api.Recipient<?> recipient) {
        Objects.requireNonNull(recipient);
        this.recipient = recipient;
    }

    @Override
    public org.kie.kogito.jobs.service.api.Recipient<?> getRecipient() {
        return recipient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RecipientInstance that = (RecipientInstance) o;
        return Objects.equals(recipient, that.recipient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipient);
    }

    @Override
    public String toString() {
        return "RecipientInstance{" +
                "recipient=" + recipient +
                '}';
    }
}
