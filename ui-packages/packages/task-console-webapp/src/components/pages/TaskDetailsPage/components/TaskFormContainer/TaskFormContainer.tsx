import React from 'react';
import {
  componentOuiaProps,
  OUIAProps
} from '@kogito-apps/ouia-tools/dist/utils/OuiaUtils';
import { useKogitoAppContext } from '@kogito-apps/consoles-common/dist/environment/context';
import { GraphQL } from '@kogito-apps/consoles-common/dist/graphql';
import UserTaskInstance = GraphQL.UserTaskInstance;
import { CustomForm, EmbeddedTaskForm } from '@kogito-apps/task-form';
import { useTaskFormGatewayApi } from '../../../../../channel/forms/TaskFormContext';

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
  const kogitoAppContext = useKogitoAppContext();

  return (
    <EmbeddedTaskForm
      {...componentOuiaProps(ouiaId, 'task-form-container', ouiaSafe)}
      userTask={userTask}
      user={kogitoAppContext.getCurrentUser()}
      driver={{
        doSubmit(phase?: string, payload?: any): Promise<any> {
          return gatewayApi
            .doSubmit(userTask, phase, payload)
            .then((result) => onSubmitSuccess(phase))
            .catch((error) => {
              const message = error.response
                ? error.response.data
                : error.message;
              onSubmitError(phase, message);
            });
        },
        getTaskFormSchema(): Promise<Record<string, any>> {
          return gatewayApi.getTaskFormSchema(userTask);
        },
        getCustomForm(): Promise<CustomForm> {
          return gatewayApi.getCustomForm(userTask);
        }
      }}
      targetOrigin={window.location.origin}
    />
  );
};

export default TaskFormContainer;
