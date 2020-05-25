package org.kie.kogito.trusty.storage.mongo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.kie.kogito.trusty.storage.api.IStorageManager;
import org.kie.kogito.trusty.storage.api.TrustyStorageQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class MongoStorageManager implements IStorageManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoStorageManager.class);

    @Inject
    MongoClient defaultMongoClient;

    private MongoDatabase database;

    public MongoStorageManager(MongoDatabase mongoDatabase) {
        this.database = mongoDatabase;
    }

    @PostConstruct
    void setup() {
        database = defaultMongoClient.getDatabase("TrustyDB");
    }

    @Override
    public <T> boolean create(String key, T request, String index) {
        MongoCollection<T> collection = getOrCreateCollection(index, request.getClass());

        collection.insertOne(request);

        return true;
    }

    @Override
    public <T> List<T> search(TrustyStorageQuery query, String index, Class<T> type) {
        MongoCollection<T> collection = getOrCreateCollection(index, type);

        Iterator<T> i = collection.find(MongoQueryFactory.build(query)).iterator();
        List<T> copy = new ArrayList<T>();
        while (i.hasNext()) {
            copy.add(i.next());
        }

        return copy;
    }

    private MongoCollection getOrCreateCollection(String collection, Class type) {
        return database.getCollection(collection, type);
    }
}

