package org.kie.kogito.trusty.service.common.responses;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The execution headers response.
 */
public class ExecutionsResponse {

    @JsonProperty("total")
    private int total;

    @JsonProperty("limit")
    private int limit;

    @JsonProperty("offset")
    private int offset;

    @JsonProperty("headers")
    private Collection<ExecutionHeaderResponse> headers;

    private ExecutionsResponse() {
    }

    public ExecutionsResponse(int total, int returnedRecords, int offset, Collection<ExecutionHeaderResponse> headers) {
        this.total = total;
        this.limit = returnedRecords;
        this.offset = offset;
        this.headers = headers;
    }

    /**
     * Gets the total number of items returned.
     *
     * @return The total number of items returned.
     */
    public int getTotal() {
        return total;
    }

    /**
     * Gets the requested limit.
     *
     * @return The maximum number of items to be returned.
     */
    public int getLimit() {
        return limit;
    }

    /**
     * Gets the starting offset for the pagination.
     *
     * @return The pagination offset.
     */
    public int getOffset() {
        return offset;
    }

    /**
     * Gets the execution headers.
     *
     * @return THe execution headers.
     */
    public Collection<ExecutionHeaderResponse> getHeaders() {
        return headers;
    }
}