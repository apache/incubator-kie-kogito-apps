/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import { TestUserSystem, User, TestUserManager } from './Auth';
import { TEST_USERS, TestUserManagerImpl } from './TestUserManager';

/**
 * Test implementation of a UserSystem
 */
export class TestUserSystemImpl implements TestUserSystem {
  private static readonly STORAGE_KEY: string = 'kogito-test-user-system';

  private readonly userManager: TestUserManager;
  private readonly onUserChange: (user: User) => void;

  private currentUser: User;

  constructor(userConsumer: (user: User) => void) {
    this.onUserChange = userConsumer;

    const stateStr: string = window.sessionStorage.getItem(
      TestUserSystemImpl.STORAGE_KEY
    );

    if (stateStr) {
      const state: State = JSON.parse(stateStr);
      this.userManager = new TestUserManagerImpl(state.users);
      this.currentUser = this.userManager.getUser(state.currentUser);
    } else {
      this.userManager = new TestUserManagerImpl();
      this.currentUser = TEST_USERS[0];
    }
  }

  public logout() {
    this.su(TEST_USERS[0].id);
  }

  getCurrentUser(): User {
    return this.currentUser;
  }

  su(userId: string) {
    const user = this.userManager.getUser(userId);

    if (user) {
      this.currentUser = user;

      const state: State = {
        currentUser: userId,
        users: this.userManager.listUsers()
      };

      window.sessionStorage.setItem(
        TestUserSystemImpl.STORAGE_KEY,
        JSON.stringify(state)
      );
      this.onUserChange(this.currentUser);
    }
  }

  public getUserManager(): TestUserManager {
    return this.userManager;
  }
}

interface State {
  users: User[];
  currentUser: string;
}
