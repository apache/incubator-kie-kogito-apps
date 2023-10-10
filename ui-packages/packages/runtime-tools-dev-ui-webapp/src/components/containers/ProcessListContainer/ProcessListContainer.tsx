import React, { useEffect } from 'react';
import { useHistory } from 'react-router-dom';
import {
  componentOuiaProps,
  OUIAProps
} from '@kogito-apps/ouia-tools/dist/utils/OuiaUtils';
import { EmbeddedProcessList } from '@kogito-apps/process-list';
import {
  ProcessListGatewayApi,
  useProcessListGatewayApi
} from '../../../channel/ProcessList';
import {
  ProcessInstance,
  ProcessListState
} from '@kogito-apps/management-console-shared/dist/types';
import { useDevUIAppContext } from '../../contexts/DevUIAppContext';
import { CloudEventPageSource } from '../../pages/CloudEventFormPage/CloudEventFormPage';

interface ProcessListContainerProps {
  initialState: ProcessListState;
}

const ProcessListContainer: React.FC<ProcessListContainerProps & OUIAProps> = ({
  initialState,
  ouiaId,
  ouiaSafe
}) => {
  const history = useHistory();
  const gatewayApi: ProcessListGatewayApi = useProcessListGatewayApi();
  const appContext = useDevUIAppContext();

  useEffect(() => {
    const onOpenInstanceUnsubscriber = gatewayApi.onOpenProcessListen({
      onOpen(process: ProcessInstance) {
        history.push({
          pathname: `/Process/${process.id}`,
          state: gatewayApi.processListState
        });
      }
    });
    const onTriggerCloudEventUnsubscriber = appContext.isWorkflow()
      ? gatewayApi.onOpenTriggerCloudEventListen({
          onOpen(processInstance?: ProcessInstance) {
            history.push({
              pathname: `/Processes/CloudEvent/${processInstance?.id ?? ''}`,
              state: {
                source: CloudEventPageSource.INSTANCES
              }
            });
          }
        })
      : undefined;
    return () => {
      onOpenInstanceUnsubscriber.unSubscribe();
      onTriggerCloudEventUnsubscriber?.unSubscribe();
    };
  }, []);

  return (
    <EmbeddedProcessList
      {...componentOuiaProps(ouiaId, 'process-list-container', ouiaSafe)}
      driver={gatewayApi}
      targetOrigin={appContext.getDevUIUrl()}
      initialState={initialState}
      singularProcessLabel={appContext.customLabels.singularProcessLabel}
      pluralProcessLabel={appContext.customLabels.pluralProcessLabel}
      isTriggerCloudEventEnabled={appContext.isWorkflow()}
      isWorkflow={appContext.isWorkflow()}
    />
  );
};

export default ProcessListContainer;
