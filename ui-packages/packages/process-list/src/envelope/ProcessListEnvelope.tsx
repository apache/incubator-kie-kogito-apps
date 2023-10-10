import * as React from 'react';
import * as ReactDOM from 'react-dom';
import { EnvelopeBus } from '@kie-tools-core/envelope-bus/dist/api';
import { ProcessListChannelApi, ProcessListEnvelopeApi } from '../api';
import { Envelope, EnvelopeDivConfig } from '@kie-tools-core/envelope';
import { ProcessListEnvelopeContext } from './ProcessListEnvelopeContext';
import {
  ProcessListEnvelopeView,
  ProcessListEnvelopeViewApi
} from './ProcessListEnvelopeView';
import { ProcessListEnvelopeApiImpl } from './ProcessListEnvelopeApiImpl';

/**
 * Function that starts an Envelope application.
 *
 * @param args.container: The HTML element in which the process list View will render
 * @param args.bus: The implementation of a `bus` that knows how to send messages to the Channel.
 * @param args.config: The config which contains the container type and the envelope id.
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
    ProcessListEnvelopeApi,
    ProcessListChannelApi,
    ProcessListEnvelopeViewApi,
    ProcessListEnvelopeContext
  >(args.bus, args.config);

  const envelopeViewDelegate = async () => {
    const ref = React.createRef<ProcessListEnvelopeViewApi>();
    return new Promise<() => ProcessListEnvelopeViewApi>((res) => {
      ReactDOM.render(
        <ProcessListEnvelopeView ref={ref} channelApi={envelope.channelApi} />,
        args.container,
        () => res(() => ref.current!)
      );
    });
  };

  const context: ProcessListEnvelopeContext = {};
  return envelope.start(envelopeViewDelegate, context, {
    create: (apiFactoryArgs) => new ProcessListEnvelopeApiImpl(apiFactoryArgs)
  });
}
