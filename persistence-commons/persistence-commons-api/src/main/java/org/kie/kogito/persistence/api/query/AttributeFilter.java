package org.kie.kogito.persistence.api.query;

public class AttributeFilter<T> {

    private String attribute;

    private FilterCondition condition;

    private T value;

    protected AttributeFilter(String attribute, FilterCondition condition, T value) {
        this.attribute = attribute;
        this.condition = condition;
        this.value = value;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public FilterCondition getCondition() {
        return condition;
    }

    public void setCondition(FilterCondition condition) {
        this.condition = condition;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AttributeFilter)) {
            return false;
        }

        AttributeFilter<?> filter = (AttributeFilter<?>) o;

        if (getAttribute() != null ? !getAttribute().equals(filter.getAttribute()) : filter.getAttribute() != null) {
            return false;
        }
        if (getCondition() != filter.getCondition()) {
            return false;
        }
        return getValue() != null ? getValue().equals(filter.getValue()) : filter.getValue() == null;
    }

    @Override
    public int hashCode() {
        int result = getAttribute() != null ? getAttribute().hashCode() : 0;
        result = 31 * result + (getCondition() != null ? getCondition().hashCode() : 0);
        result = 31 * result + (getValue() != null ? getValue().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AttributeFilter{" +
                "column='" + attribute + '\'' +
                ", condition=" + condition +
                ", value=" + value +
                '}';
    }
}
