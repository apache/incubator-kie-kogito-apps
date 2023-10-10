package org.kie.kogito.persistence.postgresql.reporting.database.sqlbuilders;

public class DynamicType {

    private String field1;
    private Integer field2;

    DynamicType() {
    }

    public DynamicType(final String field1,
            final Integer field2) {
        this.field1 = field1;
        this.field2 = field2;
    }

    public String getField1() {
        return field1;
    }

    public Integer getField2() {
        return field2;
    }
}
