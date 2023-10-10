package org.kie.kogito.swf.tools.custom.dashboard;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.kie.kogito.swf.tools.custom.dashboard.model.CustomDashboardFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

@Path("/customDashboard")
public class CustomDashboardService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomDashboardService.class);

    private CustomDashboardStorage storage;

    @Inject
    public void setStorage(CustomDashboardStorage storage) {
        this.storage = storage;
    }

    @GET
    @Path("/count")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getCustomDashboardFilesCount() {
        try {
            return Response.ok(storage.getCustomDashboardFilesCount()).build();
        } catch (Exception e) {
            LOGGER.warn("Error while getting CustomDashboard file count: ", e);
            return Response.status(INTERNAL_SERVER_ERROR.getStatusCode(), "Unexpected error while getting CustomDashboard files count: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCustomDashboardFiles(@QueryParam("names") CustomDashboardFilter filter) {
        try {
            return Response.ok(storage.getCustomDashboardFiles(filter)).build();
        } catch (Exception e) {
            LOGGER.warn("Error while getting CustomDashboard list: ", e);
            return Response.status(INTERNAL_SERVER_ERROR.getStatusCode(), "Unexpected error while getting CustomDashboard files list: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("/{name:\\S+}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCustomDashboardFileContent(@PathParam("name") String name) {
        try {
            return Response.ok(storage.getCustomDashboardFileContent(name)).build();
        } catch (Exception e) {
            LOGGER.warn("Error while getting CustomDashboard file content: ", e);
            return Response.status(INTERNAL_SERVER_ERROR.getStatusCode(), "Unexpected error while getting CustomDashboard file content: " + e.getMessage()).build();
        }
    }
}
