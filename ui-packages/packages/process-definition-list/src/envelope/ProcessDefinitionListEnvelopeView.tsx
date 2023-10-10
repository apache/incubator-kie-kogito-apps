import React, { useImperativeHandle, useState } from 'react';
import { MessageBusClientApi } from '@kie-tools-core/envelope-bus/dist/api';
import {
  ProcessDefinitionListChannelApi,
  ProcessDefinitionListInitArgs
} from '../api';
import ProcessDefinitionList from './components/ProcessDefinitionList/ProcessDefinitionList';
import ProcessDefinitionListEnvelopeViewDriver from './ProcessDefinitionListEnvelopeViewDriver';
import '@patternfly/patternfly/patternfly.css';

export interface ProcessDefinitionListEnvelopeViewApi {
  initialize: (initArgs: ProcessDefinitionListInitArgs) => void;
}

interface Props {
  channelApi: MessageBusClientApi<ProcessDefinitionListChannelApi>;
}

export const ProcessDefinitionListEnvelopeView = React.forwardRef<
  ProcessDefinitionListEnvelopeViewApi,
  Props
>((props, forwardedRef) => {
  const [isEnvelopeConnectedToChannel, setEnvelopeConnectedToChannel] =
    useState<boolean>(false);
  const [isTriggerCloudEventEnabled, setIsTriggerCloudEventEnabled] =
    useState<boolean>(false);
  const [singularProcessLabel, setSingularProcessLabel] = useState<string>('');

  useImperativeHandle(
    forwardedRef,
    () => ({
      initialize: (initArgs) => {
        setSingularProcessLabel(initArgs.singularProcessLabel);
        setIsTriggerCloudEventEnabled(initArgs.isTriggerCloudEventEnabled);
        setEnvelopeConnectedToChannel(true);
      }
    }),
    []
  );

  return (
    <React.Fragment>
      <ProcessDefinitionList
        isEnvelopeConnectedToChannel={isEnvelopeConnectedToChannel}
        driver={new ProcessDefinitionListEnvelopeViewDriver(props.channelApi)}
        singularProcessLabel={singularProcessLabel}
        isTriggerCloudEventEnabled={isTriggerCloudEventEnabled}
      />
    </React.Fragment>
  );
});

export default ProcessDefinitionListEnvelopeView;
