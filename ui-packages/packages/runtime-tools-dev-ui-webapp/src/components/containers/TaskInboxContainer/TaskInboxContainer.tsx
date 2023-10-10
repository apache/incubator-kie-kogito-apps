import React, { useEffect } from 'react';
import { useHistory } from 'react-router-dom';
import {
  componentOuiaProps,
  OUIAProps
} from '@kogito-apps/ouia-tools/dist/utils/OuiaUtils';
import { EmbeddedTaskInbox, TaskInboxApi } from '@kogito-apps/task-inbox';
import { TaskInboxGatewayApi } from '../../../channel/TaskInbox';
import { useTaskInboxGatewayApi } from '../../../channel/TaskInbox/TaskInboxContext';
import { getActiveTaskStates, getAllTaskStates } from '../../../utils/Utils';
import { GraphQL } from '@kogito-apps/consoles-common/dist/graphql';
import UserTaskInstance = GraphQL.UserTaskInstance;
import { useDevUIAppContext } from '../../contexts/DevUIAppContext';

const TaskInboxContainer: React.FC<OUIAProps> = ({ ouiaId, ouiaSafe }) => {
  const history = useHistory();
  const gatewayApi: TaskInboxGatewayApi = useTaskInboxGatewayApi();
  const taskInboxApiRef = React.useRef<TaskInboxApi>();
  const appContext = useDevUIAppContext();

  useEffect(() => {
    const unsubscriber = gatewayApi.onOpenTaskListen({
      onOpen(task: UserTaskInstance) {
        history.push(`/TaskDetails/${task.id}`);
      }
    });

    const unsubscribeUserChange = appContext.onUserChange({
      onUserChange(user) {
        taskInboxApiRef.current.taskInbox__notify(user.id);
      }
    });
    return () => {
      unsubscriber.unSubscribe();
      unsubscribeUserChange.unSubscribe();
    };
  }, []);

  return (
    <EmbeddedTaskInbox
      {...componentOuiaProps(ouiaId, 'task-inbox-container', ouiaSafe)}
      initialState={gatewayApi.taskInboxState}
      driver={gatewayApi}
      allTaskStates={getAllTaskStates()}
      activeTaskStates={getActiveTaskStates()}
      targetOrigin={appContext.getDevUIUrl()}
      ref={taskInboxApiRef}
    />
  );
};

export default TaskInboxContainer;
