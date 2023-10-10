import React, { useImperativeHandle, useState } from 'react';
import { MessageBusClientApi } from '@kie-tools-core/envelope-bus/dist/api';
import { ProcessDefinition, ProcessFormChannelApi } from '../api';
import '@patternfly/patternfly/patternfly.css';
import ProcessForm from './components/ProcessForm/ProcessForm';
import { ProcessFormEnvelopeViewDriver } from './ProcessFormEnvelopeViewDriver';

export interface ProcessFormEnvelopeViewApi {
  initialize: (processDefinitionData: ProcessDefinition) => void;
}

interface Props {
  channelApi: MessageBusClientApi<ProcessFormChannelApi>;
}

export const ProcessFormEnvelopeView = React.forwardRef<
  ProcessFormEnvelopeViewApi,
  Props
>((props, forwardedRef) => {
  const [isEnvelopeConnectedToChannel, setEnvelopeConnectedToChannel] =
    useState<boolean>(false);
  const [processDefinition, setProcessDefinition] =
    useState<ProcessDefinition>();
  useImperativeHandle(
    forwardedRef,
    () => ({
      initialize: (processDefinitionData: ProcessDefinition) => {
        setProcessDefinition(processDefinitionData);
        setEnvelopeConnectedToChannel(true);
      }
    }),
    []
  );
  return (
    <ProcessForm
      isEnvelopeConnectedToChannel={isEnvelopeConnectedToChannel}
      processDefinition={processDefinition}
      driver={new ProcessFormEnvelopeViewDriver(props.channelApi)}
    />
  );
});

export default ProcessFormEnvelopeView;
