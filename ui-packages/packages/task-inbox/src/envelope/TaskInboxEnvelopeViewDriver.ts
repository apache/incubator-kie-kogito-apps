import { MessageBusClientApi } from '@kie-tools-core/envelope-bus/dist/api';
import { UserTaskInstance } from '@kogito-apps/task-console-shared';
import {
  QueryFilter,
  SortBy,
  TaskInboxChannelApi,
  TaskInboxDriver,
  TaskInboxState
} from '../api';

/**
 * Implementation of TaskInboxDriver that delegates calls to the channel Api
 */
export default class TaskInboxEnvelopeViewDriver implements TaskInboxDriver {
  constructor(
    private readonly channelApi: MessageBusClientApi<TaskInboxChannelApi>
  ) {}

  setInitialState(taskInboxState: TaskInboxState): Promise<void> {
    return this.channelApi.requests.taskInbox__setInitialState(taskInboxState);
  }

  applyFilter(filter: QueryFilter): Promise<void> {
    return this.channelApi.requests.taskInbox__applyFilter(filter);
  }

  applySorting(sortBy: SortBy): Promise<void> {
    return this.channelApi.requests.taskInbox__applySorting(sortBy);
  }

  query(offset: number, limit: number): Promise<UserTaskInstance[]> {
    return this.channelApi.requests.taskInbox__query(offset, limit);
  }

  openTask(task: UserTaskInstance): void {
    return this.channelApi.notifications.taskInbox__openTask.send(task);
  }
}
