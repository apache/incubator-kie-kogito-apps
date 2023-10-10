package org.kie.kogito.persistence.mongodb.client;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.bson.Document;
import org.eclipse.microprofile.config.ConfigProvider;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@ApplicationScoped
public class MongoClientManager {

    private static final String DATABASE_PROPERTY = "quarkus.mongodb.database";

    String database;

    @Inject
    MongoClient mongoClient;

    public MongoClientManager() {
        database = ConfigProvider.getConfig().getValue(DATABASE_PROPERTY, String.class);
    }

    public <E> MongoCollection<E> getCollection(String collection, Class<E> type) {
        return getMongoDatabase().getCollection(collection, type);
    }

    public MongoCollection<Document> getCollection(String collection) {
        return getMongoDatabase().getCollection(collection);
    }

    private MongoDatabase getMongoDatabase() {
        return mongoClient.getDatabase(database);
    }
}
