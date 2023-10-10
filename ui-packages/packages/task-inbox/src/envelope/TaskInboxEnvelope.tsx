import * as React from 'react';
import * as ReactDOM from 'react-dom';
import { EnvelopeBus } from '@kie-tools-core/envelope-bus/dist/api';
import { TaskInboxChannelApi, TaskInboxEnvelopeApi } from '../api';
import { TaskInboxEnvelopeContext } from './TaskInboxEnvelopeContext';
import {
  TaskInboxEnvelopeView,
  TaskInboxEnvelopeViewApi
} from './TaskInboxEnvelopeView';
import { TaskInboxEnvelopeApiImpl } from './TaskInboxEnvelopeApiImpl';
import { Envelope, EnvelopeDivConfig } from '@kie-tools-core/envelope';

/**
 * Function that starts an Envelope application.
 *
 * @param args.container: The HTML element in which the TaskInbox will render
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
    TaskInboxEnvelopeApi,
    TaskInboxChannelApi,
    TaskInboxEnvelopeViewApi,
    TaskInboxEnvelopeContext
  >(args.bus, args.config);

  /**
   * Function that knows how to render a TaskInbox.
   * In this case, it's a React application, but any other framework can be used.
   *
   * Returns a Promise<() => TaskInboxEnvelopeViewApi> that can be used in TaskInboxEnvelopeApiImpl.
   */
  const envelopeViewDelegate = async () => {
    const ref = React.createRef<TaskInboxEnvelopeViewApi>();
    return new Promise<() => TaskInboxEnvelopeViewApi>((res) => {
      ReactDOM.render(
        <TaskInboxEnvelopeView ref={ref} channelApi={envelope.channelApi} />,
        args.container,
        () => res(() => ref.current)
      );
    });
  };

  const context: TaskInboxEnvelopeContext = {};
  return envelope.start(envelopeViewDelegate, context, {
    create: (apiFactoryArgs) => new TaskInboxEnvelopeApiImpl(apiFactoryArgs)
  });
}
