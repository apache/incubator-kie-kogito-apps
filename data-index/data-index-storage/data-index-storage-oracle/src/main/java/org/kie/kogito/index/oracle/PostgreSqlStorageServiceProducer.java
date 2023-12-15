package org.kie.kogito.index.oracle;

import org.kie.kogito.index.jpa.storage.JPAStorageService;
import org.kie.kogito.index.jpa.storage.JobEntityStorage;
import org.kie.kogito.index.jpa.storage.ProcessDefinitionEntityStorage;
import org.kie.kogito.index.jpa.storage.ProcessInstanceEntityStorage;
import org.kie.kogito.index.jpa.storage.UserTaskInstanceEntityStorage;
import org.kie.kogito.persistence.api.StorageService;

import io.quarkus.arc.properties.IfBuildProperty;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.enterprise.inject.Produces;

import static org.kie.kogito.persistence.api.factory.Constants.PERSISTENCE_TYPE_PROPERTY;

public class PostgreSqlStorageServiceProducer {
    @Produces
    @Alternative
    @Priority(1)
    @ApplicationScoped
    @IfBuildProperty(name = PERSISTENCE_TYPE_PROPERTY, stringValue = "oracle")
    StorageService PostgreSqlStorageService(final ProcessDefinitionEntityStorage definitionStorage,
            final ProcessInstanceEntityStorage processStorage,
            final JobEntityStorage jobStorage,
            final UserTaskInstanceEntityStorage taskStorage) {
        return new JPAStorageService(definitionStorage, processStorage, jobStorage, taskStorage);
    }
}
