package org.kie.kogito.index.addon.config;

import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;

@ConfigRoot(name = "kogito.data-index", phase = ConfigPhase.BUILD_TIME)
public class DataIndexBuildConfig {

    /**
     * If GraphQL UI should be enabled. By default, this is only included when the application is running in dev mode.
     */
    @ConfigItem(name = "graphql.ui.always-include", defaultValue = "false")
    public boolean graphqlUIEnabled;

}
