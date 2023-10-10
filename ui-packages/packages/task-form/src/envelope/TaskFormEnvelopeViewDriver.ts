import { MessageBusClientApi } from '@kie-tools-core/envelope-bus/dist/api';
import { TaskFormChannelApi, TaskFormDriver } from '../api';
import { CustomForm } from '../types';

/**
 * Implementation of TaskFormDriver to be used on TaskFormEnvelopeView
 */
export class TaskFormEnvelopeViewDriver implements TaskFormDriver {
  constructor(
    private readonly channelApi: MessageBusClientApi<TaskFormChannelApi>
  ) {}

  getTaskFormSchema(): Promise<Record<string, any>> {
    return this.channelApi.requests.taskForm__getTaskFormSchema();
  }

  getCustomForm(): Promise<CustomForm> {
    return this.channelApi.requests.taskForm__getCustomForm();
  }

  doSubmit(phase?: string, payload?: any): Promise<any> {
    return this.channelApi.requests.taskForm__doSubmit(phase, payload);
  }
}
