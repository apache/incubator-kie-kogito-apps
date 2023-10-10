package org.kie.kogito.trusty.service.common.handlers;

import org.junit.jupiter.api.Test;
import org.kie.kogito.explainability.api.LIMEExplainabilityResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LIMEExplainerServiceHandlerTest extends BaseExplainerServiceHandlerTest<LIMEExplainerServiceHandler, LIMEExplainabilityResult> {

    @Override
    protected LIMEExplainerServiceHandler getHandler() {
        return new LIMEExplainerServiceHandler(storageService);
    }

    @Override
    protected Class<LIMEExplainabilityResult> getResult() {
        return LIMEExplainabilityResult.class;
    }

    @Override
    protected void setupMockStorage() {
        when(storageService.getLIMEResultStorage()).thenReturn(storage);
    }

    @Test
    @Override
    public void testGetExplainabilityResultById_WhenStored() {
        when(storage.containsKey(anyString())).thenReturn(true);
        when(storage.get(eq(EXECUTION_ID))).thenReturn(result);

        assertEquals(result, handler.getExplainabilityResultById(EXECUTION_ID));
    }

    @Test
    @Override
    public void testGetExplainabilityResultById_WhenNotStored() {
        when(storage.containsKey(anyString())).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> handler.getExplainabilityResultById(EXECUTION_ID));
    }

    @Test
    @Override
    public void testStoreExplainabilityResult_WhenAlreadyStored() {
        when(storage.containsKey(anyString())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> handler.storeExplainabilityResult(EXECUTION_ID, result));
    }

    @Test
    @Override
    public void testStoreExplainabilityResultById_WhenNotAlreadyStored() {
        when(storage.containsKey(anyString())).thenReturn(false);

        handler.storeExplainabilityResult(EXECUTION_ID, result);

        verify(storage).put(eq(EXECUTION_ID), eq(result));
    }
}
