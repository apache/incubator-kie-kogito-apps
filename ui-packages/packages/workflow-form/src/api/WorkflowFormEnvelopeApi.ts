export interface WorkflowFormEnvelopeApi {
  workflowForm__init(
    association: Association,
    workflowDefinition: WorkflowDefinition
  ): Promise<void>;
}

export interface Association {
  origin: string;
  envelopeServerId: string;
}

export interface WorkflowDefinition {
  workflowName: string;
  endpoint: string;
}
