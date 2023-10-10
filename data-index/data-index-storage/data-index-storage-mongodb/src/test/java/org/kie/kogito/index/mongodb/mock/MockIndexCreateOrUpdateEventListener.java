package org.kie.kogito.index.mongodb.mock;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import org.kie.kogito.persistence.mongodb.index.IndexCreateOrUpdateEvent;
import org.mockito.Mockito;

import io.quarkus.test.Mock;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Mock
@ApplicationScoped
public class MockIndexCreateOrUpdateEventListener {

    List<String> collections = mock(List.class);

    List<String> indexes = mock(List.class);

    public void reset() {
        Mockito.reset(collections, indexes);
    }

    public void onIndexCreateOrUpdateEvent(@Observes IndexCreateOrUpdateEvent event) {
        this.collections.add(event.getCollection());
        this.indexes.add(event.getIndex());
    }

    public void assertFire(String collection, String index) {
        verify(collections, times(1)).add(eq(collection));
        verify(indexes, times(1)).add(eq(index));
    }
}
