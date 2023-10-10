import React, { useCallback } from 'react';
import { EnvelopeServer } from '@kie-tools-core/envelope-bus/dist/channel';
import {
  EmbeddedEnvelopeProps,
  RefForwardingEmbeddedEnvelope
} from '@kie-tools-core/envelope/dist/embedded';
import {
  ProcessDefinitionListApi,
  ProcessDefinitionListChannelApi,
  ProcessDefinitionListEnvelopeApi,
  ProcessDefinitionListDriver
} from '../api';
import { ProcessDefinitionListChannelApiImpl } from './ProcessDefinitionListChannelApiImpl';
import { ContainerType } from '@kie-tools-core/envelope/dist/api';
import { init } from '../envelope';

export interface Props {
  targetOrigin: string;
  driver: ProcessDefinitionListDriver;
  singularProcessLabel: string;
  isTriggerCloudEventEnabled?: boolean;
}

export const EmbeddedProcessDefinitionList = React.forwardRef(
  (props: Props, forwardedRef: React.Ref<ProcessDefinitionListApi>) => {
    const refDelegate = useCallback(
      (
        envelopeServer: EnvelopeServer<
          ProcessDefinitionListChannelApi,
          ProcessDefinitionListEnvelopeApi
        >
      ): ProcessDefinitionListApi => ({}),
      []
    );
    const pollInit = useCallback(
      (
        envelopeServer: EnvelopeServer<
          ProcessDefinitionListChannelApi,
          ProcessDefinitionListEnvelopeApi
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
        return envelopeServer.envelopeApi.requests.processDefinitionList__init(
          {
            origin: envelopeServer.origin,
            envelopeServerId: envelopeServer.id
          },
          {
            singularProcessLabel: props.singularProcessLabel,
            isTriggerCloudEventEnabled: props.isTriggerCloudEventEnabled
          }
        );
      },
      []
    );

    return (
      <EmbeddedProcessDefinitionListEnvelope
        ref={forwardedRef}
        apiImpl={new ProcessDefinitionListChannelApiImpl(props.driver)}
        origin={props.targetOrigin}
        refDelegate={refDelegate}
        pollInit={pollInit}
        config={{ containerType: ContainerType.DIV }}
      />
    );
  }
);

const EmbeddedProcessDefinitionListEnvelope = React.forwardRef<
  ProcessDefinitionListApi,
  EmbeddedEnvelopeProps<
    ProcessDefinitionListChannelApi,
    ProcessDefinitionListEnvelopeApi,
    ProcessDefinitionListApi
  >
>(RefForwardingEmbeddedEnvelope);
