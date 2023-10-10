package org.kie.kogito.jobs.service.repository.marshaller;

import java.util.Objects;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;

import org.kie.kogito.jobs.service.model.Recipient;
import org.kie.kogito.jobs.service.model.RecipientInstance;

import io.vertx.core.json.JsonObject;

@ApplicationScoped
public class RecipientMarshaller implements Marshaller<Recipient, JsonObject> {

    public static final String CLASS_TYPE = "classType";

    @Override
    public JsonObject marshall(Recipient recipient) {
        if (Objects.isNull(recipient)) {
            return null;
        }
        return JsonObject
                .mapFrom(recipient.getRecipient())
                .put(CLASS_TYPE, recipient.getRecipient().getClass().getName());
    }

    @Override
    public Recipient unmarshall(JsonObject jsonObject) {
        if (Objects.isNull(jsonObject)) {
            return null;
        }
        String classType = Optional.ofNullable(jsonObject).map(o -> (String) o.remove(CLASS_TYPE)).orElse(null);
        if (Objects.isNull(classType)) {
            return null;
        }
        try {
            return new RecipientInstance((org.kie.kogito.jobs.service.api.Recipient<?>) jsonObject.mapTo(Class.forName(classType)));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
