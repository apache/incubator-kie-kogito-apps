package org.kie.kogito.jobs.service.messaging;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Path;

import static org.kie.kogito.jobs.service.messaging.BaseCallbackResource.CALLBACK_RESOURCE_PATH;

@Path(CALLBACK_RESOURCE_PATH)
@ApplicationScoped
public class MongoDBCallbackResource extends BaseCallbackResource {

}
