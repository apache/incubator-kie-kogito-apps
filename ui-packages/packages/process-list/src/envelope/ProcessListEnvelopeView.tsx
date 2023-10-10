import * as React from 'react';
import { useImperativeHandle, useState } from 'react';
import { MessageBusClientApi } from '@kie-tools-core/envelope-bus/dist/api';
import { ProcessListChannelApi, ProcessListInitArgs } from '../api';
import ProcessList from './components/ProcessList/ProcessList';
import ProcessListEnvelopeViewDriver from './ProcessListEnvelopeViewDriver';
import '@patternfly/patternfly/patternfly.css';

export interface ProcessListEnvelopeViewApi {
  initialize: (initialState?: ProcessListInitArgs) => void;
}
interface Props {
  channelApi: MessageBusClientApi<ProcessListChannelApi>;
}

export const ProcessListEnvelopeView = React.forwardRef<
  ProcessListEnvelopeViewApi,
  Props
>((props, forwardedRef) => {
  const [isEnvelopeConnectedToChannel, setEnvelopeConnectedToChannel] =
    useState<boolean>(false);
  const [processInitialState, setProcessInitialState] =
    useState<ProcessListInitArgs>({} as ProcessListInitArgs);
  useImperativeHandle(
    forwardedRef,
    () => ({
      initialize: (initialState) => {
        setEnvelopeConnectedToChannel(false);
        setProcessInitialState(initialState);
        setEnvelopeConnectedToChannel(true);
      }
    }),
    []
  );

  return (
    <React.Fragment>
      <ProcessList
        isEnvelopeConnectedToChannel={isEnvelopeConnectedToChannel}
        driver={new ProcessListEnvelopeViewDriver(props.channelApi)}
        initialState={processInitialState.initialState}
        singularProcessLabel={processInitialState.singularProcessLabel}
        pluralProcessLabel={processInitialState.pluralProcessLabel}
        isTriggerCloudEventEnabled={
          processInitialState.isTriggerCloudEventEnabled
        }
        isWorkflow={processInitialState.isWorkflow}
      />
    </React.Fragment>
  );
});

export default ProcessListEnvelopeView;
