package org.kie.kogito.persistence.oracle.model;

import java.io.Serializable;
import java.util.Objects;

public class CacheId implements Serializable {

    private String name;
    private String key;

    public CacheId() {
    }

    public CacheId(String name, String key) {
        this.name = name;
        this.key = key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CacheId)) {
            return false;
        }
        CacheId cacheId = (CacheId) o;
        return Objects.equals(name, cacheId.name) && Objects.equals(key, cacheId.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, key);
    }
}
