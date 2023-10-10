import React, { useImperativeHandle, useState } from 'react';
import { MessageBusClientApi } from '@kie-tools-core/envelope-bus/dist/api';
import { FormsListChannelApi } from '../api';
import FormsList from './components/FormsList/FormsList';
import FormsListEnvelopeViewDriver from './FormsListEnvelopeViewDriver';
import '@patternfly/patternfly/patternfly.css';

export interface FormsListEnvelopeViewApi {
  initialize: () => void;
}

interface Props {
  channelApi: MessageBusClientApi<FormsListChannelApi>;
}

export const FormsListEnvelopeView = React.forwardRef<
  FormsListEnvelopeViewApi,
  Props
>((props, forwardedRef) => {
  const [isEnvelopeConnectedToChannel, setEnvelopeConnectedToChannel] =
    useState<boolean>(false);
  useImperativeHandle(
    forwardedRef,
    () => ({
      initialize: () => {
        setEnvelopeConnectedToChannel(true);
      }
    }),
    []
  );

  return (
    <React.Fragment>
      <FormsList
        isEnvelopeConnectedToChannel={isEnvelopeConnectedToChannel}
        driver={new FormsListEnvelopeViewDriver(props.channelApi)}
      />
    </React.Fragment>
  );
});

export default FormsListEnvelopeView;
