package org.kie.kogito.trusty.service.common.handlers;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.kie.kogito.explainability.api.LIMEExplainabilityResult;
import org.kie.kogito.explainability.api.SaliencyModel;
import org.kie.kogito.trusty.service.common.TrustyService;
import org.kie.kogito.trusty.service.common.responses.SalienciesResponse;
import org.kie.kogito.trusty.service.common.responses.SaliencyResponse;
import org.kie.kogito.trusty.storage.api.model.decision.Decision;
import org.kie.kogito.trusty.storage.api.model.decision.DecisionOutcome;

/**
 * Converts from the generic {@link SaliencyModel} to {@link SaliencyResponse} comaptible with the needs of the UI.
 * In addition to "name" and "feature importance" the UI uses the original {@link DecisionOutcome} ID as a key to match
 * Salience with Outcome. The need for the specialised {@link SaliencyResponse} and this concerter class become
 * obsolete when https://issues.redhat.com/browse/FAI-655 is completed.
 */
@ApplicationScoped
public class LIMESaliencyConverter {

    protected TrustyService trustyService;

    protected LIMESaliencyConverter() {
        //CDI proxy
    }

    @Inject
    public LIMESaliencyConverter(TrustyService trustyService) {
        this.trustyService = trustyService;
    }

    public SalienciesResponse fromResult(String executionId,
            LIMEExplainabilityResult result) {
        return buildSalienciesResponse(trustyService.getDecisionById(executionId), result);
    }

    private SalienciesResponse buildSalienciesResponse(Decision decision,
            LIMEExplainabilityResult result) {
        Map<String, String> outcomeNameToIdMap = decision == null
                ? Collections.emptyMap()
                : decision.getOutcomes().stream().collect(Collectors.toUnmodifiableMap(DecisionOutcome::getOutcomeName, DecisionOutcome::getOutcomeId));

        List<SaliencyResponse> saliencies = result.getSaliencies() == null
                ? Collections.emptyList()
                : result.getSaliencies().stream()
                        .map(saliency -> saliencyFrom(outcomeNameToIdMap.get(saliency.getOutcomeName()), saliency))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
        return new SalienciesResponse(result.getStatus().name(),
                result.getStatusDetails(),
                saliencies);
    }

    private SaliencyResponse saliencyFrom(String outcomeId,
            SaliencyModel saliency) {
        if (Objects.isNull(outcomeId)) {
            return null;
        }
        if (Objects.isNull(saliency)) {
            return null;
        }
        return new SaliencyResponse(outcomeId,
                saliency.getOutcomeName(),
                saliency.getFeatureImportance());
    }
}
