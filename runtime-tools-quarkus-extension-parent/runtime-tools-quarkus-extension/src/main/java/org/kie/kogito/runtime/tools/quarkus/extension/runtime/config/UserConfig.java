package org.kie.kogito.runtime.tools.quarkus.extension.runtime.config;

import java.util.List;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;

@ConfigGroup
public class UserConfig {

    /**
     * Groups which the user belong to.
     */
    @ConfigItem(name = "groups")
    public List<String> groups;
}
