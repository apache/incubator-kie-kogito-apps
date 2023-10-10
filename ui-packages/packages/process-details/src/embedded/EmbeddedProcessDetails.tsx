import React, { useCallback } from 'react';
import { EnvelopeServer } from '@kie-tools-core/envelope-bus/dist/channel';
import {
  EmbeddedEnvelopeProps,
  RefForwardingEmbeddedEnvelope
} from '@kie-tools-core/envelope/dist/embedded';
import { ContainerType } from '@kie-tools-core/envelope/dist/api';
import { init } from '../envelope';
import {
  ProcessDetailsApi,
  ProcessDetailsChannelApi,
  ProcessDetailsEnvelopeApi,
  ProcessDetailsDriver,
  DiagramPreviewSize
} from '../api';
import { ProcessInstance } from '@kogito-apps/management-console-shared/dist/types';
import { ProcessDetailsChannelApiImpl } from './ProcessDetailsChannelApiImpl';

export interface Props {
  targetOrigin: string;
  driver: ProcessDetailsDriver;
  processInstance: ProcessInstance;
  omittedProcessTimelineEvents?: string[];
  diagramPreviewSize?: DiagramPreviewSize;
  showSwfDiagram: boolean;
  isStunnerEnabled?: boolean;
  singularProcessLabel: string;
  pluralProcessLabel: string;
}

export const EmbeddedProcessDetails = React.forwardRef(
  (props: Props, forwardedRef: React.Ref<ProcessDetailsApi>) => {
    const refDelegate = useCallback(
      (
        envelopeServer: EnvelopeServer<
          ProcessDetailsChannelApi,
          ProcessDetailsEnvelopeApi
        >
      ): ProcessDetailsApi => ({}),
      []
    );
    const pollInit = useCallback(
      (
        envelopeServer: EnvelopeServer<
          ProcessDetailsChannelApi,
          ProcessDetailsEnvelopeApi
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

        return envelopeServer.envelopeApi.requests.processDetails__init(
          {
            origin: envelopeServer.origin,
            envelopeServerId: envelopeServer.id
          },
          {
            processInstance: props.processInstance,
            omittedProcessTimelineEvents: props.omittedProcessTimelineEvents,
            diagramPreviewSize: props.diagramPreviewSize,
            showSwfDiagram: props.showSwfDiagram,
            isStunnerEnabled: props.isStunnerEnabled,
            singularProcessLabel: props.singularProcessLabel,
            pluralProcessLabel: props.pluralProcessLabel
          }
        );
      },
      []
    );

    return (
      <EmbeddedProcessDetailsEnvelope
        ref={forwardedRef}
        apiImpl={new ProcessDetailsChannelApiImpl(props.driver)}
        origin={props.targetOrigin}
        refDelegate={refDelegate}
        pollInit={pollInit}
        config={{ containerType: ContainerType.DIV }}
      />
    );
  }
);

const EmbeddedProcessDetailsEnvelope = React.forwardRef<
  ProcessDetailsApi,
  EmbeddedEnvelopeProps<
    ProcessDetailsChannelApi,
    ProcessDetailsEnvelopeApi,
    ProcessDetailsApi
  >
>(RefForwardingEmbeddedEnvelope);
