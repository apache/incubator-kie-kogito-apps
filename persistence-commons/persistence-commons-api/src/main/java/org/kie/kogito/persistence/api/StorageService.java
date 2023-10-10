package org.kie.kogito.persistence.api;

public interface StorageService {

    Storage<String, String> getCache(String name);

    <T> Storage<String, T> getCache(String name, Class<T> type);

    <T> Storage<String, T> getCache(String name, Class<T> type, String rootType);
}
