export type RemoteData<E, D> =
  | { status: RemoteDataStatus.NOT_ASKED }
  | { status: RemoteDataStatus.LOADING }
  | { status: RemoteDataStatus.FAILURE; error: E }
  | { status: RemoteDataStatus.SUCCESS; data: D };

export enum RemoteDataStatus {
  NOT_ASKED,
  LOADING,
  FAILURE,
  SUCCESS
}

export interface Execution {
  executionId: string;
  executionDate: string;
  executedModelName: string;
  executionType: string;
  executionSucceeded: boolean;
  executorName: string;
}

export interface Executions {
  headers: Execution[];
  total: number;
}

export interface ExecutionRouteParams {
  executionId: string;
  executionType: string;
}

export interface ItemObject {
  name: string;
  typeRef: string;
  value: string | number | boolean | Array<string | number | boolean> | null;
  components: (ItemObject | ItemObject[])[] | null;
  impact?: boolean | number;
  score?: number;
}

export function isItemObjectArray(object: unknown): object is ItemObject[] {
  return typeof object[0].name === 'string';
}

export function isItemObjectMultiArray(
  object: unknown
): object is ItemObject[][] {
  return Array.isArray(object[0]);
}

export interface InputRow {
  inputLabel: string;
  inputValue?: ItemObject['value'];
  hasEffect?: boolean | number;
  score?: number;
  key: string;
  category: string;
}

export enum evaluationStatus {
  EVALUATING = 'Evaluating',
  FAILED = 'Failed',
  NOT_EVALUATED = 'Not evaluated',
  SKIPPED = 'Skipped',
  SUCCEEDED = 'Succeeded'
}

export type evaluationStatusStrings = keyof typeof evaluationStatus;

export interface Outcome {
  outcomeId: string;
  outcomeName: string;
  evaluationStatus: evaluationStatusStrings;
  hasErrors: boolean;
  messages: string[];
  outcomeResult: ItemObject;
}

export interface FeatureScores {
  featureName: string;
  featureId: string;
  featureScore: number;
}

export interface Saliency {
  outcomeId: string;
  featureImportance: FeatureScores[];
}
export enum SaliencyStatus {
  SUCCEEDED = 'SUCCEEDED',
  FAILED = 'FAILED'
}
export type SaliencyStatusStrings = keyof typeof SaliencyStatus;

export interface Saliencies {
  status: SaliencyStatusStrings;
  statusDetail: string;
  saliencies: Saliency[];
}

export interface ServiceIdentifier {
  groupId?: string;
  artifactId?: string;
  version?: string;
}

export interface ModelData {
  deploymentDate?: string;
  modelId?: string;
  name: string;
  namespace: string;
  modelVersion: string;
  dmnVersion: string;
  serviceIdentifier: ServiceIdentifier;
  model: string;
}
