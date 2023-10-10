import { EnvelopeApiFactoryArgs } from '@kie-tools-core/envelope';
import {
  Association,
  TaskDetailsChannelApi,
  TaskDetailsEnvelopeApi,
  TaskDetailsInitArgs
} from '../api';
import { TaskDetailsEnvelopeViewApi } from './TaskDetailsEnvelopeView';
import { TaskDetailsEnvelopeContext } from './TaskDetailsEnvelopeContext';

export class TaskDetailsEnvelopeApiImpl implements TaskDetailsEnvelopeApi {
  private view: () => TaskDetailsEnvelopeViewApi;
  constructor(
    private readonly args: EnvelopeApiFactoryArgs<
      TaskDetailsEnvelopeApi,
      TaskDetailsChannelApi,
      TaskDetailsEnvelopeViewApi,
      TaskDetailsEnvelopeContext
    >
  ) {}

  public async taskDetails__init(
    association: Association,
    initArgs: TaskDetailsInitArgs
  ) {
    this.args.envelopeClient.associate(
      association.origin,
      association.envelopeServerId
    );
    this.view = await this.args.viewDelegate();
    this.view().setTask(initArgs.task);
  }
}
