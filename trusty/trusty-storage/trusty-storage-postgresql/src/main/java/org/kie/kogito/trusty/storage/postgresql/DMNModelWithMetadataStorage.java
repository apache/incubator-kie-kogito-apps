package org.kie.kogito.trusty.storage.postgresql;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.kie.kogito.persistence.postgresql.model.CacheEntityRepository;
import org.kie.kogito.trusty.storage.api.model.decision.DMNModelWithMetadata;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.kie.kogito.trusty.storage.common.TrustyStorageService.MODELS_STORAGE;

@ApplicationScoped
public class DMNModelWithMetadataStorage extends BaseTransactionalStorage<DMNModelWithMetadata> {

    DMNModelWithMetadataStorage() {
        //CDI proxy
    }

    @Inject
    public DMNModelWithMetadataStorage(CacheEntityRepository repository, ObjectMapper mapper) {
        super(MODELS_STORAGE, repository, mapper, DMNModelWithMetadata.class);
    }
}
