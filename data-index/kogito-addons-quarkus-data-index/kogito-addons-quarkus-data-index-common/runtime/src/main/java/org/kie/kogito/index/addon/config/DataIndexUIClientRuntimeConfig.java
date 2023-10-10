package org.kie.kogito.index.addon.config;

import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;

@ConfigRoot(prefix = "kogito", name = "dataindex", phase = ConfigPhase.RUN_TIME)
public class DataIndexUIClientRuntimeConfig {

    /**
     * Data Index HTTP URL
     */
    @ConfigItem(name = "http.url", defaultValue = "http://localhost:${quarkus.http.port}")
    public String dataIndexHttpUrl;

    /**
     * Data Index WS URL
     */
    @ConfigItem(name = "ws.url", defaultValue = "ws://localhost:${quarkus.http.port}")
    public String dataIndexWebsocketUrl;

}
