package org.kie.kogito.runtime.tools.quarkus.extension.runtime.user;

import java.util.*;

import org.junit.jupiter.api.Test;
import org.kie.kogito.runtime.tools.quarkus.extension.runtime.config.UserConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserInfoSupplierTest {

    @Test
    void testNullGetUserInfo() {
        final UserInfoSupplier userInfoSupplier = new UserInfoSupplier(null);
        final UserInfo userInfo = userInfoSupplier.get();

        assertEquals(0, userInfo.getUsers().size());
        assertEquals("[  ]", userInfo.getArrayRepresentation());
    }

    @Test
    void testEmptyGetUserInfo() {
        final UserInfoSupplier userInfoSupplier = new UserInfoSupplier(Collections.emptyMap());
        final UserInfo userInfo = userInfoSupplier.get();

        assertEquals(0, userInfo.getUsers().size());
        assertEquals("[  ]", userInfo.getArrayRepresentation());
    }

    @Test
    void testNotEmptyGetUserInfo() {
        final UserConfig userA = new UserConfig();
        userA.groups = Collections.singletonList("admin");
        final UserConfig userB = new UserConfig();
        userB.groups = Arrays.asList("admin", "user");

        final Map<String, UserConfig> userConfigByUser = new HashMap<>();
        userConfigByUser.put("userA", userA);
        userConfigByUser.put("userB", userB);

        final UserInfoSupplier userInfoSupplier = new UserInfoSupplier(userConfigByUser);
        final UserInfo userInfo = userInfoSupplier.get();

        assertEquals(2, userInfo.getUsers().size());
        assertEquals("[ { id: 'userA', groups: ['admin'] }, { id: 'userB', groups: ['admin', 'user'] } ]", userInfo.getArrayRepresentation());
    }
}
