import { UserTaskInstance } from '@kogito-apps/task-console-shared';

export interface TaskDetailsEnvelopeApi {
  taskDetails__init(
    association: Association,
    initArgs: TaskDetailsInitArgs
  ): Promise<void>;
}

export interface Association {
  origin: string;
  envelopeServerId: string;
}

export interface TaskDetailsInitArgs {
  task: UserTaskInstance;
}
