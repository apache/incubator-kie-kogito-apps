/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

    public String getMapRepresentation() {
        return "new Map([ " + users.stream().map(user -> "[\"" + user.getUsername() + "\", [" + user.getRoles().stream().map(role -> "\"" + role + "\"").collect(Collectors.joining(", ")) + "] ]")
                .collect(Collectors.joining(", ")) + " ] )";
    }
}
