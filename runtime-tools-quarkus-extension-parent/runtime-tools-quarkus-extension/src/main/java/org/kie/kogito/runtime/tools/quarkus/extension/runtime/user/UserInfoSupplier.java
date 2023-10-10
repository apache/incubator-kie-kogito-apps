package org.kie.kogito.runtime.tools.quarkus.extension.runtime.user;

import java.util.Collections;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.kie.kogito.runtime.tools.quarkus.extension.runtime.config.UserConfig;

public class UserInfoSupplier implements Supplier<UserInfo> {

    private final Map<String, UserConfig> userConfigByUser;

    public UserInfoSupplier(final Map<String, UserConfig> userConfigByUser) {
        this.userConfigByUser = userConfigByUser;
    }

    public UserInfo get() {
        if (userConfigByUser == null || userConfigByUser.size() == 0) {
            return new UserInfo(Collections.emptyList());
        }

        return new UserInfo(userConfigByUser.entrySet().stream()
                .map(entry -> new User(entry.getKey(), entry.getValue().groups)).collect(Collectors.toList()));
    }
}
