import { UserTaskInstance } from '@kogito-apps/task-console-shared';

export interface TaskFormEnvelopeApi {
  taskForm__init(
    association: Association,
    initArgs: TaskFormInitArgs
  ): Promise<void>;
}

export interface Association {
  origin: string;
  envelopeServerId: string;
}

export interface TaskFormInitArgs {
  userTask: UserTaskInstance;
  user: User;
}

export interface User {
  id: string;
  groups: string[];
}
