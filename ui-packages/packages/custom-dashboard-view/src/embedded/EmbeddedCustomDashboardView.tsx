import React, { useCallback } from 'react';
import { EnvelopeServer } from '@kie-tools-core/envelope-bus/dist/channel';
import {
  EmbeddedEnvelopeProps,
  RefForwardingEmbeddedEnvelope
} from '@kie-tools-core/envelope/dist/embedded';
import {
  CustomDashboardViewApi,
  CustomDashboardViewChannelApi,
  CustomDashboardViewEnvelopeApi,
  CustomDashboardViewDriver
} from '../api';
import { CustomDashboardViewChannelApiImpl } from './CustomDashboardViewChannelApiImpl';
import { ContainerType } from '@kie-tools-core/envelope/dist/api';
import { init } from '../envelope';

export interface Props {
  targetOrigin: string;
  driver: CustomDashboardViewDriver;
  dashboardName: string;
}

export const EmbeddedCustomDashboardView = React.forwardRef(
  (props: Props, forwardedRef: React.Ref<CustomDashboardViewApi>) => {
    const refDelegate = useCallback(
      (
        envelopeServer: EnvelopeServer<
          CustomDashboardViewChannelApi,
          CustomDashboardViewEnvelopeApi
        >
      ): CustomDashboardViewApi => ({}),
      []
    );
    const pollInit = useCallback(
      (
        envelopeServer: EnvelopeServer<
          CustomDashboardViewChannelApi,
          CustomDashboardViewEnvelopeApi
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
        return envelopeServer.envelopeApi.requests.customDashboardView__init(
          {
            origin: envelopeServer.origin,
            envelopeServerId: envelopeServer.id
          },
          props.dashboardName
        );
      },
      []
    );

    return (
      <EmbeddedCustomDashboardViewEnvelope
        ref={forwardedRef}
        apiImpl={new CustomDashboardViewChannelApiImpl(props.driver)}
        origin={props.targetOrigin}
        refDelegate={refDelegate}
        pollInit={pollInit}
        config={{ containerType: ContainerType.DIV }}
      />
    );
  }
);

const EmbeddedCustomDashboardViewEnvelope = React.forwardRef<
  CustomDashboardViewApi,
  EmbeddedEnvelopeProps<
    CustomDashboardViewChannelApi,
    CustomDashboardViewEnvelopeApi,
    CustomDashboardViewApi
  >
>(RefForwardingEmbeddedEnvelope);
