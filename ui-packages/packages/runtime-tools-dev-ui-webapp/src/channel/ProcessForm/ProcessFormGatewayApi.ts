import { ProcessDefinition } from '@kogito-apps/process-definition-list';
import { getProcessSchema, startProcessInstance } from '../apis';

export interface ProcessFormGatewayApi {
  getProcessFormSchema(processDefinitionData: ProcessDefinition): Promise<any>;
  startProcess(
    formData: any,
    processDefinitionData: ProcessDefinition
  ): Promise<string>;
  setBusinessKey(bk: string): void;
  getBusinessKey(): string;
}

export class ProcessFormGatewayApiImpl implements ProcessFormGatewayApi {
  private businessKey: string;
  constructor() {
    this.businessKey = '';
  }

  setBusinessKey(bk: string): void {
    this.businessKey = bk;
  }

  getBusinessKey(): string {
    return this.businessKey;
  }

  getProcessFormSchema(
    processDefinitionData: ProcessDefinition
  ): Promise<Record<string, any>> {
    return getProcessSchema(processDefinitionData);
  }

  startProcess(
    formData: any,
    processDefinitionData: ProcessDefinition
  ): Promise<string> {
    return startProcessInstance(
      formData,
      this.businessKey,
      processDefinitionData
    );
  }
}
