import React from 'react';
import {
  componentOuiaProps,
  OUIAProps
} from '@kogito-apps/ouia-tools/dist/utils/OuiaUtils';
import { GraphQL } from '@kogito-apps/consoles-common/dist/graphql';
import UserTaskInstance = GraphQL.UserTaskInstance;
import { EmbeddedTaskForm, CustomForm } from '@kogito-apps/task-form';
import { useTaskFormGatewayApi } from '../../../channel/TaskForms/TaskFormContext';
import { useDevUIAppContext } from '../../contexts/DevUIAppContext';

interface Props {
  userTask: UserTaskInstance;
  onSubmitSuccess: (message: string) => void;
  onSubmitError: (message: string, details?: string) => void;
}

const TaskFormContainer: React.FC<Props & OUIAProps> = ({
  userTask,
  onSubmitSuccess,
  onSubmitError,
  ouiaId,
  ouiaSafe
}) => {
  const gatewayApi = useTaskFormGatewayApi();
  const appContext = useDevUIAppContext();

  return (
    <EmbeddedTaskForm
      {...componentOuiaProps(ouiaId, 'task-form-container', ouiaSafe)}
      userTask={userTask}
      user={appContext.getCurrentUser()}
      driver={{
        doSubmit(phase?: string, payload?: any): Promise<any> {
          return new Promise<any>((resolve, reject) => {
            gatewayApi
              .doSubmit(userTask, phase, payload)
              .then((response) => {
                onSubmitSuccess(phase);
                resolve(response);
              })
              .catch((error) => {
                const message = error.response
                  ? error.response.data
                  : error.message;
                onSubmitError(phase, message);
                reject(error);
              });
          });
        },
        getTaskFormSchema(): Promise<Record<string, any>> {
          return gatewayApi.getTaskFormSchema(userTask);
        },
        getCustomForm(): Promise<CustomForm> {
          return gatewayApi.getCustomForm(userTask);
        }
      }}
      targetOrigin={appContext.getDevUIUrl()}
    />
  );
};

export default TaskFormContainer;
