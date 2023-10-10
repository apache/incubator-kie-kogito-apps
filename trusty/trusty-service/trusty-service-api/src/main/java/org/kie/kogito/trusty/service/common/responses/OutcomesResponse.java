package org.kie.kogito.trusty.service.common.responses;

import java.util.Collection;

import org.kie.kogito.ModelDomain;
import org.kie.kogito.trusty.service.common.responses.decision.DecisionOutcomesResponse;
import org.kie.kogito.trusty.service.common.responses.process.ProcessOutcomesResponse;
import org.kie.kogito.trusty.storage.api.model.Outcome;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Base abstract class for <b>OutcomesResponse</b>
 * 
 * @param <T>
 * @param <E>
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "@type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DecisionOutcomesResponse.class, name = "DECISION"),
        @JsonSubTypes.Type(value = ProcessOutcomesResponse.class, name = "PROCESS")
})
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class OutcomesResponse<T extends ExecutionHeaderResponse, E extends Outcome> {

    @JsonProperty("header")
    private T header;

    @JsonProperty("outcomes")
    private Collection<E> outcomes;

    @JsonProperty("@type")
    private ModelDomain modelDomain;

    protected OutcomesResponse() {
    }

    protected OutcomesResponse(T header, Collection<E> outcomes, ModelDomain modelDomain) {
        this.header = header;
        this.outcomes = outcomes;
        this.modelDomain = modelDomain;
    }

    public T getHeader() {
        return header;
    }

    public Collection<E> getOutcomes() {
        return outcomes;
    }
}
