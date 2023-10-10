import React, { useEffect } from 'react';
import {
  componentOuiaProps,
  OUIAProps
} from '@kogito-apps/ouia-tools/dist/utils/OuiaUtils';
import { ProcessInstance } from '@kogito-apps/management-console-shared/dist/types';
import { EmbeddedProcessDetails } from '@kogito-apps/process-details';
import { ProcessDetailsGatewayApi } from '../../../channel/ProcessDetails';
import { useProcessDetailsGatewayApi } from '../../../channel/ProcessDetails/ProcessDetailsContext';
import { useHistory } from 'react-router-dom';

interface ProcessDetailsContainerProps {
  processInstance: ProcessInstance;
}

const ProcessDetailsContainer: React.FC<
  ProcessDetailsContainerProps & OUIAProps
> = ({ processInstance, ouiaId, ouiaSafe }) => {
  const history = useHistory();
  const gatewayApi: ProcessDetailsGatewayApi = useProcessDetailsGatewayApi();
  useEffect(() => {
    const unSubscribeHandler = gatewayApi.onOpenProcessInstanceDetailsListener({
      onOpen(id: string) {
        history.push(`/`);
        history.push(`/Process/${id}`);
      }
    });

    return () => {
      unSubscribeHandler.unSubscribe();
    };
  }, [processInstance]);

  return (
    <EmbeddedProcessDetails
      {...componentOuiaProps(ouiaId, 'process-details-container', ouiaSafe)}
      driver={gatewayApi}
      targetOrigin={window.location.origin}
      processInstance={processInstance}
      showSwfDiagram={false}
      singularProcessLabel={'process'}
      pluralProcessLabel={'processes'}
    />
  );
};

export default ProcessDetailsContainer;
