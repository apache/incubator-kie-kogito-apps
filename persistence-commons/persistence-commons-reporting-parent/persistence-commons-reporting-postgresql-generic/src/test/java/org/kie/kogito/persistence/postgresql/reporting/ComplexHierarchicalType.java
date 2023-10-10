package org.kie.kogito.persistence.postgresql.reporting;

import java.util.Collection;

public class ComplexHierarchicalType {

    private String root;
    private BasicType nestedBasic;
    private Collection<ComplexHierarchicalType> nestedComplexCollection;

    ComplexHierarchicalType() {
    }

    public ComplexHierarchicalType(final String root,
            final BasicType nestedBasic,
            final Collection<ComplexHierarchicalType> nestedComplexCollection) {
        this.root = root;
        this.nestedBasic = nestedBasic;
        this.nestedComplexCollection = nestedComplexCollection;
    }

    public String getRoot() {
        return root;
    }

    public BasicType getNestedBasic() {
        return nestedBasic;
    }

    public Collection<ComplexHierarchicalType> getNestedComplexCollection() {
        return nestedComplexCollection;
    }
}
