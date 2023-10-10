package org.kie.kogito.persistence.api.query;

public class AttributeSort {

    private String attribute;

    private SortDirection sort;

    protected AttributeSort(String attribute, SortDirection sort) {
        this.attribute = attribute;
        this.sort = sort;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public SortDirection getSort() {
        return sort;
    }

    public void setSort(SortDirection sort) {
        this.sort = sort;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AttributeSort)) {
            return false;
        }

        AttributeSort that = (AttributeSort) o;

        if (getAttribute() != null ? !getAttribute().equals(that.getAttribute()) : that.getAttribute() != null) {
            return false;
        }
        return getSort() == that.getSort();
    }

    @Override
    public int hashCode() {
        int result = getAttribute() != null ? getAttribute().hashCode() : 0;
        result = 31 * result + (getSort() != null ? getSort().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AttributeSort{" +
                "attribute='" + attribute + '\'' +
                ", sort=" + sort +
                '}';
    }
}
