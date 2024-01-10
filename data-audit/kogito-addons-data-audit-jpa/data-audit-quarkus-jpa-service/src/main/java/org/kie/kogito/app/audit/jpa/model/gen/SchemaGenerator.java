package org.kie.kogito.app.audit.jpa.model.gen;

import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.Persistence;

public class SchemaGenerator {

    public static void main(String[] args) {
        Map<String, String> props = new HashMap<>();
        props.put("javax.persistence.schema-generation.scripts.action", "create");
        props.put("javax.persistence.schema-generation.scripts.create-target", "target/create.sql");
        props.put("jakarta.persistence.jdbc.url", "jdbc:postgresql://localhost:5432/kogito");
        props.put("jakarta.persistence.jdbc.user", "kogito-user");
        props.put("jakarta.persistence.jdbc.password", "kogito-pass");
        props.put("jakarta.persistence.schema-generation.database.action", "create");

        Persistence.generateSchema("DataAuditPU", props);
    }
}
