package org.kie.kogito.trusty.service.common.handlers;

import org.kie.kogito.explainability.api.BaseExplainabilityResult;
import org.kie.kogito.trusty.storage.common.TrustyStorageService;

public abstract class BaseExplainerServiceHandler<R extends BaseExplainabilityResult> implements ExplainerServiceHandler<R> {

    protected TrustyStorageService storageService;

    protected BaseExplainerServiceHandler() {
        //CDI proxy
    }

    protected BaseExplainerServiceHandler(TrustyStorageService storageService) {
        this.storageService = storageService;
    }
}
