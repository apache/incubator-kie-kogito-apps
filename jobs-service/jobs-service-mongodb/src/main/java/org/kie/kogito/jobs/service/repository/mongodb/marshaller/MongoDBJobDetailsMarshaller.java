/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.kogito.jobs.service.repository.mongodb.marshaller;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

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
