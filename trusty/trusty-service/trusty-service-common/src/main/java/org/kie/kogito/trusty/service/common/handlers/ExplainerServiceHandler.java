package org.kie.kogito.trusty.service.common.handlers;

import org.kie.kogito.explainability.api.BaseExplainabilityResult;

/**
 * The handler for a specific type of explanation; decoupling operations specific to type of explanation.
 *
 * @param <R> The Trusty Service explanation type.
 */
public interface ExplainerServiceHandler<R extends BaseExplainabilityResult> {

    /**
     * Checks whether an implementation supports a type of explanation.
     *
     * @param type The Trusty Service result type.
     * @param <T>
     * @return true if the implementation supports the type of explanation.
     */
    <T extends BaseExplainabilityResult> boolean supports(Class<T> type);

    /**
     * Gets the result for an explanation.
     *
     * @param executionId The execution Id for which to retrieve an explanation result.
     * @return The result of an explanation.
     */
    R getExplainabilityResultById(String executionId);

    /**
     * Stores the result for an explanation.
     *
     * @param executionId The execution Id for which to retrieve an explanation result.
     * @param result The result to store.
     */
    void storeExplainabilityResult(String executionId, R result);
}
