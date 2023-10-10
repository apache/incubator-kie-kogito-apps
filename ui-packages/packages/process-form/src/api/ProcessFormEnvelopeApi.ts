export interface ProcessFormEnvelopeApi {
  processForm__init(
    association: Association,
    processDefinition: ProcessDefinition
  ): Promise<void>;
}

export interface Association {
  origin: string;
  envelopeServerId: string;
}

export interface ProcessDefinition {
  processName: string;
  endpoint: string;
}
