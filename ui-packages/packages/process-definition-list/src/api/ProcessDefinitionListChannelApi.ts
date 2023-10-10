import { ProcessDefinition } from './ProcessDefinitionListEnvelopeApi';

/**
 * Channel Api for Process Definition List
 */
export interface ProcessDefinitionListChannelApi {
  processDefinitionList__getProcessDefinitionsQuery(): Promise<
    ProcessDefinition[]
  >;
  processDefinitionList__setProcessDefinitionFilter(
    filter: string[]
  ): Promise<void>;
  processDefinitionList__getProcessDefinitionFilter(): Promise<string[]>;
  processDefinitionList__openProcessForm(
    processDefinition: ProcessDefinition
  ): Promise<void>;
  processDefinitionsList__openTriggerCloudEvent(): void;
}
