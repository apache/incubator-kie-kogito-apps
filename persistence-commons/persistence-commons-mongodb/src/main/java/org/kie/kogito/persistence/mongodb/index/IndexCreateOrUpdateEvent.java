package org.kie.kogito.persistence.mongodb.index;

import java.util.Objects;

public class IndexCreateOrUpdateEvent {

    String collection;

    String index;

    public IndexCreateOrUpdateEvent(String collection, String index) {
        this.collection = collection;
        this.index = index;
    }

    public String getCollection() {
        return collection;
    }

    public String getIndex() {
        return index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IndexCreateOrUpdateEvent that = (IndexCreateOrUpdateEvent) o;
        return Objects.equals(collection, that.collection) &&
                Objects.equals(index, that.index);
    }

    @Override
    public int hashCode() {
        return Objects.hash(collection, index);
    }
}
