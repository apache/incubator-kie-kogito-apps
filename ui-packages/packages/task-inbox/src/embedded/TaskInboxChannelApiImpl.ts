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

import {
  QueryFilter,
  SortBy,
  TaskInboxChannelApi,
  TaskInboxDriver,
  TaskInboxState
} from '../api';
import { UserTaskInstance } from '../types';

/**
 * Implementation of the TaskInboxChannelApi delegating to a TaskInboxDriver
 */
export class TaskInboxChannelApiImpl implements TaskInboxChannelApi {
  constructor(private readonly driver: TaskInboxDriver) {}

  taskInbox__setInitialState(initialState: TaskInboxState): Promise<void> {
    return this.driver.setInitialState(initialState);
  }

  taskInbox__applyFilter(filter: QueryFilter): Promise<void> {
    return this.driver.applyFilter(filter);
  }

  taskInbox__applySorting(sortBy: SortBy): Promise<void> {
    return this.driver.applySorting(sortBy);
  }

  taskInbox__query(offset: number, limit: number): Promise<UserTaskInstance[]> {
    return this.driver.query(offset, limit);
  }

  taskInbox__refresh(): Promise<UserTaskInstance[]> {
    return this.driver.refresh();
  }

  taskInbox__openTask(task: UserTaskInstance): void {
    this.driver.openTask(task);
  }
}
