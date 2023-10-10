import React, { useCallback } from 'react';
import { EnvelopeServer } from '@kie-tools-core/envelope-bus/dist/channel';
import {
  EmbeddedEnvelopeProps,
  RefForwardingEmbeddedEnvelope
} from '@kie-tools-core/envelope/dist/embedded';
import {
  WorkflowDefinition,
  WorkflowFormApi,
  WorkflowFormChannelApi,
  WorkflowFormDriver,
  WorkflowFormEnvelopeApi
} from '../api';
import { EmbeddedWorkflowFormChannelApiImpl } from './EmbeddedWorkflowFormChannelApiImpl';
import { init } from '../envelope';
import { ContainerType } from '@kie-tools-core/envelope/dist/api';

export interface EmbeddedWorkflowFormProps {
  targetOrigin: string;
  driver: WorkflowFormDriver;
  workflowDefinition: WorkflowDefinition;
}

export const EmbeddedWorkflowForm = React.forwardRef(
  (
    props: EmbeddedWorkflowFormProps,
    forwardedRef: React.Ref<WorkflowFormApi>
  ) => {
    const refDelegate = useCallback(
      (
        envelopeServer: EnvelopeServer<
          WorkflowFormChannelApi,
          WorkflowFormEnvelopeApi
        >
      ): WorkflowFormApi => ({}),
      []
    );
    const pollInit = useCallback(
      (
        envelopeServer: EnvelopeServer<
          WorkflowFormChannelApi,
          WorkflowFormEnvelopeApi
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
        return envelopeServer.envelopeApi.requests.workflowForm__init(
          {
            origin: envelopeServer.origin,
            envelopeServerId: envelopeServer.id
          },
          { ...props.workflowDefinition }
        );
      },
      []
    );

    return (
      <EmbeddedWorkflowFormEnvelope
        ref={forwardedRef}
        apiImpl={new EmbeddedWorkflowFormChannelApiImpl(props.driver)}
        origin={props.targetOrigin}
        refDelegate={refDelegate}
        pollInit={pollInit}
        config={{ containerType: ContainerType.DIV }}
      />
    );
  }
);

const EmbeddedWorkflowFormEnvelope = React.forwardRef<
  WorkflowFormApi,
  EmbeddedEnvelopeProps<
    WorkflowFormChannelApi,
    WorkflowFormEnvelopeApi,
    WorkflowFormApi
  >
>(RefForwardingEmbeddedEnvelope);
