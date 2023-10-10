import { ProcessDefinition } from './ProcessFormEnvelopeApi';

/**
 * Interface that defines a Driver for ProcessForm views.
 */
export interface ProcessFormDriver {
  getProcessFormSchema(
    processDefinitionData: ProcessDefinition
  ): Promise<Record<string, any>>;
  startProcess(formData: any): Promise<void>;
}
