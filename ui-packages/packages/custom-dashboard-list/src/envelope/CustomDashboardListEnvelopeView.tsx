import React, { useImperativeHandle, useState } from 'react';
import { MessageBusClientApi } from '@kie-tools-core/envelope-bus/dist/api';
import { CustomDashboardListChannelApi } from '../api';
import CustomDashboardList from './components/CustomDashboardList/CustomDashboardList';
import CustomDashboardListEnvelopeViewDriver from './CustomDashboardListEnvelopeViewDriver';
import '@patternfly/patternfly/patternfly.css';

export interface CustomDashboardListEnvelopeViewApi {
  initialize: () => void;
}

interface Props {
  channelApi: MessageBusClientApi<CustomDashboardListChannelApi>;
}

export const CustomDashboardListEnvelopeView = React.forwardRef<
  CustomDashboardListEnvelopeViewApi,
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
      <CustomDashboardList
        isEnvelopeConnectedToChannel={isEnvelopeConnectedToChannel}
        driver={new CustomDashboardListEnvelopeViewDriver(props.channelApi)}
      />
    </React.Fragment>
  );
});

export default CustomDashboardListEnvelopeView;
