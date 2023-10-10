package org.kie.kogito.runtime.tools.quarkus.extension.runtime.config;

import java.util.Map;

import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;

@ConfigRoot(name = "", prefix = "kogito", phase = ConfigPhase.BUILD_AND_RUN_TIME_FIXED)
public class DevConsoleRuntimeConfig {

    /**
     * Mocked users data for the task inbox screen.
     */
    @ConfigItem(name = "users")
    public Map<String, UserConfig> userConfigByUser;
}
