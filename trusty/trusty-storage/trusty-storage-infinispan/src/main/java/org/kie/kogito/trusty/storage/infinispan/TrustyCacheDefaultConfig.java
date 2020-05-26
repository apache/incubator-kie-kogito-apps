package org.kie.kogito.trusty.storage.infinispan;

import java.io.InputStream;
import java.util.Scanner;

import org.infinispan.commons.configuration.XMLStringConfiguration;

/**
 * Default process event cache configuration
 */
public class TrustyCacheDefaultConfig extends XMLStringConfiguration {

    private static final String CACHE_CONFIG_PATH = "kogito-cache-default.xml";
    private static final String CACHE_NAME_PLACEHOLDER = "${cache_name}";

    public TrustyCacheDefaultConfig(final String cacheName) {
        super(defaultCacheTemplate(cacheName));
    }

    private static final String defaultCacheTemplate(final String cacheName) {
        final InputStream is = TrustyCacheDefaultConfig.class.getClassLoader().getResourceAsStream(CACHE_CONFIG_PATH);
        if (is == null) {
            throw new IllegalArgumentException(String.format("Cache configuration file %s not found", CACHE_CONFIG_PATH));
        }

        try (Scanner s = new Scanner(is)) {
            s.useDelimiter("\\A");
            final String xmlCacheConfig = s.hasNext() ? s.next() : "";
            return xmlCacheConfig.replace(CACHE_NAME_PLACEHOLDER, cacheName);
        }
    }
}