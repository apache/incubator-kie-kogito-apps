import React from 'react';
import {
  componentOuiaProps,
  OUIAProps
} from '@kogito-apps/ouia-tools/dist/utils/OuiaUtils';
import { WorkflowFormGatewayApi } from '../../../channel/WorkflowForm/WorkflowFormGatewayApi';
import { useWorkflowFormGatewayApi } from '../../../channel/WorkflowForm/WorkflowFormContext';
import {
  EmbeddedWorkflowForm,
  WorkflowDefinition
} from '@kogito-apps/workflow-form';
import { useDevUIAppContext } from '../../contexts/DevUIAppContext';

interface WorkflowFormContainerProps {
  workflowDefinitionData: WorkflowDefinition;
  onSubmitSuccess: (id: string) => void;
  onSubmitError: (details?: string) => void;
  onResetForm: () => void;
}
const WorkflowFormContainer: React.FC<
  WorkflowFormContainerProps & OUIAProps
> = ({
  workflowDefinitionData,
  onSubmitSuccess,
  onSubmitError,
  onResetForm,
  ouiaId,
  ouiaSafe
}) => {
  const gatewayApi: WorkflowFormGatewayApi = useWorkflowFormGatewayApi();
  const appContext = useDevUIAppContext();

  return (
    <EmbeddedWorkflowForm
      {...componentOuiaProps(ouiaId, 'workflow-form-container', ouiaSafe)}
      driver={{
        async getCustomWorkflowSchema(): Promise<Record<string, any>> {
          return gatewayApi.getCustomWorkflowSchema(
            workflowDefinitionData.workflowName
          );
        },
        async resetBusinessKey() {
          onResetForm();
        },
        async startWorkflow(
          endpoint: string,
          data: Record<string, any>
        ): Promise<void> {
          return gatewayApi
            .startWorkflow(endpoint, data)
            .then((id: string) => {
              onSubmitSuccess(
                `A workflow with id ${id} was triggered successfully.`
              );
            })
            .catch((error) => {
              const message =
                error?.response?.data?.message +
                  ' ' +
                  error?.response?.data?.cause ||
                error?.message ||
                'Unknown error. More details in the developer tools console.';
              onSubmitError(message);
            });
        }
      }}
      targetOrigin={appContext.getDevUIUrl()}
      workflowDefinition={{
        workflowName: workflowDefinitionData.workflowName,
        endpoint: workflowDefinitionData.endpoint
      }}
    />
  );
};

export default WorkflowFormContainer;
