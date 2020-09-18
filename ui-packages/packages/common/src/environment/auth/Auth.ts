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

export interface User {
  id: string;
  groups: string[];
}

export interface UserSystem {
  logout();
  getCurrentUser(): User;
}

export interface TestUserManager {
  listUsers(): User[];
  listAllUsers(): User[];
  systemUsers(): string[];
  addUser(userId: string, groups: string[]): void;
  removeUser(userId: string);
  getUser(userId: string): User;
}

export interface TestUserSystem extends UserSystem {
  getUserManager(): TestUserManager;
  su(userId: string);
}

export class DefaultUser implements User {
  public readonly id: string;
  public readonly groups: string[];

  public constructor(id: string, groups: string[]) {
    this.id = id;
    this.groups = groups;
  }
}
