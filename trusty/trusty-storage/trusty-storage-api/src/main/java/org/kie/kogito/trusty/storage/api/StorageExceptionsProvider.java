package org.kie.kogito.trusty.storage.api;

public interface StorageExceptionsProvider {
    boolean isConnectionException(Throwable e);
}
