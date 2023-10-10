import * as React from 'react';
import * as ReactDOM from 'react-dom';
import { EnvelopeBus } from '@kie-tools-core/envelope-bus/dist/api';
import { Envelope, EnvelopeDivConfig } from '@kie-tools-core/envelope';
import { CloudEventFormChannelApi, CloudEventFormEnvelopeApi } from '../api';

import './styles.css';
import {
  CloudEventFormEnvelopeView,
  CloudEventFormEnvelopeViewApi
} from './CloudEventFormEnvelopeView';
import { CloudEventFormEnvelopeApiImpl } from './CloudEventFormEnvelopeApiImpl';

/**
 * Function that starts an Envelope application.
 *
 * @param args.container: The HTML element in which the WorkflowForm will render
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
    CloudEventFormEnvelopeApi,
    CloudEventFormChannelApi,
    CloudEventFormEnvelopeViewApi,
    undefined
  >(args.bus, args.config);

  /**
   * Function that knows how to render a CloudEventForm.
   * In this case, it's a React application, but any other framework can be used.
   *
   * Returns a Promise<() => CloudEventFormEnvelopeViewApi> that can be used in CloudEventFormEnvelopeApiImpl.
   */
  const envelopeViewDelegate = async () => {
    const ref = React.createRef<CloudEventFormEnvelopeViewApi>();
    return new Promise<() => CloudEventFormEnvelopeViewApi>((res) => {
      args.container.className = 'kogito-cloud-event-form-container';
      ReactDOM.render(
        <CloudEventFormEnvelopeView
          ref={ref}
          channelApi={envelope.channelApi}
        />,
        args.container,
        () => res(() => ref.current)
      );
    });
  };

  return envelope.start(envelopeViewDelegate, undefined, {
    create: (apiFactoryArgs) =>
      new CloudEventFormEnvelopeApiImpl(apiFactoryArgs)
  });
}
