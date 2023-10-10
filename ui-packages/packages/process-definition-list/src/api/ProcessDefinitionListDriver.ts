import { ProcessDefinition } from './ProcessDefinitionListEnvelopeApi';

/**
 * Interface that defines a Driver for ProcessDefinitionList views.
 */
export interface ProcessDefinitionListDriver {
  getProcessDefinitionsQuery(): Promise<ProcessDefinition[]>;
  setProcessDefinitionFilter(filter: string[]): Promise<void>;
  getProcessDefinitionFilter(): Promise<string[]>;
  openProcessForm(processDefinition: ProcessDefinition): Promise<void>;
  openTriggerCloudEvent(): void;
}
