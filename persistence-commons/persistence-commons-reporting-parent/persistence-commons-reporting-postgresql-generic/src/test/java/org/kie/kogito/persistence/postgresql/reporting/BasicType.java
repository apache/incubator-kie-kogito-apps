package org.kie.kogito.persistence.postgresql.reporting;

public class BasicType {

    private Integer field1;
    private Long field2;
    private String field3;

    BasicType() {
    }

    public BasicType(final Integer field1,
            final Long field2,
            final String field3) {
        this.field1 = field1;
        this.field2 = field2;
        this.field3 = field3;
    }

    public Integer getField1() {
        return field1;
    }

    public Long getField2() {
        return field2;
    }

    public String getField3() {
        return field3;
    }
}
