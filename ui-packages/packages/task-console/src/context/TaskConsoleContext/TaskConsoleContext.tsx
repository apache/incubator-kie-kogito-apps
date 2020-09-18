/**
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
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
import React from 'react';
import { User } from '@kogito-apps/common';

export interface IContext<T> {
  getUser(): User;
  setActiveItem(item: any);
  getActiveItem(): any;
}

export class DefaultContext<T> implements IContext<T> {
  private user: User;
  private item: T;

  constructor(user: User) {
    this.user = user;
  }

  getUser(): User {
    return this.user;
  }

  getActiveItem(): T {
    return this.item;
  }

  setActiveItem(item: T) {
    this.item = item;
  }
}

const TaskConsoleContext = React.createContext<IContext<any>>(null);

export default TaskConsoleContext;
