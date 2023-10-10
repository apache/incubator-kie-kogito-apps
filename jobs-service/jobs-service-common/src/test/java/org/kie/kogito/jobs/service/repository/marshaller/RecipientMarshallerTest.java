package org.kie.kogito.jobs.service.repository.marshaller;

import org.junit.jupiter.api.Test;
import org.kie.kogito.jobs.service.api.recipient.http.HttpRecipient;
import org.kie.kogito.jobs.service.model.Recipient;
import org.kie.kogito.jobs.service.model.RecipientInstance;

import io.vertx.core.json.JsonObject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class RecipientMarshallerTest {

    RecipientMarshaller marshaller = new RecipientMarshaller();

    @Test
    void marshall() {
        Recipient recipient = new RecipientInstance(HttpRecipient.builder().forStringPayload().url("test").build());
        JsonObject jsonObject = marshaller.marshall(recipient);
        assertEquals(buildRecipient(), jsonObject);
    }

    private static JsonObject buildRecipient() {
        return JsonObject
                .mapFrom(HttpRecipient.builder().forStringPayload().url("test").build())
                .put(RecipientMarshaller.CLASS_TYPE, HttpRecipient.class.getName());
    }

    @Test
    void marshallNull() {
        JsonObject jsonObject = marshaller.marshall(null);
        assertNull(jsonObject);
    }

    @Test
    void unmarshall() {
        JsonObject jsonObject = buildRecipient();
        Recipient recipient = marshaller.unmarshall(jsonObject);
        assertEquals(HttpRecipient.builder().forStringPayload().url("test").build(), recipient.getRecipient());
    }

    @Test
    void unmarshallInvalid() {
        JsonObject jsonObject = new JsonObject();
        Recipient recipient = marshaller.unmarshall(jsonObject);
        assertNull(recipient);
    }

    @Test
    void unmarshallNull() {
        Recipient recipient = marshaller.unmarshall(null);
        assertNull(recipient);
    }
}
