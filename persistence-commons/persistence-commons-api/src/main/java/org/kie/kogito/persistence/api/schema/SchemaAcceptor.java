package org.kie.kogito.persistence.api.schema;

public interface SchemaAcceptor {

    boolean accept(SchemaType type);
}
