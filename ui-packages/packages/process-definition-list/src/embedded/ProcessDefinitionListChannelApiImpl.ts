import {
  ProcessDefinition,
  ProcessDefinitionListChannelApi,
  ProcessDefinitionListDriver
} from '../api';

/**
 * Implementation of the ProcessDefinitionListChannelApiImpl delegating to a ProcessDefinitionListDriver
 */
export class ProcessDefinitionListChannelApiImpl
  implements ProcessDefinitionListChannelApi
{
  constructor(private readonly driver: ProcessDefinitionListDriver) {}
  processDefinitionList__openProcessForm(
    processDefinition: ProcessDefinition
  ): Promise<void> {
    return this.driver.openProcessForm(processDefinition);
  }

  processDefinitionList__setProcessDefinitionFilter(
    filter: string[]
  ): Promise<void> {
    return this.driver.setProcessDefinitionFilter(filter);
  }
  processDefinitionList__getProcessDefinitionFilter(): Promise<string[]> {
    return this.driver.getProcessDefinitionFilter();
  }

  processDefinitionList__getProcessDefinitionsQuery(): Promise<
    ProcessDefinition[]
  > {
    return this.driver.getProcessDefinitionsQuery();
  }

  processDefinitionsList__openTriggerCloudEvent(): void {
    this.driver.openTriggerCloudEvent();
  }
}
