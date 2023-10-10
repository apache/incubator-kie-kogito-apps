package org.kie.kogito.index.mongodb.model;

import org.bson.Document;
import org.kie.kogito.persistence.mongodb.model.ModelUtils;
import org.kie.kogito.persistence.mongodb.model.MongoEntityMapper;

import com.fasterxml.jackson.databind.node.ObjectNode;

import static org.kie.kogito.persistence.mongodb.model.ModelUtils.MAPPER;
import static org.kie.kogito.persistence.mongodb.model.ModelUtils.MONGO_ID;
import static org.kie.kogito.persistence.mongodb.model.ModelUtils.jsonNodeToDocument;

public class DomainEntityMapper implements MongoEntityMapper<ObjectNode, Document> {

    static final String ID = "id";

    @Override
    public Class<Document> getEntityClass() {
        return Document.class;
    }

    @Override
    public Document mapToEntity(String key, ObjectNode value) {
        if (value == null) {
            return null;
        }

        ObjectNode n = value.deepCopy();
        n.remove(ID);
        return jsonNodeToDocument(n).append(MONGO_ID, key);
    }

    @Override
    public ObjectNode mapToModel(Document entity) {
        if (entity == null) {
            return null;
        }

        Object idObj = entity.remove(MONGO_ID);
        if (idObj != null) {
            ObjectNode result = MAPPER.createObjectNode();
            result.put(ID, idObj.toString());
            result.setAll(ModelUtils.documentToJsonNode(entity));
            return result;
        }
        return ModelUtils.documentToJsonNode(entity);
    }
}
