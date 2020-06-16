package org.kie.kogito.trusty.service;

import org.kie.kogito.persistence.infinispan.InfinispanServerTestResource;

public class TrustyInfinispanServerTestResource extends InfinispanServerTestResource {
        @Override
        public boolean shouldCleanCache(String cacheName) {
            return cacheName.equals("processinstances")
                    || cacheName.endsWith("_domain")
                    || cacheName.equals("jobs")
                    || cacheName.equals("usertaskinstances");
        }
    }