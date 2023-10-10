package org.kie.kogito.index.addon.config;

import java.util.Optional;

import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;

@ConfigRoot(prefix = "kogito", name = "data-index", phase = ConfigPhase.RUN_TIME)
public class DataIndexRuntimeConfig {

    /**
     * Data Index URL
     */
    @ConfigItem(name = "url")
    public Optional<String> dataIndexUrl;

}
