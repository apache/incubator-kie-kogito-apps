export interface WorkflowFormChannelApi {
  workflowForm__resetBusinessKey(): Promise<void>;
  workflowForm__getCustomWorkflowSchema(): Promise<Record<string, any>>;
  workflowForm__startWorkflow(
    endpoint: string,
    data: Record<string, any>
  ): Promise<void>;
}
