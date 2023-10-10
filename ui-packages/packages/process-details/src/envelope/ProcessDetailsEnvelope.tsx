import * as React from 'react';
import * as ReactDOM from 'react-dom';
import { EnvelopeBus } from '@kie-tools-core/envelope-bus/dist/api';
import { ProcessDetailsChannelApi, ProcessDetailsEnvelopeApi } from '../api';
import { Envelope, EnvelopeDivConfig } from '@kie-tools-core/envelope';
import { ProcessDetailsEnvelopeContext } from './ProcessDetailsEnvelopeContext';
import {
  ProcessDetailsEnvelopeView,
  ProcessDetailsEnvelopeViewApi
} from './ProcessDetailsEnvelopeView';
import { ProcessDetailsEnvelopeApiImpl } from './ProcessDetailsEnvelopeApiImpl';

/**
 * Function that starts an Envelope application.
 * @param args.config: This passes envelope div config
 * @param args.container: The HTML element in which the Process details View will render
 * @param args.bus: The implementation of a `bus` that knows how to send messages to the Channel.
 *
 */
export function init(args: {
  config: EnvelopeDivConfig;
  container: HTMLElement;
  bus: EnvelopeBus;
}) {
  /**
   * Creates a new generic Envelope, typed with the right interfaces.
   */
  const envelope = new Envelope<
    ProcessDetailsEnvelopeApi,
    ProcessDetailsChannelApi,
    ProcessDetailsEnvelopeViewApi,
    ProcessDetailsEnvelopeContext
  >(args.bus, args.config);

  const envelopeViewDelegate = async () => {
    const ref = React.createRef<ProcessDetailsEnvelopeViewApi>();
    return new Promise<() => ProcessDetailsEnvelopeViewApi>((res) => {
      ReactDOM.render(
        <ProcessDetailsEnvelopeView
          ref={ref}
          channelApi={envelope.channelApi}
        />,
        args.container,
        () => res(() => ref.current!)
      );
    });
  };

  const context: ProcessDetailsEnvelopeContext = {};
  return envelope.start(envelopeViewDelegate, context, {
    create: (apiFactoryArgs) =>
      new ProcessDetailsEnvelopeApiImpl(apiFactoryArgs)
  });
}
