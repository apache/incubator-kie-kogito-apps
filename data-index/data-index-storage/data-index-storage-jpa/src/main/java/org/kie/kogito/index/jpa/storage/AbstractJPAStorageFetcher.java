package org.kie.kogito.index.jpa.storage;

import java.util.function.Function;

import org.kie.kogito.index.jpa.model.AbstractEntity;
import org.kie.kogito.persistence.api.StorageFetcher;
import org.kie.kogito.persistence.api.query.Query;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.smallrye.mutiny.Multi;

public class AbstractJPAStorageFetcher<E extends AbstractEntity, V> implements StorageFetcher<String, V> {

    private static final String LISTENER_NOT_AVAILABLE_IN_POSTGRES_SQL = "Listener not available in PostgresSQL";

    protected PanacheRepositoryBase<E, String> repository;
    protected Class<E> entityClass;
    protected Function<E, V> mapToModel;

    protected AbstractJPAStorageFetcher() {
    }

    protected AbstractJPAStorageFetcher(PanacheRepositoryBase<E, String> repository, Class<E> entityClass, Function<E, V> mapToModel) {
        this.repository = repository;
        this.entityClass = entityClass;
        this.mapToModel = mapToModel;
    }

    @Override
    public Multi<V> objectCreatedListener() {
        throw new UnsupportedOperationException(LISTENER_NOT_AVAILABLE_IN_POSTGRES_SQL);
    }

    @Override
    public Multi<V> objectUpdatedListener() {
        throw new UnsupportedOperationException(LISTENER_NOT_AVAILABLE_IN_POSTGRES_SQL);
    }

    @Override
    public Multi<String> objectRemovedListener() {
        throw new UnsupportedOperationException(LISTENER_NOT_AVAILABLE_IN_POSTGRES_SQL);
    }

    @Override
    public Query<V> query() {
        return new JPASqlQuery<>(repository, mapToModel, entityClass);
    }

    @Override
    public V get(String key) {
        return repository.findByIdOptional(key).map(mapToModel).orElse(null);
    }

}
