package org.kie.kogito.jobs.service.resource.error;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseExceptionMapper<T extends Throwable> implements ExceptionMapper<T> {

    public static final int DEFAULT_ERROR_CODE = 500;

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final boolean logStackTrace;
    private final int errorCode;

    public BaseExceptionMapper(boolean logStackTrace) {
        this(DEFAULT_ERROR_CODE, logStackTrace);
    }

    public BaseExceptionMapper(int errorCode, boolean logStackTrace) {
        this.errorCode = errorCode;
        this.logStackTrace = logStackTrace;
    }

    @Override
    public Response toResponse(T exception) {
        log(exception);
        return buildResponse(exception, errorCode);
    }

    protected Response buildResponse(T exception, int errorCode) {
        return Response.status(errorCode)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(new ErrorResponse(errorMessage(exception)))
                .build();
    }

    protected void log(T exception) {
        if (logStackTrace) {
            logger.error("Handling HTTP Error", exception);
        } else {
            logger.error("Handling HTTP Error {}", exception.getMessage());
        }
    }

    protected String errorMessage(T exception) {
        return exception.getMessage();
    }
}
