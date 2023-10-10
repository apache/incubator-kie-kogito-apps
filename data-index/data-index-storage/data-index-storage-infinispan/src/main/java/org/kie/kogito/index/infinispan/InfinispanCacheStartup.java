package org.kie.kogito.index.infinispan;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.kie.kogito.index.storage.DataIndexStorageService;

import io.quarkus.runtime.Startup;

@ApplicationScoped
@Startup
public class InfinispanCacheStartup {

    @Inject
    DataIndexStorageService storageService;

    @PostConstruct
    public void init() {
        //Force caches to be initialized at start up
        storageService.getProcessDefinitionsCache();
        storageService.getProcessInstancesCache();
        storageService.getUserTaskInstancesCache();
        storageService.getJobsCache();
        storageService.getProcessIdModelCache();
    }
}
