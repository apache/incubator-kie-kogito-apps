package org.kie.kogito.jitexecutor.dmn.api;

import jakarta.ws.rs.core.Response;

import java.util.function.Supplier;

public class DMNResourceHelper {

    private DMNResourceHelper() {}

    public static Response manageResponse(Supplier<Response> responseSupplier) {
        try{
            return responseSupplier.get();
        } catch (Exception e) {
            String errorMessage = e.getMessage() != null ? e.getMessage() : "Failed to get result due to " + e.getClass().getName();
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), errorMessage).build();
        }
    }
}
