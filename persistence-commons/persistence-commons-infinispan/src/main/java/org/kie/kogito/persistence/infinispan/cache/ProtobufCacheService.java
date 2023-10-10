package org.kie.kogito.persistence.infinispan.cache;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.kie.kogito.persistence.api.Storage;
import org.kie.kogito.persistence.api.StorageService;
import org.kie.kogito.persistence.infinispan.Constants;

@ApplicationScoped
public class ProtobufCacheService {

    @Inject
    StorageService storageService;

    public Storage<String, String> getProtobufCache() {
        return storageService.getCache(Constants.PROTOBUF_METADATA_CACHE_NAME, String.class);
    }
}
