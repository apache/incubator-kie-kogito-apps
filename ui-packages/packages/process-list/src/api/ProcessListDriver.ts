import {
  BulkProcessInstanceActionResponse,
  ProcessInstance,
  ProcessInstanceFilter,
  ProcessListSortBy
} from '@kogito-apps/management-console-shared/dist/types';
import { OperationType } from '@kogito-apps/management-console-shared/dist/components/BulkList';

export interface ProcessListDriver {
  initialLoad(
    filter: ProcessInstanceFilter,
    sortBy: ProcessListSortBy
  ): Promise<void>;
  openProcess(process: ProcessInstance): Promise<void>;
  applyFilter(filter: ProcessInstanceFilter): Promise<void>;
  applySorting(sortBy: ProcessListSortBy): Promise<void>;
  handleProcessSkip(processInstance: ProcessInstance): Promise<void>;
  handleProcessRetry(processInstance: ProcessInstance): Promise<void>;
  handleProcessAbort(processInstance: ProcessInstance): Promise<void>;
  handleProcessMultipleAction(
    processInstances: ProcessInstance[],
    operationType: OperationType
  ): Promise<BulkProcessInstanceActionResponse>;
  query(offset: number, limit: number): Promise<ProcessInstance[]>;
  getChildProcessesQuery(
    rootProcessInstanceId: string
  ): Promise<ProcessInstance[]>;
  openTriggerCloudEvent(processInstance?: ProcessInstance): void;
}
