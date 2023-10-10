/**
 * Interface that defines a Driver for WorkflowForm views.
 */
export interface WorkflowFormDriver {
  resetBusinessKey(): Promise<void>;
  getCustomWorkflowSchema(): Promise<Record<string, any>>;
  startWorkflow(endpoint: string, data: Record<string, any>): Promise<void>;
}
