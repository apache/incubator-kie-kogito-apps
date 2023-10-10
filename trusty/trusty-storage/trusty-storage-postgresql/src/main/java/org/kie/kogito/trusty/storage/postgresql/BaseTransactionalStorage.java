package org.kie.kogito.trusty.storage.postgresql;

import java.util.Map;

import javax.transaction.Transactional;

import org.kie.kogito.persistence.api.Storage;
import org.kie.kogito.persistence.api.query.Query;
import org.kie.kogito.persistence.postgresql.PostgresStorage;
import org.kie.kogito.persistence.postgresql.model.CacheEntityRepository;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.smallrye.mutiny.Multi;

public abstract class BaseTransactionalStorage<T> implements Storage<String, T> {

    private PostgresStorage<T> delegate;

    BaseTransactionalStorage() {
        //CDI proxy
    }

    protected BaseTransactionalStorage(String name, CacheEntityRepository repository, ObjectMapper mapper, Class<T> type) {
        this(new PostgresStorage<>(name, repository, mapper, type));
    }

    //For Unit Tests to check delegation
    BaseTransactionalStorage(final PostgresStorage<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public Multi<T> objectCreatedListener() {
        return delegate.objectCreatedListener();
    }

    @Override
    public Multi<T> objectUpdatedListener() {
        return delegate.objectUpdatedListener();
    }

    @Override
    public Multi<String> objectRemovedListener() {
        return delegate.objectRemovedListener();
    }

    @Override
    @Transactional
    public Query<T> query() {
        return delegate.query();
    }

    @Override
    @Transactional
    public T get(String key) {
        return delegate.get(key);
    }

    @Override
    @Transactional
    public T put(String key, T value) {
        return delegate.put(key, value);
    }

    @Override
    @Transactional
    public T remove(String key) {
        return delegate.remove(key);
    }

    @Override
    @Transactional
    public boolean containsKey(String key) {
        return delegate.containsKey(key);
    }

    @Override
    @Transactional
    public Map<String, T> entries() {
        return delegate.entries();
    }

    @Override
    @Transactional
    public void clear() {
        delegate.clear();
    }

    @Override
    public String getRootType() {
        return delegate.getRootType();
    }
}
