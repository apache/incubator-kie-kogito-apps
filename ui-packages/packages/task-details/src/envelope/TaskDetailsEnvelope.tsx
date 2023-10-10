import * as React from 'react';
import * as ReactDOM from 'react-dom';
import { EnvelopeBus } from '@kie-tools-core/envelope-bus/dist/api';
import { Envelope, EnvelopeDivConfig } from '@kie-tools-core/envelope';
import { TaskDetailsChannelApi, TaskDetailsEnvelopeApi } from '../api';
import { TaskDetailsEnvelopeContext } from './TaskDetailsEnvelopeContext';
import {
  TaskDetailsEnvelopeView,
  TaskDetailsEnvelopeViewApi
} from './TaskDetailsEnvelopeView';
import { TaskDetailsEnvelopeApiImpl } from './TaskDetailsEnvelopeApiImpl';

export function init(args: {
  config: EnvelopeDivConfig;
  container: HTMLDivElement;
  bus: EnvelopeBus;
}) {
  const envelope = new Envelope<
    TaskDetailsEnvelopeApi,
    TaskDetailsChannelApi,
    TaskDetailsEnvelopeViewApi,
    TaskDetailsEnvelopeContext
  >(args.bus, args.config);

  const envelopeViewDelegate = async () => {
    const ref = React.createRef<TaskDetailsEnvelopeViewApi>();
    return new Promise<() => TaskDetailsEnvelopeViewApi>((res) => {
      ReactDOM.render(
        <TaskDetailsEnvelopeView ref={ref} channelApi={envelope.channelApi} />,
        args.container,
        () => res(() => ref.current)
      );
    });
  };

  const context: TaskDetailsEnvelopeContext = {};
  return envelope.start(envelopeViewDelegate, context, {
    create: (apiFactoryArgs) => {
      return new TaskDetailsEnvelopeApiImpl(apiFactoryArgs);
    }
  });
}
