package org.kie.kogito.persistence.postgresql.reporting;

import java.util.Collection;

public class HierarchicalType {

    private String root;
    private BasicType nestedBasic;
    private Collection<BasicType> nestedBasicCollection;

    HierarchicalType() {
    }

    public HierarchicalType(final String root,
            final BasicType nestedBasic,
            final Collection<BasicType> nestedBasicCollection) {
        this.root = root;
        this.nestedBasic = nestedBasic;
        this.nestedBasicCollection = nestedBasicCollection;
    }

    public String getRoot() {
        return root;
    }

    public BasicType getNestedBasic() {
        return nestedBasic;
    }

    public Collection<BasicType> getNestedBasicCollection() {
        return nestedBasicCollection;
    }
}
