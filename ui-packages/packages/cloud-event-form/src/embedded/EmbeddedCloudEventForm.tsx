import React, { useCallback } from 'react';
import { EnvelopeServer } from '@kie-tools-core/envelope-bus/dist/channel';
import {
  EmbeddedEnvelopeProps,
  RefForwardingEmbeddedEnvelope
} from '@kie-tools-core/envelope/dist/embedded';
import {
  CloudEventFormApi,
  CloudEventFormChannelApi,
  CloudEventFormEnvelopeApi,
  CloudEventFormDriver
} from '../api';
import { init } from '../envelope';
import { ContainerType } from '@kie-tools-core/envelope/dist/api';
import { EmbeddedCloudEventFormChannelApiImpl } from './EmbeddedCloudEventFormChannelApiImpl';

export interface EmbeddedCloudEventFormProps {
  targetOrigin: string;
  driver: CloudEventFormDriver;
  isNewInstanceEvent?: boolean;
  defaultValues?: {
    cloudEventSource?: string;
    instanceId?: string;
  };
}

export const EmbeddedCloudEventForm = React.forwardRef(
  (
    props: EmbeddedCloudEventFormProps,
    forwardedRef: React.Ref<CloudEventFormApi>
  ) => {
    const refDelegate = useCallback(
      (
        envelopeServer: EnvelopeServer<
          CloudEventFormChannelApi,
          CloudEventFormEnvelopeApi
        >
      ): CloudEventFormApi => ({}),
      []
    );
    const pollInit = useCallback(
      (
        envelopeServer: EnvelopeServer<
          CloudEventFormChannelApi,
          CloudEventFormEnvelopeApi
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
        return envelopeServer.envelopeApi.requests.cloudEventForm__init(
          {
            origin: envelopeServer.origin,
            envelopeServerId: envelopeServer.id
          },
          {
            isNewInstanceEvent: props.isNewInstanceEvent ?? true,
            defaultValues: props.defaultValues
          }
        );
      },
      []
    );
    return (
      <EmbeddedCloudEventFormEnvelope
        ref={forwardedRef}
        apiImpl={new EmbeddedCloudEventFormChannelApiImpl(props.driver)}
        origin={props.targetOrigin}
        refDelegate={refDelegate}
        pollInit={pollInit}
        config={{ containerType: ContainerType.DIV }}
      />
    );
  }
);

const EmbeddedCloudEventFormEnvelope = React.forwardRef<
  CloudEventFormApi,
  EmbeddedEnvelopeProps<
    CloudEventFormChannelApi,
    CloudEventFormEnvelopeApi,
    CloudEventFormApi
  >
>(RefForwardingEmbeddedEnvelope);
