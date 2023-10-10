import { WorkflowFormChannelApi, WorkflowFormDriver } from '../api';

export class EmbeddedWorkflowFormChannelApiImpl
  implements WorkflowFormChannelApi
{
  constructor(private readonly driver: WorkflowFormDriver) {}

  workflowForm__resetBusinessKey(): Promise<void> {
    return this.driver.resetBusinessKey();
  }

  workflowForm__getCustomWorkflowSchema(): Promise<Record<string, any>> {
    return this.driver.getCustomWorkflowSchema();
  }

  workflowForm__startWorkflow(
    endpoint: string,
    data: Record<string, any>
  ): Promise<void> {
    return this.driver.startWorkflow(endpoint, data);
  }
}
