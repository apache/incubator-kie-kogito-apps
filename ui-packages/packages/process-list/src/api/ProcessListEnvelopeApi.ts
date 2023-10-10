import { ProcessListState } from '@kogito-apps/management-console-shared/dist/types';
export interface ProcessListEnvelopeApi {
  processList__init(
    association: Association,
    initArgs: ProcessListInitArgs
  ): Promise<void>;
}

export interface Association {
  origin: string;
  envelopeServerId: string;
}

export interface QueryPage {
  offset: number;
  limit: number;
}

export interface ProcessListInitArgs {
  initialState: ProcessListState;
  singularProcessLabel: string;
  pluralProcessLabel: string;
  isWorkflow: boolean;
  isTriggerCloudEventEnabled?: boolean;
}
