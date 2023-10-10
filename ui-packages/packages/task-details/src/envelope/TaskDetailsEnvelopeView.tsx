import * as React from 'react';
import { useImperativeHandle, useState } from 'react';

import { TaskDetailsChannelApi } from '../api';
import { MessageBusClientApi } from '@kie-tools-core/envelope-bus/dist/api';
import { UserTaskInstance } from '@kogito-apps/task-console-shared';
import TaskDetails from './component/TaskDetails';

import '@patternfly/patternfly/patternfly.css';

export interface TaskDetailsEnvelopeViewApi {
  setTask(task: UserTaskInstance): void;
}

interface Props {
  channelApi: MessageBusClientApi<TaskDetailsChannelApi>;
}

export const TaskDetailsEnvelopeView = React.forwardRef<
  TaskDetailsEnvelopeViewApi,
  Props
>((props, forwardedRef) => {
  const [task, setTask] = useState<UserTaskInstance>();

  useImperativeHandle(
    forwardedRef,
    () => {
      return {
        setTask: (userTask: UserTaskInstance) => {
          setTask(userTask);
        }
      };
    },
    []
  );

  return <TaskDetails userTask={task} />;
});
