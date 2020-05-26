package org.kie.kogito.trusty.storage.infinispan;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TrustyCacheDefaultConfigTest {
    @Test
    public void GivenTheTrustyCacheConfig_WhenDefaultCacheConfigIsRetrieved_TheConfigIsGenerated(){
        Assertions.assertDoesNotThrow(() -> new TrustyCacheDefaultConfig("test"));
    }
}
