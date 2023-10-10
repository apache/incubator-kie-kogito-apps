import React, { useEffect } from 'react';
import { useHistory } from 'react-router-dom';
import {
  componentOuiaProps,
  OUIAProps
} from '@kogito-apps/ouia-tools/dist/utils/OuiaUtils';
import { EmbeddedTaskInbox } from '@kogito-apps/task-inbox';
import { TaskInboxGatewayApi } from '../../../../../channel/inbox';
import { useTaskInboxGatewayApi } from '../../../../../channel/inbox/TaskInboxContext';
import {
  getActiveTaskStates,
  getAllTaskStates
} from '../../../../../utils/Utils';
import { GraphQL } from '@kogito-apps/consoles-common/dist/graphql';
import UserTaskInstance = GraphQL.UserTaskInstance;

const TaskInboxContainer: React.FC<OUIAProps> = ({ ouiaId, ouiaSafe }) => {
  const history = useHistory();
  const gatewayApi: TaskInboxGatewayApi = useTaskInboxGatewayApi();

  useEffect(() => {
    const unsubscriber = gatewayApi.onOpenTaskListen({
      onOpen(task: UserTaskInstance) {
        history.push(`/TaskDetails/${task.id}`);
      }
    });

    return () => {
      unsubscriber.unSubscribe();
    };
  }, []);

  return (
    <EmbeddedTaskInbox
      {...componentOuiaProps(ouiaId, 'task-inbox-container', ouiaSafe)}
      initialState={gatewayApi.taskInboxState}
      driver={gatewayApi}
      allTaskStates={getAllTaskStates()}
      activeTaskStates={getActiveTaskStates()}
      targetOrigin={window.location.origin}
    />
  );
};

export default TaskInboxContainer;
