import { EnvelopeApiFactoryArgs } from '@kie-tools-core/envelope';
import { TaskFormEnvelopeViewApi } from './TaskFormEnvelopeView';
import {
  Association,
  TaskFormChannelApi,
  TaskFormEnvelopeApi,
  TaskFormInitArgs
} from '../api';
import { TaskFormEnvelopeContext } from './TaskFormEnvelopeContext';

/**
 * Implementation of the TaskFormEnvelopeApi
 */
export class TaskFormEnvelopeApiImpl implements TaskFormEnvelopeApi {
  private view: () => TaskFormEnvelopeViewApi;
  private capturedInitRequestYet = false;
  constructor(
    private readonly args: EnvelopeApiFactoryArgs<
      TaskFormEnvelopeApi,
      TaskFormChannelApi,
      TaskFormEnvelopeViewApi,
      TaskFormEnvelopeContext
    >
  ) {}

  private hasCapturedInitRequestYet() {
    return this.capturedInitRequestYet;
  }

  private ackCapturedInitRequest() {
    this.capturedInitRequestYet = true;
  }

  taskForm__init = async (
    association: Association,
    initArgs: TaskFormInitArgs
  ): Promise<void> => {
    this.args.envelopeClient.associate(
      association.origin,
      association.envelopeServerId
    );

    if (this.hasCapturedInitRequestYet()) {
      return;
    }

    this.ackCapturedInitRequest();
    this.view = await this.args.viewDelegate();
    this.view().initialize(initArgs);
  };
}
