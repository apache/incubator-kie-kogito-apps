import cloneDeep from 'lodash/cloneDeep';
import unset from 'lodash/unset';
import { User } from '../../../../api';
import { UserTaskInstance } from '@kogito-apps/task-console-shared';

export function buildTaskFormContext(
  userTask: UserTaskInstance,
  schema: Record<string, any>,
  user: User
): Record<string, any> {
  const ctxSchema = cloneDeep(schema);

  const ctxPhases = ctxSchema.phases;

  unset(ctxSchema, 'phases');

  const ctxTask = cloneDeep(userTask);

  unset(ctxTask, 'actualOwner');
  unset(ctxTask, 'adminGroups');
  unset(ctxTask, 'adminUsers');
  unset(ctxTask, 'excludedUsers');
  unset(ctxTask, 'potentialGroups');
  unset(ctxTask, 'potentialUsers');
  unset(ctxTask, 'inputs');
  unset(ctxTask, 'outputs');
  unset(ctxTask, 'endpoint');

  return {
    user: user,
    task: ctxTask,
    schema: ctxSchema,
    phases: ctxPhases
  };
}
