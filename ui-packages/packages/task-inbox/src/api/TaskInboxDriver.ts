import { UserTaskInstance } from '@kogito-apps/task-console-shared';
import { QueryFilter, SortBy, TaskInboxState } from './TaskInboxEnvelopeApi';

/**
 * Interface that defines a Driver for TaskInbox views.
 */
export interface TaskInboxDriver {
  setInitialState(taskInboxState: TaskInboxState): Promise<void>;
  applyFilter(filter: QueryFilter): Promise<void>;
  applySorting(sortBy: SortBy): Promise<void>;
  query(offset: number, limit: number): Promise<UserTaskInstance[]>;
  openTask(task: UserTaskInstance): void;
}
