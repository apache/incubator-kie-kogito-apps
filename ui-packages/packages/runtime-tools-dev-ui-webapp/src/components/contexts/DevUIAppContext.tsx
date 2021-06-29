/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
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

import React, { useContext } from 'react';
import { User } from '@kogito-apps/consoles-common';

export interface DevUIAppContext {
  getCurrentUser(): User;
  getAllUsers(): User[];
}

export class DevUIAppContextImpl implements DevUIAppContext {
  private users: User[];
  private currentUser: User;

  constructor(users) {
    this.users = users;
    if (users.length > 1) {
      this.currentUser = users[0];
    }
  }

  getCurrentUser(): User {
    return this.currentUser;
  }

  getAllUsers(): User[] {
    return this.users;
  }
}

const RuntimeToolsDevUIAppContext = React.createContext<DevUIAppContext>(null);

export default RuntimeToolsDevUIAppContext;

export const useDevUIAppContext = () =>
  useContext<DevUIAppContext>(RuntimeToolsDevUIAppContext);
