import React, { useCallback } from 'react';
import { EnvelopeServer } from '@kie-tools-core/envelope-bus/dist/channel';
import {
  EmbeddedEnvelopeProps,
  RefForwardingEmbeddedEnvelope
} from '@kie-tools-core/envelope/dist/embedded';
import {
  ProcessDefinition,
  ProcessFormApi,
  ProcessFormChannelApi,
  ProcessFormDriver,
  ProcessFormEnvelopeApi
} from '../api';
import { EmbeddedProcessFormChannelApiImpl } from './EmbeddedProcessFormChannelApiImpl';
import { init } from '../envelope';
import { ContainerType } from '@kie-tools-core/envelope/dist/api';

export interface EmbeddedProcessFormProps {
  targetOrigin: string;
  driver: ProcessFormDriver;
  processDefinition: ProcessDefinition;
}

export const EmbeddedProcessForm = React.forwardRef(
  (
    props: EmbeddedProcessFormProps,
    forwardedRef: React.Ref<ProcessFormApi>
  ) => {
    const refDelegate = useCallback(
      (
        envelopeServer: EnvelopeServer<
          ProcessFormChannelApi,
          ProcessFormEnvelopeApi
        >
      ): ProcessFormApi => ({}),
      []
    );
    const pollInit = useCallback(
      (
        envelopeServer: EnvelopeServer<
          ProcessFormChannelApi,
          ProcessFormEnvelopeApi
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
        return envelopeServer.envelopeApi.requests.processForm__init(
          {
            origin: envelopeServer.origin,
            envelopeServerId: envelopeServer.id
          },
          { ...props.processDefinition }
        );
      },
      []
    );

    return (
      <EmbeddedProcessFormEnvelope
        ref={forwardedRef}
        apiImpl={new EmbeddedProcessFormChannelApiImpl(props.driver)}
        origin={props.targetOrigin}
        refDelegate={refDelegate}
        pollInit={pollInit}
        config={{ containerType: ContainerType.DIV }}
      />
    );
  }
);

const EmbeddedProcessFormEnvelope = React.forwardRef<
  ProcessFormApi,
  EmbeddedEnvelopeProps<
    ProcessFormChannelApi,
    ProcessFormEnvelopeApi,
    ProcessFormApi
  >
>(RefForwardingEmbeddedEnvelope);
