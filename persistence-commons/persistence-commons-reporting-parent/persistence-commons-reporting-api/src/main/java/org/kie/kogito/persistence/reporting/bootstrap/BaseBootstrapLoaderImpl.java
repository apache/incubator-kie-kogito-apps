package org.kie.kogito.persistence.reporting.bootstrap;

import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

import org.kie.kogito.event.cloudevents.utils.CloudEventUtils;
import org.kie.kogito.persistence.reporting.model.Field;
import org.kie.kogito.persistence.reporting.model.JsonField;
import org.kie.kogito.persistence.reporting.model.Mapping;
import org.kie.kogito.persistence.reporting.model.MappingDefinition;
import org.kie.kogito.persistence.reporting.model.MappingDefinitions;
import org.kie.kogito.persistence.reporting.model.PartitionField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseBootstrapLoaderImpl<T, F extends Field, P extends PartitionField, J extends JsonField<T>, M extends Mapping<T, J>, D extends MappingDefinition<T, F, P, J, M>, S extends MappingDefinitions<T, F, P, J, M, D>>
        implements BootstrapLoader<T, F, P, J, M, D, S> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseBootstrapLoaderImpl.class);

    private final Supplier<InputStream> inputStreamSupplier;

    @SuppressWarnings("unused")
    protected BaseBootstrapLoaderImpl() {
        this(() -> BaseBootstrapLoaderImpl.class.getResourceAsStream("/bootstrap.json"));
    }

    protected BaseBootstrapLoaderImpl(final Supplier<InputStream> inputStreamSupplier) {
        this.inputStreamSupplier = Objects.requireNonNull(inputStreamSupplier);
    }

    @Override
    public Optional<S> load() {
        LOGGER.debug("Loading Mapping Definitions.");

        try (InputStream is = inputStreamSupplier.get()) {
            final S mappingDefinitions = CloudEventUtils.Mapper.mapper().readValue(is, getMappingDefinitionsType());
            return Optional.ofNullable(mappingDefinitions);
        } catch (Exception e) {
            LOGGER.error(String.format("Failed to load Mapping Definitions: %s",
                    e.getMessage()));
        }
        return Optional.empty();
    }
}
