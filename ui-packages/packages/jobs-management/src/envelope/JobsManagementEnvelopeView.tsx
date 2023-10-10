import * as React from 'react';
import { useImperativeHandle, useState } from 'react';
import { MessageBusClientApi } from '@kie-tools-core/envelope-bus/dist/api';
import { JobsManagementChannelApi } from '../api';
import JobsManagement from './components/JobsManagement/JobsManagement';
import JobsManagementEnvelopeViewDriver from './JobsManagementEnvelopeViewDriver';
import '@patternfly/patternfly/patternfly.css';

export interface JobsManagementEnvelopeViewApi {
  initialize: () => void;
}
interface Props {
  channelApi: MessageBusClientApi<JobsManagementChannelApi>;
}

export const JobsManagementEnvelopeView = React.forwardRef<
  JobsManagementEnvelopeViewApi,
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
      <JobsManagement
        isEnvelopeConnectedToChannel={isEnvelopeConnectedToChannel}
        driver={new JobsManagementEnvelopeViewDriver(props.channelApi)}
      />
    </React.Fragment>
  );
});

export default JobsManagementEnvelopeView;
