package org.kie.kogito.trusty.service.common;

import javax.enterprise.context.ApplicationScoped;

import org.kie.kogito.trusty.storage.api.StorageExceptionsProvider;

import io.quarkus.test.Mock;

@Mock
@ApplicationScoped
public class StorageExceptionsProviderMock implements StorageExceptionsProvider {
    @Override
    public boolean isConnectionException(Throwable e) {
        return false;
    }
}
