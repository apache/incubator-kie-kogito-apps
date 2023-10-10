package org.kie.kogito.runtime.tools.quarkus.extension.runtime.user;

import java.util.List;
import java.util.stream.Collectors;

public class UserInfo {

    private List<User> users;

    public UserInfo(final List<User> users) {
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }

    public String getArrayRepresentation() {
        return "[ "
                + users.stream().map(user -> "{ id: '" + user.getId() + "', groups: [" + user.getGroups().stream().map(group -> "'" + group + "'").collect(Collectors.joining(", ")) + "] }")
                        .collect(Collectors.joining(", "))
                + " ]";
    }
}
