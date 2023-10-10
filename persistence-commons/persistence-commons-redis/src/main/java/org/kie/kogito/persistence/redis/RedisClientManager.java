package org.kie.kogito.persistence.redis;

import java.net.URL;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.redisearch.Client;

@ApplicationScoped
public class RedisClientManager {

    @ConfigProperty(name = "kogito.persistence.redis.url", defaultValue = "http://localhost:6379")
    private URL url;

    public Client getClient(String indexName) {
        return new io.redisearch.client.Client(indexName, url.getHost(), url.getPort());
    }
}
