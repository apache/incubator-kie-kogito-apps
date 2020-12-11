package org.kie.kogito.persistence.redis.index;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import io.redisearch.Schema;
import io.redisearch.client.Client;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.exceptions.JedisDataException;

@ApplicationScoped
public class RedisIndexManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisIndexManager.class);

    @ConfigProperty(name = "kogito.persistence.redis.host", defaultValue = "localhost")
    private String host;
    @ConfigProperty(name = "kogito.persistence.redis.port", defaultValue = "6379")
    private int port;

    private Map<String, List<String>> indexes = new HashMap<>();

    public void createIndex(RedisCreateIndexEvent event) {
        Client client = getClient(event.indexName);
        List<String> fields = new ArrayList<>();
        Schema schema = new Schema();
        if (!event.isPrimitiveType){
            for (Schema.Field field : event.fields) {
                fields.add(field.name);
                schema.addField(field);
            }
        }
        else{ // Add a dummy entry to make it happy
            schema.addField(new Schema.Field("myKey", Schema.FieldType.FullText, false ));
        }

        indexes.put(event.indexName, fields);

        try{
            client.createIndex(schema, Client.IndexOptions.defaultOptions());
        }
        catch (JedisDataException ignored){
            LOGGER.warn("something went wrong.", ignored);
        }
    }

    public List<String> getSchema(String indexName){
        return indexes.get(indexName);
    }

    public Client getClient(String indexName){
        return new Client(indexName, host, port);
    }
}