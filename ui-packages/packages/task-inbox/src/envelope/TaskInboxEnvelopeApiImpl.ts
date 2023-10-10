import { EnvelopeApiFactoryArgs } from '@kie-tools-core/envelope';
import { TaskInboxEnvelopeViewApi } from './TaskInboxEnvelopeView';
import {
  Association,
  TaskInboxChannelApi,
  TaskInboxEnvelopeApi,
  TaskInboxInitArgs
} from '../api';
import { TaskInboxEnvelopeContext } from './TaskInboxEnvelopeContext';

/**
 * Implementation of the TaskInboxEnvelopeApi
 */
export class TaskInboxEnvelopeApiImpl implements TaskInboxEnvelopeApi {
  private view: () => TaskInboxEnvelopeViewApi;
  private capturedInitRequestYet = false;
  constructor(
    private readonly args: EnvelopeApiFactoryArgs<
      TaskInboxEnvelopeApi,
      TaskInboxChannelApi,
      TaskInboxEnvelopeViewApi,
      TaskInboxEnvelopeContext
    >
  ) {}

  private hasCapturedInitRequestYet() {
    return this.capturedInitRequestYet;
  }

  private ackCapturedInitRequest() {
    this.capturedInitRequestYet = true;
  }

  taskInbox__init = async (
    association: Association,
    initArgs: TaskInboxInitArgs
  ): Promise<void> => {
    this.args.envelopeClient.associate(
      association.origin,
      association.envelopeServerId
    );

    if (this.hasCapturedInitRequestYet()) {
      return;
    }
    this.view = await this.args.viewDelegate();
    this.ackCapturedInitRequest();
    this.view().initialize(
      initArgs.initialState,
      initArgs.allTaskStates,
      initArgs.activeTaskStates
    );
  };

  taskInbox__notify = (userName): Promise<void> => {
    this.view().notify(userName);
    return Promise.resolve();
  };
}
