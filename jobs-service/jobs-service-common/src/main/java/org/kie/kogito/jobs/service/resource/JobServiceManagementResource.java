package org.kie.kogito.jobs.service.resource;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.kie.kogito.jobs.service.management.ReleaseLeaderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.smallrye.mutiny.Uni;

@Path("/management")
public class JobServiceManagementResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobServiceManagementResource.class);

    @Inject
    Event<ReleaseLeaderEvent> releaseLeaderEventEvent;

    @POST
    @Path("/shutdown")
    public Uni<Response> shutdownHook() {
        return Uni.createFrom().voidItem()
                .onItem().invoke(i -> LOGGER.info("Job Service is shutting down"))
                .onItem().invoke(() -> releaseLeaderEventEvent.fire(new ReleaseLeaderEvent()))
                .onItem().transform(i -> Response.ok().build());
    }
}
