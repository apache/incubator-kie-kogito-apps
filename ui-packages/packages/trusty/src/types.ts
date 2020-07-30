export type RemoteData<E, D> =
  | { status: 'NOT_ASKED' }
  | { status: 'LOADING' }
  | { status: 'FAILURE'; error: E }
  | { status: 'SUCCESS'; data: D };

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

export interface IExecutionRouteParams {
  executionId: string;
  executionType: string;
}

export interface IItemObject {
  name: string;
  typeRef: string;
  value: string | number | boolean | object | null;
  components: (IItemObject | IItemObject[])[];
  impact?: boolean | number;
  score?: number;
}

export function isIItemObjectArray(object: any): object is IItemObject[] {
  return typeof object[0].name === 'string';
}

export function isIItemObjectMultiArray(
  object: any
): object is IItemObject[][] {
  return Array.isArray(object[0]);
}

export enum evaluationStatus {
  EVALUATING = 'Evaluating',
  FAILED = 'Failed',
  NOT_EVALUATED = 'Not evaluated',
  SKIPPED = 'Skipped',
  SUCCEEDED = 'Succeeded'
}

export type evaluationStatusStrings = keyof typeof evaluationStatus;

export interface IOutcome {
  outcomeId: string;
  outcomeName: string;
  evaluationStatus: evaluationStatusStrings;
  hasErrors: boolean;
  messages: string[];
  outcomeResult: IItemObject;
}
