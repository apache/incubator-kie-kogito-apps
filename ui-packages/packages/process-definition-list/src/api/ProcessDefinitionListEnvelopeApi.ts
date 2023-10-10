export interface ProcessDefinitionListEnvelopeApi {
  processDefinitionList__init(
    association: Association,
    initArgs: ProcessDefinitionListInitArgs
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

export interface ProcessDefinitionListInitArgs {
  singularProcessLabel: string;
  isTriggerCloudEventEnabled?: boolean;
}
