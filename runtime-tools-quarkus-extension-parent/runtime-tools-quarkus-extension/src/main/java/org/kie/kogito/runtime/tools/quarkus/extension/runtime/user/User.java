package org.kie.kogito.runtime.tools.quarkus.extension.runtime.user;

import java.util.List;

public class User {

    private String id;

    private List<String> groups;

    public User(final String id,
            final List<String> groups) {
        this.id = id;
        this.groups = groups;
    }

    public String getId() {
        return id;
    }

    public List<String> getGroups() {
        return groups;
    }
}
