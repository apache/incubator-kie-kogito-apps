import { MessageBusClientApi } from '@kie-tools-core/envelope-bus/dist/api';
import {
  ProcessDefinition,
  ProcessFormChannelApi,
  ProcessFormDriver
} from '../api';

/**
 * Implementation of ProcessFormDriver to be used on ProcessFormEnvelopeView
 */
export class ProcessFormEnvelopeViewDriver implements ProcessFormDriver {
  constructor(
    private readonly channelApi: MessageBusClientApi<ProcessFormChannelApi>
  ) {}

  getProcessFormSchema(
    processDefinitionData: ProcessDefinition
  ): Promise<Record<string, any>> {
    return this.channelApi.requests.processForm__getProcessFormSchema(
      processDefinitionData
    );
  }

  startProcess(formData: any): Promise<void> {
    return this.channelApi.requests.processForm__startProcess(formData);
  }
}
