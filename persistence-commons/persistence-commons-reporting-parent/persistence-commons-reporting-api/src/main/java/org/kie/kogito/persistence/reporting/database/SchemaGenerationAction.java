package org.kie.kogito.persistence.reporting.database;

import java.util.Arrays;

/**
 * An enumerated encapsulation of JPA's Schema Generation text values
 * See https://jakarta.ee/specifications/persistence/3.0/jakarta-persistence-spec-3.0.html#a12917
 */
public enum SchemaGenerationAction {

    //No schema creation or deletion will take place.
    NONE("none"),
    //The provider will create the database artifacts on application deployment. The artifacts will remain unchanged after application redeployment.
    CREATE("create"),
    //Any artifacts in the database will be deleted, and the provider will create the database artifacts on deployment.
    DROP_AND_CREATE("drop-and-create"),
    //Any artifacts in the database will be deleted on application deployment.
    DROP("drop");

    private final String action;

    SchemaGenerationAction(final String action) {
        this.action = action;
    }

    public String getActionString() {
        return action;
    }

    public static SchemaGenerationAction fromString(final String action) {
        return Arrays
                .stream(SchemaGenerationAction
                        .values())
                .filter(s -> s.action.equalsIgnoreCase(action))
                .findFirst()
                .orElse(SchemaGenerationAction.NONE);
    }
}
