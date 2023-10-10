import { ProcessDefinition } from './ProcessFormEnvelopeApi';

export interface ProcessFormChannelApi {
  processForm__getProcessFormSchema(
    processDefinitionData: ProcessDefinition
  ): Promise<Record<string, any>>;
  processForm__startProcess(formData: any): Promise<void>;
}
