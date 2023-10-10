package org.kie.kogito.trusty.service.common.handlers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.kogito.explainability.api.BaseExplainabilityResult;
import org.kie.kogito.persistence.api.Storage;
import org.kie.kogito.trusty.storage.api.model.decision.Decision;
import org.kie.kogito.trusty.storage.common.TrustyStorageService;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public abstract class BaseExplainerServiceHandlerTest<H extends ExplainerServiceHandler<R>, R extends BaseExplainabilityResult> {

    protected static final String EXECUTION_ID = "executionId";

    protected H handler;

    protected TrustyStorageService storageService;

    protected Storage<String, R> storage;

    protected R result;

    protected Decision decision;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() {
        this.storage = mock(Storage.class);
        this.storageService = mock(TrustyStorageService.class);
        this.handler = getHandler();
        this.result = mock(getResult());
        this.decision = mock(Decision.class);

        setupMockStorage();
    }

    protected abstract H getHandler();

    protected abstract Class<R> getResult();

    protected abstract void setupMockStorage();

    @Test
    public void testExplainabilityResult() {
        assertTrue(handler.supports(getResult()));
        assertFalse(handler.supports(BaseExplainabilityResult.class));
    }

    @Test
    public abstract void testGetExplainabilityResultById_WhenStored();

    @Test
    public abstract void testGetExplainabilityResultById_WhenNotStored();

    @Test
    public abstract void testStoreExplainabilityResult_WhenAlreadyStored();

    @Test
    public abstract void testStoreExplainabilityResultById_WhenNotAlreadyStored();
}
