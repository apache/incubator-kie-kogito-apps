package org.kie.kogito.persistence.api.query;

public enum FilterCondition {

    IN("in"),
    CONTAINS("contains"),
    CONTAINS_ALL("containsAll"),
    CONTAINS_ANY("containsAny"),
    LIKE("like"),
    IS_NULL("isNull"),
    NOT_NULL("notNull"),
    EQUAL("equal"),
    GT("greaterThan"),
    GTE("greaterThanEqual"),
    LT("lessThan"),
    LTE("lessThanEqual"),
    BETWEEN("between"),
    AND("and"),
    OR("or"),
    NOT("not");

    private String label;

    FilterCondition(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static FilterCondition fromLabel(String label) {
        for (FilterCondition c : FilterCondition.values()) {
            if (c.getLabel().equals(label)) {
                return c;
            }
        }
        return null;
    }
}
