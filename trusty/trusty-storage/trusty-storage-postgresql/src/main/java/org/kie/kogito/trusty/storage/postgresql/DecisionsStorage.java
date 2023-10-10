package org.kie.kogito.trusty.storage.postgresql;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.kie.kogito.persistence.postgresql.model.CacheEntityRepository;
import org.kie.kogito.trusty.storage.api.model.decision.Decision;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.kie.kogito.trusty.storage.common.TrustyStorageService.DECISIONS_STORAGE;

@ApplicationScoped
public class DecisionsStorage extends BaseTransactionalStorage<Decision> {

    DecisionsStorage() {
        //CDI proxy
    }

    @Inject
    public DecisionsStorage(CacheEntityRepository repository, ObjectMapper mapper) {
        super(DECISIONS_STORAGE, repository, mapper, Decision.class);
    }
}
