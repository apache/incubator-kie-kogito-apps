import { MessageBusClientApi } from '@kie-tools-core/envelope-bus/dist/api';
import { WorkflowFormChannelApi, WorkflowFormDriver } from '../api';

/**
 * Implementation of WorkflowFormDriver to be used on WorkflowFormEnvelopeView
 */
export class WorkflowFormEnvelopeViewDriver implements WorkflowFormDriver {
  constructor(
    private readonly channelApi: MessageBusClientApi<WorkflowFormChannelApi>
  ) {}

  resetBusinessKey(): Promise<void> {
    return this.channelApi.requests.workflowForm__resetBusinessKey();
  }

  getCustomWorkflowSchema(): Promise<Record<string, any>> {
    return this.channelApi.requests.workflowForm__getCustomWorkflowSchema();
  }

  startWorkflow(endpoint: string, data: Record<string, any>): Promise<void> {
    return this.channelApi.requests.workflowForm__startWorkflow(endpoint, data);
  }
}
