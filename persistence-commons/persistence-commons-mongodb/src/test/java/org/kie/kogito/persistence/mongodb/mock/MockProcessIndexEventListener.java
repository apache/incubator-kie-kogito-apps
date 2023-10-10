package org.kie.kogito.persistence.mongodb.mock;

import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import org.kie.kogito.persistence.mongodb.index.ProcessIndexEvent;
import org.mockito.Mockito;

import io.quarkus.test.Mock;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Mock
@ApplicationScoped
public class MockProcessIndexEventListener {

    Map<String, String> mockProcessTypeMapper = mock(Map.class);

    public void reset() {
        Mockito.reset(mockProcessTypeMapper);
    }

    public void onIndexCreateOrUpdateEvent(@Observes ProcessIndexEvent event) {
        mockProcessTypeMapper.put(event.getProcessDescriptor().getProcessId(), event.getProcessDescriptor().getProcessType());
    }

    public void assertFire(String processId, String processType) {
        verify(mockProcessTypeMapper, times(1)).put(eq(processId), eq(processType));
    }
}
