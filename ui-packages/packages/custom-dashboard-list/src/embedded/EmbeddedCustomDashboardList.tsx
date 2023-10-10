import React, { useCallback } from 'react';
import { EnvelopeServer } from '@kie-tools-core/envelope-bus/dist/channel';
import {
  EmbeddedEnvelopeProps,
  RefForwardingEmbeddedEnvelope
} from '@kie-tools-core/envelope/dist/embedded';
import {
  CustomDashboardListApi,
  CustomDashboardListChannelApi,
  CustomDashboardListEnvelopeApi,
  CustomDashboardListDriver
} from '../api';
import { CustomDashboardListChannelApiImpl } from './CustomDashboardListChannelApiImpl';
import { ContainerType } from '@kie-tools-core/envelope/dist/api';
import { init } from '../envelope';

export interface Props {
  targetOrigin: string;
  driver: CustomDashboardListDriver;
}

export const EmbeddedCustomDashboardList = React.forwardRef(
  (props: Props, forwardedRef: React.Ref<CustomDashboardListApi>) => {
    const refDelegate = useCallback(
      (
        envelopeServer: EnvelopeServer<
          CustomDashboardListChannelApi,
          CustomDashboardListEnvelopeApi
        >
      ): CustomDashboardListApi => ({}),
      []
    );
    const pollInit = useCallback(
      (
        envelopeServer: EnvelopeServer<
          CustomDashboardListChannelApi,
          CustomDashboardListEnvelopeApi
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
        return envelopeServer.envelopeApi.requests.customDashboardList__init({
          origin: envelopeServer.origin,
          envelopeServerId: envelopeServer.id
        });
      },
      []
    );

    return (
      <EmbeddedCustomDashboardListEnvelope
        ref={forwardedRef}
        apiImpl={new CustomDashboardListChannelApiImpl(props.driver)}
        origin={props.targetOrigin}
        refDelegate={refDelegate}
        pollInit={pollInit}
        config={{ containerType: ContainerType.DIV }}
      />
    );
  }
);

const EmbeddedCustomDashboardListEnvelope = React.forwardRef<
  CustomDashboardListApi,
  EmbeddedEnvelopeProps<
    CustomDashboardListChannelApi,
    CustomDashboardListEnvelopeApi,
    CustomDashboardListApi
  >
>(RefForwardingEmbeddedEnvelope);
