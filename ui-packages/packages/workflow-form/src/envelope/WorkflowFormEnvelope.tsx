import * as React from 'react';
import * as ReactDOM from 'react-dom';
import { EnvelopeBus } from '@kie-tools-core/envelope-bus/dist/api';
import { Envelope, EnvelopeDivConfig } from '@kie-tools-core/envelope';
import { WorkflowFormChannelApi, WorkflowFormEnvelopeApi } from '../api';
import { WorkflowFormEnvelopeContext } from './WorkflowFormEnvelopeContext';
import {
  WorkflowFormEnvelopeView,
  WorkflowFormEnvelopeViewApi
} from './WorkflowFormEnvelopeView';
import { WorkflowFormEnvelopeApiImpl } from './WorkflowFormEnvelopeApiImpl';

import './styles.css';

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
    WorkflowFormEnvelopeApi,
    WorkflowFormChannelApi,
    WorkflowFormEnvelopeViewApi,
    WorkflowFormEnvelopeContext
  >(args.bus, args.config);

  /**
   * Function that knows how to render a WorkflowForm.
   * In this case, it's a React application, but any other framework can be used.
   *
   * Returns a Promise<() => WorkflowFormEnvelopeViewApi> that can be used in WorkflowFormEnvelopeApiImpl.
   */
  const envelopeViewDelegate = async () => {
    const ref = React.createRef<WorkflowFormEnvelopeViewApi>();
    return new Promise<() => WorkflowFormEnvelopeViewApi>((res) => {
      args.container.className = 'kogito-workflow-form-container';
      ReactDOM.render(
        <WorkflowFormEnvelopeView ref={ref} channelApi={envelope.channelApi} />,
        args.container,
        () => res(() => ref.current)
      );
    });
  };

  const context: WorkflowFormEnvelopeContext = {};
  return envelope.start(envelopeViewDelegate, context, {
    create: (apiFactoryArgs) => new WorkflowFormEnvelopeApiImpl(apiFactoryArgs)
  });
}
