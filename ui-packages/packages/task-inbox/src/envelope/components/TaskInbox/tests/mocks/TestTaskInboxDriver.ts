import { UserTaskInstance } from '@kogito-apps/task-console-shared';
import {
  QueryFilter,
  SortBy,
  TaskInboxDriver,
  TaskInboxState
} from '../../../../../api';

export default class TestTaskInboxDriver implements TaskInboxDriver {
  private readonly tasks: UserTaskInstance[];
  private offset: number = 0;
  private limit: number = 10;

  constructor(tasks: UserTaskInstance[], initialState?: TaskInboxState) {
    this.tasks = tasks;
    if (initialState) {
      this.doSetState(initialState);
    }
  }

  private doSetState(initialState: TaskInboxState) {
    // do nothing
  }

  setInitialState(taskInboxState: TaskInboxState): Promise<void> {
    this.doSetState(taskInboxState);
    return Promise.resolve();
  }

  applyFilter(filter: QueryFilter): Promise<void> {
    // do nothing
    return Promise.resolve();
  }

  applySorting(sortBy: SortBy): Promise<void> {
    // do nothing
    return Promise.resolve(undefined);
  }

  openTask(task: UserTaskInstance): void {
    // do nothing
  }

  query(offset: number, limit: number): Promise<UserTaskInstance[]> {
    this.offset = offset;
    this.limit = limit;
    return this.doQuery(offset, this.getQueryLimit());
  }

  private doQuery(start: number, limit: number): Promise<UserTaskInstance[]> {
    const queryLimit = limit > this.tasks.length ? this.tasks.length : limit;

    return Promise.resolve(this.tasks.slice(start, queryLimit));
  }

  private getQueryLimit = (): number => {
    return this.offset + this.limit;
  };
}
