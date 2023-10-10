package org.kie.kogito.trusty.storage.infinispan;

import javax.enterprise.context.ApplicationScoped;

import org.infinispan.client.hotrod.exceptions.HotRodClientException;
import org.kie.kogito.trusty.storage.api.StorageExceptionsProvider;

@ApplicationScoped
public class InfinispanStorageExceptionsProviderImpl implements StorageExceptionsProvider {
    public boolean isConnectionException(Throwable e) {
        return e instanceof HotRodClientException;
    }
}
