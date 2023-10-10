import { getCustomWorkflowSchema, startWorkflowRest } from '../apis';

export interface WorkflowFormGatewayApi {
  setBusinessKey(bk: string): void;
  getBusinessKey(): string;
  getCustomWorkflowSchema(workflowName: string): Promise<Record<string, any>>;
  startWorkflow(endpoint: string, data: Record<string, any>): Promise<string>;
}

export class WorkflowFormGatewayApiImpl implements WorkflowFormGatewayApi {
  private businessKey: string;
  private readonly baseUrl: string;
  private readonly openApiPath: string;

  constructor(baseUrl: string, openApiPath: string) {
    this.businessKey = '';
    this.baseUrl = baseUrl;
    this.openApiPath = openApiPath;
  }

  setBusinessKey(bk: string): void {
    this.businessKey = bk;
  }

  getBusinessKey(): string {
    return this.businessKey;
  }

  getCustomWorkflowSchema(workflowName: string): Promise<Record<string, any>> {
    return getCustomWorkflowSchema(
      this.baseUrl,
      this.openApiPath,
      workflowName
    );
  }

  startWorkflow(endpoint: string, data: Record<string, any>): Promise<string> {
    return startWorkflowRest(data, endpoint, this.businessKey);
  }
}
