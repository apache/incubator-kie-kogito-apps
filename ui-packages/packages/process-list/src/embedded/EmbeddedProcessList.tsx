import React, { useCallback } from 'react';
import { EnvelopeServer } from '@kie-tools-core/envelope-bus/dist/channel';
import {
  EmbeddedEnvelopeProps,
  RefForwardingEmbeddedEnvelope
} from '@kie-tools-core/envelope/dist/embedded';
import {
  ProcessListApi,
  ProcessListChannelApi,
  ProcessListEnvelopeApi,
  ProcessListDriver
} from '../api';
import { ProcessListChannelApiImpl } from './ProcessListChannelApiImpl';
import { ContainerType } from '@kie-tools-core/envelope/dist/api';
import { init } from '../envelope';
import { ProcessListState } from '@kogito-apps/management-console-shared/dist/types';

export interface Props {
  targetOrigin: string;
  driver: ProcessListDriver;
  initialState: ProcessListState;
  singularProcessLabel: string;
  pluralProcessLabel: string;
  isWorkflow: boolean;
  isTriggerCloudEventEnabled?: boolean;
}

export const EmbeddedProcessList = React.forwardRef(
  (props: Props, forwardedRef: React.Ref<ProcessListApi>) => {
    const refDelegate = useCallback(
      (
        envelopeServer: EnvelopeServer<
          ProcessListChannelApi,
          ProcessListEnvelopeApi
        >
      ): ProcessListApi => ({}),
      []
    );
    const pollInit = useCallback(
      (
        envelopeServer: EnvelopeServer<
          ProcessListChannelApi,
          ProcessListEnvelopeApi
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
              /* istanbul ignore next */
              window.postMessage(message, targetOrigin, transfer);
            }
          }
        });
        return envelopeServer.envelopeApi.requests.processList__init(
          {
            origin: envelopeServer.origin,
            envelopeServerId: envelopeServer.id
          },
          {
            initialState: { ...props.initialState },
            singularProcessLabel: props.singularProcessLabel,
            pluralProcessLabel: props.pluralProcessLabel,
            isWorkflow: props.isWorkflow,
            isTriggerCloudEventEnabled: props.isTriggerCloudEventEnabled
          }
        );
      },
      []
    );

    return (
      <EmbeddedProcessListEnvelope
        ref={forwardedRef}
        apiImpl={new ProcessListChannelApiImpl(props.driver)}
        origin={props.targetOrigin}
        refDelegate={refDelegate}
        pollInit={pollInit}
        config={{ containerType: ContainerType.DIV }}
      />
    );
  }
);

const EmbeddedProcessListEnvelope = React.forwardRef<
  ProcessListApi,
  EmbeddedEnvelopeProps<
    ProcessListChannelApi,
    ProcessListEnvelopeApi,
    ProcessListApi
  >
>(RefForwardingEmbeddedEnvelope);
