import { UserTaskInstance } from '@kogito-apps/task-console-shared';
import {
  QueryFilter,
  SortBy,
  TaskInboxChannelApi,
  TaskInboxDriver,
  TaskInboxState
} from '../api';

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

  taskInbox__openTask(task: UserTaskInstance): void {
    this.driver.openTask(task);
  }
}
