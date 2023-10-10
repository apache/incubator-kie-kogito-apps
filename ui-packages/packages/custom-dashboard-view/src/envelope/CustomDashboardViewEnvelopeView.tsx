import React, { useImperativeHandle, useState } from 'react';
import { MessageBusClientApi } from '@kie-tools-core/envelope-bus/dist/api';
import { CustomDashboardViewChannelApi } from '../api';
import CustomDashboardView from './components/CustomDashboardView/CustomDashboardView';
import CustomDashboardViewEnvelopeViewDriver from './CustomDashboardViewEnvelopeViewDriver';
import '@patternfly/patternfly/patternfly.css';

export interface CustomDashboardViewEnvelopeViewApi {
  initialize: (dashboardName: string, targetOrigin: string) => void;
}

interface Props {
  channelApi: MessageBusClientApi<CustomDashboardViewChannelApi>;
}

export const CustomDashboardViewEnvelopeView = React.forwardRef<
  CustomDashboardViewEnvelopeViewApi,
  Props
>((props, forwardedRef) => {
  const [isEnvelopeConnectedToChannel, setEnvelopeConnectedToChannel] =
    useState<boolean>(false);

  const [customDashboardName, setCustomDashboardName] = useState<string>();
  const [targetOrigin, setTargetOrigin] = useState<string>();

  useImperativeHandle(
    forwardedRef,
    () => ({
      initialize: (dashboardName: string, targetOrigin: string) => {
        setEnvelopeConnectedToChannel(true);
        setCustomDashboardName(dashboardName);
        setTargetOrigin(targetOrigin);
      }
    }),
    []
  );
  return (
    <>
      {customDashboardName && customDashboardName != 'undefined' && (
        <CustomDashboardView
          isEnvelopeConnectedToChannel={isEnvelopeConnectedToChannel}
          driver={new CustomDashboardViewEnvelopeViewDriver(props.channelApi)}
          customDashboardName={customDashboardName}
          targetOrigin={targetOrigin}
        />
      )}
    </>
  );
});

export default CustomDashboardViewEnvelopeView;
