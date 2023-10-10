package org.kie.kogito.jobs.service.repository.mongodb.marshaller;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.kie.kogito.jobs.service.model.JobDetails;
import org.kie.kogito.jobs.service.repository.marshaller.JobDetailsMarshaller;
import org.kie.kogito.jobs.service.repository.marshaller.RecipientMarshaller;
import org.kie.kogito.jobs.service.repository.marshaller.TriggerMarshaller;

import io.vertx.core.json.JsonObject;

@ApplicationScoped
public class MongoDBJobDetailsMarshaller extends JobDetailsMarshaller {

    MongoDBJobDetailsMarshaller() {
    }

    @Inject
    public MongoDBJobDetailsMarshaller(TriggerMarshaller triggerMarshaller, RecipientMarshaller recipientMarshaller) {
        super(triggerMarshaller, recipientMarshaller);
    }

    @Override
    public JobDetails unmarshall(JsonObject jsonObject) {
        if (jsonObject != null) {
            jsonObject.remove("_id");
        }
        return super.unmarshall(jsonObject);
    }
}
