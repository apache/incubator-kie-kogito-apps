import React, { useCallback } from 'react';
import { EnvelopeServer } from '@kie-tools-core/envelope-bus/dist/channel';
import {
  EmbeddedEnvelopeProps,
  RefForwardingEmbeddedEnvelope
} from '@kie-tools-core/envelope/dist/embedded';
import { ContainerType } from '@kie-tools-core/envelope/dist/api';
import { init } from '../envelope';
import {
  JobsManagementApi,
  JobsManagementChannelApi,
  JobsManagementEnvelopeApi,
  JobsManagementDriver
} from '../api';
import { JobsManagementChannelApiImpl } from './JobsManagementChannelApiImpl';

export interface Props {
  targetOrigin: string;
  driver: JobsManagementDriver;
}

export const EmbeddedJobsManagement = React.forwardRef(
  (props: Props, forwardedRef: React.Ref<JobsManagementApi>) => {
    const refDelegate = useCallback(
      (
        envelopeServer: EnvelopeServer<
          JobsManagementChannelApi,
          JobsManagementEnvelopeApi
        >
      ): JobsManagementApi => ({}),
      []
    );
    const pollInit = useCallback(
      (
        envelopeServer: EnvelopeServer<
          JobsManagementChannelApi,
          JobsManagementEnvelopeApi
        >,
        container: () => HTMLDivElement
      ) => {
        init({
          config: {
            containerType: ContainerType.DIV,
            envelopeId: envelopeServer.id
          },
          container: container(),
          bus: {
            postMessage(message, targetOrigin, transfer) {
              window.postMessage(message, targetOrigin, transfer);
            }
          }
        });

        return envelopeServer.envelopeApi.requests.jobsManagement__init({
          origin: envelopeServer.origin,
          envelopeServerId: envelopeServer.id
        });
      },
      []
    );

    return (
      <EmbeddedJobsManagementEnvelope
        ref={forwardedRef}
        apiImpl={new JobsManagementChannelApiImpl(props.driver)}
        origin={props.targetOrigin}
        refDelegate={refDelegate}
        pollInit={pollInit}
        config={{ containerType: ContainerType.DIV }}
      />
    );
  }
);

const EmbeddedJobsManagementEnvelope = React.forwardRef<
  JobsManagementApi,
  EmbeddedEnvelopeProps<
    JobsManagementChannelApi,
    JobsManagementEnvelopeApi,
    JobsManagementApi
  >
>(RefForwardingEmbeddedEnvelope);
