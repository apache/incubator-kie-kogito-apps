package org.kie.kogito.persistence.api.schema;

import java.util.List;
import java.util.Objects;

public class IndexDescriptor {

    String name;

    List<String> indexAttributes;

    public IndexDescriptor(String name, List<String> indexAttributes) {
        this.name = name;
        this.indexAttributes = indexAttributes;
    }

    public String getName() {
        return name;
    }

    public List<String> getIndexAttributes() {
        return indexAttributes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IndexDescriptor that = (IndexDescriptor) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(indexAttributes, that.indexAttributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, indexAttributes);
    }
}
