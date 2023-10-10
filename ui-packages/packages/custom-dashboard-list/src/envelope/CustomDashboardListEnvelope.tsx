import * as React from 'react';
import * as ReactDOM from 'react-dom';
import { EnvelopeBus } from '@kie-tools-core/envelope-bus/dist/api';
import {
  CustomDashboardListChannelApi,
  CustomDashboardListEnvelopeApi
} from '../api';
import { CustomDashboardListEnvelopeContext } from './CustomDashboardListEnvelopeContext';
import {
  CustomDashboardListEnvelopeView,
  CustomDashboardListEnvelopeViewApi
} from './CustomDashboardListEnvelopeView';
import { CustomDashboardListEnvelopeApiImpl } from './CustomDashboardListEnvelopeApiImpl';
import { Envelope, EnvelopeDivConfig } from '@kie-tools-core/envelope';

/**
 * Function that starts an Envelope application.
 *
 * @param args.container: The HTML element in which the CustomDashboardList will render
 * @param args.bus: The implementation of a `bus` that knows how to send messages to the Channel.
 *
 */
export function init(args: {
  config: EnvelopeDivConfig;
  container: HTMLDivElement;
  bus: EnvelopeBus;
}) {
  /**
   * Creates a new generic Envelope, typed with the right interfaces.
   */
  const envelope = new Envelope<
    CustomDashboardListEnvelopeApi,
    CustomDashboardListChannelApi,
    CustomDashboardListEnvelopeViewApi,
    CustomDashboardListEnvelopeContext
  >(args.bus, args.config);

  /**
   * Function that knows how to render a CustomDashboardList.
   * In this case, it's a React application, but any other framework can be used.
   *
   * Returns a Promise<() => CustomDashboardListEnvelopeViewApi> that can be used in CustomDashboardListEnvelopeApiImpl.
   */
  const envelopeViewDelegate = async () => {
    const ref = React.createRef<CustomDashboardListEnvelopeViewApi>();
    return new Promise<() => CustomDashboardListEnvelopeViewApi>((res) => {
      ReactDOM.render(
        <CustomDashboardListEnvelopeView
          ref={ref}
          channelApi={envelope.channelApi}
        />,
        args.container,
        () => res(() => ref.current)
      );
    });
  };

  const context: CustomDashboardListEnvelopeContext = {};
  return envelope.start(envelopeViewDelegate, context, {
    create: (apiFactoryArgs) =>
      new CustomDashboardListEnvelopeApiImpl(apiFactoryArgs)
  });
}
