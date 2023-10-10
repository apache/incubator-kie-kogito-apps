import React, { useCallback } from 'react';
import { EnvelopeServer } from '@kie-tools-core/envelope-bus/dist/channel';
import {
  EmbeddedEnvelopeProps,
  RefForwardingEmbeddedEnvelope
} from '@kie-tools-core/envelope/dist/embedded';
import {
  FormsListApi,
  FormsListChannelApi,
  FormsListEnvelopeApi,
  FormsListDriver
} from '../api';
import { FormsListChannelApiImpl } from './FormsListChannelApiImpl';
import { ContainerType } from '@kie-tools-core/envelope/dist/api';
import { init } from '../envelope';

export interface Props {
  targetOrigin: string;
  driver: FormsListDriver;
}

export const EmbeddedFormsList = React.forwardRef(
  (props: Props, forwardedRef: React.Ref<FormsListApi>) => {
    const refDelegate = useCallback(
      (
        envelopeServer: EnvelopeServer<
          FormsListChannelApi,
          FormsListEnvelopeApi
        >
      ): FormsListApi => ({}),
      []
    );
    const pollInit = useCallback(
      (
        envelopeServer: EnvelopeServer<
          FormsListChannelApi,
          FormsListEnvelopeApi
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
        return envelopeServer.envelopeApi.requests.formsList__init({
          origin: envelopeServer.origin,
          envelopeServerId: envelopeServer.id
        });
      },
      []
    );

    return (
      <EmbeddedFormsListEnvelope
        ref={forwardedRef}
        apiImpl={new FormsListChannelApiImpl(props.driver)}
        origin={props.targetOrigin}
        refDelegate={refDelegate}
        pollInit={pollInit}
        config={{ containerType: ContainerType.DIV }}
      />
    );
  }
);

const EmbeddedFormsListEnvelope = React.forwardRef<
  FormsListApi,
  EmbeddedEnvelopeProps<FormsListChannelApi, FormsListEnvelopeApi, FormsListApi>
>(RefForwardingEmbeddedEnvelope);
