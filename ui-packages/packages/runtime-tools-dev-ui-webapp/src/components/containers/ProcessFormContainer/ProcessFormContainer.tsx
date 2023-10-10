import React from 'react';
import {
  componentOuiaProps,
  OUIAProps
} from '@kogito-apps/ouia-tools/dist/utils/OuiaUtils';
import { ProcessFormGatewayApi } from '../../../channel/ProcessForm/ProcessFormGatewayApi';
import { useProcessFormGatewayApi } from '../../../channel/ProcessForm/ProcessFormContext';
import { EmbeddedProcessForm } from '@kogito-apps/process-form';
import { ProcessDefinition } from '@kogito-apps/process-definition-list';
import { useDevUIAppContext } from '../../contexts/DevUIAppContext';

interface ProcessFormContainerProps {
  processDefinitionData: ProcessDefinition;
  onSubmitSuccess: (id: string) => void;
  onSubmitError: (details?: string) => void;
}
const ProcessFormContainer: React.FC<ProcessFormContainerProps & OUIAProps> = ({
  processDefinitionData,
  onSubmitSuccess,
  onSubmitError,
  ouiaId,
  ouiaSafe
}) => {
  const gatewayApi: ProcessFormGatewayApi = useProcessFormGatewayApi();
  const appContext = useDevUIAppContext();
  return (
    <EmbeddedProcessForm
      {...componentOuiaProps(ouiaId, 'process-form-container', ouiaSafe)}
      driver={{
        getProcessFormSchema(
          processDefinitionData: ProcessDefinition
        ): Promise<any> {
          return gatewayApi.getProcessFormSchema(processDefinitionData);
        },
        async startProcess(formData: any): Promise<void> {
          return gatewayApi
            .startProcess(formData, processDefinitionData)
            .then((id: string) => {
              gatewayApi.setBusinessKey('');
              onSubmitSuccess(id);
            })
            .catch((error) => {
              const message = error.response
                ? error.response.data
                : error.message;
              onSubmitError(message);
            });
        }
      }}
      targetOrigin={appContext.getDevUIUrl()}
      processDefinition={processDefinitionData}
    />
  );
};

export default ProcessFormContainer;
