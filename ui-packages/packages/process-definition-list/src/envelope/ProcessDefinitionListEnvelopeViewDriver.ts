import { MessageBusClientApi } from '@kie-tools-core/envelope-bus/dist/api';
import {
  ProcessDefinition,
  ProcessDefinitionListChannelApi,
  ProcessDefinitionListDriver
} from '../api';

/**
 * Implementation of ProcessDefinitionListDriver that delegates calls to the channel Api
 */
export default class ProcessDefinitionListEnvelopeViewDriver
  implements ProcessDefinitionListDriver
{
  constructor(
    private readonly channelApi: MessageBusClientApi<ProcessDefinitionListChannelApi>
  ) {}
  setProcessDefinitionFilter(filter: string[]): Promise<void> {
    return this.channelApi.requests.processDefinitionList__setProcessDefinitionFilter(
      filter
    );
  }
  getProcessDefinitionFilter(): Promise<string[]> {
    return this.channelApi.requests.processDefinitionList__getProcessDefinitionFilter();
  }
  openProcessForm(processDefinition: ProcessDefinition): Promise<void> {
    return this.channelApi.requests.processDefinitionList__openProcessForm(
      processDefinition
    );
  }

  getProcessDefinitionsQuery(): Promise<ProcessDefinition[]> {
    return this.channelApi.requests.processDefinitionList__getProcessDefinitionsQuery();
  }

  openTriggerCloudEvent(): void {
    this.channelApi.notifications.processDefinitionsList__openTriggerCloudEvent.send();
  }
}
