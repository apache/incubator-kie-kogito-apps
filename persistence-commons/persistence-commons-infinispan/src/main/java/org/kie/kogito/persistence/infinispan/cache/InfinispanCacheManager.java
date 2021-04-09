/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kie.kogito.persistence.infinispan.cache;

import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.infinispan.client.hotrod.DataFormat;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.RemoteCacheManagerAdmin;
import org.infinispan.commons.dataconversion.MediaType;
import org.kie.kogito.persistence.api.Storage;
import org.kie.kogito.persistence.api.StorageService;
import org.kie.kogito.persistence.api.factory.StorageQualifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.kie.kogito.persistence.infinispan.Constants.INFINISPAN_STORAGE;

@ApplicationScoped
@StorageQualifier(INFINISPAN_STORAGE)
public class InfinispanCacheManager implements StorageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InfinispanCacheManager.class);

    @Inject
    JsonDataFormatMarshaller marshaller;

    DataFormat jsonDataFormat;

    @Inject
    @ConfigProperty(name = "kogito.cache.domain.template")
    Optional<String> cacheTemplateName;

    @Inject
    RemoteCacheManager manager;

    @PostConstruct
    public void init() {
        jsonDataFormat = DataFormat.builder().valueType(MediaType.APPLICATION_JSON).valueMarshaller(marshaller).build();
        manager.start();
    }

    @PreDestroy
    public void destroy() {
        manager.stop();
        try {
            manager.close();
        } catch (Exception ex) {
            LOGGER.warn("Error trying to close Infinispan remote cache manager", ex);
        }
    }

    /**
     * Gets the cache if exists, otherwise tries to create one with the given template.
     * If the template does not exist on the server, creates the cache based on a default configuration.
     *
     * @param name the cache manager name
     * @see KogitoCacheDefaultConfiguration
     */
    protected <K, V> RemoteCache<K, V> getOrCreateCache(final String name) {
        LOGGER.debug("Trying to get cache {} from the server", name);
        RemoteCache<K, V> remoteCache = manager.getCache(name);
        return remoteCache == null ? createCache(name) : remoteCache;
    }

    protected <K, V> RemoteCache<K, V> createCache(final String name) {
        RemoteCacheManagerAdmin admin = manager.administration();
        if (cacheTemplateName.isPresent()) {
            LOGGER.debug("Creating cache {} based on template named {}", name, cacheTemplateName.get());
            return admin.createCache(name, cacheTemplateName.get());
        } else {
            LOGGER.debug("Creating cache {} based on Kogito default configuration", name);
            return admin.createCache(name, new KogitoCacheDefaultConfiguration(name));
        }
    }

    @Override
    public Storage<String, String> getCache(String name) {
        return new StorageImpl<>(getOrCreateCache(name), String.class.getName());
    }

    @Override
    public <T> Storage<String, T> getCache(String name, Class<T> type) {
        return new StorageImpl<>(getOrCreateCache(name), type.getName());
    }

    @Override
    public <T> Storage<String, T> getCache(String name, Class<T> type, String rootType) {
        return new StorageImpl<>(getOrCreateCache(name).withDataFormat(jsonDataFormat), rootType);
    }
}