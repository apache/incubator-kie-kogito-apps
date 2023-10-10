package org.kie.kogito.trusty.storage.postgresql;

import javax.enterprise.context.ApplicationScoped;

import org.hibernate.exception.JDBCConnectionException;
import org.kie.kogito.trusty.storage.api.StorageExceptionsProvider;

@ApplicationScoped
public class PostgreSQLStorageExceptionsProviderImpl implements StorageExceptionsProvider {

    @Override
    public boolean isConnectionException(Throwable e) {
        return e instanceof JDBCConnectionException;
    }
}
