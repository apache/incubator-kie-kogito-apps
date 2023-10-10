import React, { useEffect } from 'react';
import { useHistory } from 'react-router-dom';
import { OUIAProps } from '@kogito-apps/ouia-tools/dist/utils/OuiaUtils';
import { EmbeddedProcessList } from '@kogito-apps/process-list';
import { ProcessListGatewayApi } from '../../../channel/ProcessList';
import { useProcessListGatewayApi } from '../../../channel/ProcessList/ProcessListContext';
import {
  ProcessInstance,
  ProcessListState
} from '@kogito-apps/management-console-shared/dist/types';

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

  useEffect(() => {
    const unsubscriber = gatewayApi.onOpenProcessListen({
      onOpen(process: ProcessInstance) {
        history.push({
          pathname: `/Process/${process.id}`,
          state: gatewayApi.processListState
        });
      }
    });
    return () => {
      unsubscriber.unSubscribe();
    };
  }, []);

  return (
    <EmbeddedProcessList
      driver={gatewayApi}
      targetOrigin={window.location.origin}
      initialState={initialState}
      singularProcessLabel={'Process'}
      pluralProcessLabel={'Processes'}
      isWorkflow={false}
    />
  );
};

export default ProcessListContainer;
