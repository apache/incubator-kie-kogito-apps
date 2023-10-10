import React, { useCallback } from 'react';
import { EnvelopeServer } from '@kie-tools-core/envelope-bus/dist/channel';
import {
  EmbeddedEnvelopeProps,
  RefForwardingEmbeddedEnvelope
} from '@kie-tools-core/envelope/dist/embedded';
import {
  FormDetailsApi,
  FormDetailsChannelApi,
  FormDetailsEnvelopeApi,
  FormDetailsDriver
} from '../api';
import { FormDetailsChannelApiImpl } from './FormDetailsChannelApiImpl';
import { FormInfo } from '@kogito-apps/forms-list';
import { ContainerType } from '@kie-tools-core/envelope/dist/api';
import { init } from '../envelope';
export interface Props {
  targetOrigin: string;
  driver: FormDetailsDriver;
  formData: FormInfo;
}

export const EmbeddedFormDetails = React.forwardRef(
  (props: Props, forwardedRef: React.Ref<FormDetailsApi>) => {
    const refDelegate = useCallback(
      (
        envelopeServer: EnvelopeServer<
          FormDetailsChannelApi,
          FormDetailsEnvelopeApi
        >
      ): FormDetailsApi => ({}),
      []
    );
    const pollInit = useCallback(
      (
        envelopeServer: EnvelopeServer<
          FormDetailsChannelApi,
          FormDetailsEnvelopeApi
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
          },
          targetOrigin: props.targetOrigin
        });
        return envelopeServer.envelopeApi.requests.formDetails__init(
          {
            origin: envelopeServer.origin,
            envelopeServerId: envelopeServer.id
          },
          {
            ...props.formData
          }
        );
      },
      []
    );

    return (
      <EmbeddedFormDetailsEnvelope
        ref={forwardedRef}
        apiImpl={new FormDetailsChannelApiImpl(props.driver)}
        origin={props.targetOrigin}
        refDelegate={refDelegate}
        pollInit={pollInit}
        config={{ containerType: ContainerType.DIV }}
      />
    );
  }
);

const EmbeddedFormDetailsEnvelope = React.forwardRef<
  FormDetailsApi,
  EmbeddedEnvelopeProps<
    FormDetailsChannelApi,
    FormDetailsEnvelopeApi,
    FormDetailsApi
  >
>(RefForwardingEmbeddedEnvelope);
