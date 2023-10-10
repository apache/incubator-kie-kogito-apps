package org.kie.kogito.job.recipient.common.http.converters;

import org.kie.kogito.job.recipient.common.http.HTTPRequest;

import io.vertx.core.http.HttpMethod;

public class HttpConverters {

    private HttpConverters() {
    }

    public static HttpMethod convertHttpMethod(HTTPRequest.HTTPMethod method) {
        return HttpMethod.valueOf(method.name());
    }
}
