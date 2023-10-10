package org.kie.kogito.persistence.postgresql.reporting.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.kie.kogito.persistence.api.Storage;
import org.kie.kogito.persistence.postgresql.PostgresStorageService;
import org.kie.kogito.persistence.postgresql.reporting.model.JsonType;
import org.kie.kogito.persistence.postgresql.reporting.model.PostgresField;
import org.kie.kogito.persistence.postgresql.reporting.model.PostgresJsonField;
import org.kie.kogito.persistence.postgresql.reporting.model.PostgresMapping;
import org.kie.kogito.persistence.postgresql.reporting.model.PostgresMappingDefinition;
import org.kie.kogito.persistence.postgresql.reporting.model.PostgresPartitionField;
import org.kie.kogito.persistence.reporting.service.MappingService;

@ApplicationScoped
public class PostgresMappingServiceImpl implements MappingService<JsonType, PostgresField, PostgresPartitionField, PostgresJsonField, PostgresMapping, PostgresMappingDefinition> {

    static final String CACHE_NAME = "MappingDefinitions";

    private final PostgresStorageService storageService;

    @Inject
    public PostgresMappingServiceImpl(final PostgresStorageService storageService) {
        this.storageService = Objects.requireNonNull(storageService);
    }

    @Override
    public List<PostgresMappingDefinition> getAllMappingDefinitions() {
        final List<PostgresMappingDefinition> mappingDefinitions = new ArrayList<>(storageService.getCache(CACHE_NAME, PostgresMappingDefinition.class).entries().values());
        return Collections.unmodifiableList(mappingDefinitions);
    }

    @Override
    public PostgresMappingDefinition getMappingDefinitionById(final String mappingId) {
        final Storage<String, PostgresMappingDefinition> storage = storageService.getCache(CACHE_NAME, PostgresMappingDefinition.class);
        if (!storage.containsKey(mappingId)) {
            throw new IllegalArgumentException(String.format("A MappingDefinition with ID '%s' cannot be found in the storage.", mappingId));
        }
        return storage.get(mappingId);
    }

    @Override
    @Transactional
    public void saveMappingDefinition(final PostgresMappingDefinition definition) {
        final String mappingId = definition.getMappingId();
        final Storage<String, PostgresMappingDefinition> storage = storageService.getCache(CACHE_NAME, PostgresMappingDefinition.class);
        if (storage.containsKey(mappingId)) {
            throw new IllegalArgumentException(String.format("A MappingDefinition with ID '%s' is already present in the storage.", mappingId));
        }
        storage.put(mappingId, definition);
    }

    @Override
    @Transactional
    public PostgresMappingDefinition deleteMappingDefinitionById(final String mappingId) {
        final Storage<String, PostgresMappingDefinition> storage = storageService.getCache(CACHE_NAME, PostgresMappingDefinition.class);
        if (!storage.containsKey(mappingId)) {
            throw new IllegalArgumentException(String.format("A MappingDefinition with ID '%s' cannot be found in the storage.", mappingId));
        }
        return storage.remove(mappingId);
    }
}
