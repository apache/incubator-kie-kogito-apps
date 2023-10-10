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
import { useDevUIAppContext } from '../../contexts/DevUIAppContext';

interface ProcessDetailsContainerProps {
  processInstance: ProcessInstance;
}

const ProcessDetailsContainer: React.FC<
  ProcessDetailsContainerProps & OUIAProps
> = ({ processInstance, ouiaId, ouiaSafe }) => {
  const history = useHistory();
  const appContext = useDevUIAppContext();
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
      targetOrigin={appContext.getDevUIUrl()}
      processInstance={processInstance}
      omittedProcessTimelineEvents={appContext.omittedProcessTimelineEvents}
      diagramPreviewSize={appContext.diagramPreviewSize}
      showSwfDiagram={appContext.isWorkflow()}
      isStunnerEnabled={appContext.getIsStunnerEnabled()}
      singularProcessLabel={appContext.customLabels.singularProcessLabel}
      pluralProcessLabel={appContext.customLabels.pluralProcessLabel}
    />
  );
};

export default ProcessDetailsContainer;
