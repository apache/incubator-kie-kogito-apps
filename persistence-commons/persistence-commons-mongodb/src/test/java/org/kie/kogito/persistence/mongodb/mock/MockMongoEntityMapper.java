package org.kie.kogito.persistence.mongodb.mock;

import org.bson.Document;
import org.kie.kogito.persistence.mongodb.model.MongoEntityMapper;

import static org.kie.kogito.persistence.mongodb.model.ModelUtils.MONGO_ID;

public class MockMongoEntityMapper implements MongoEntityMapper<String, Document> {

    public static final String TEST_ATTRIBUTE = "test_attribute";

    @Override
    public Class<Document> getEntityClass() {
        return Document.class;
    }

    @Override
    public Document mapToEntity(String key, String value) {
        return new Document(MONGO_ID, key).append(TEST_ATTRIBUTE, value);
    }

    @Override
    public String mapToModel(Document entity) {
        return entity.getString(TEST_ATTRIBUTE);
    }
}
