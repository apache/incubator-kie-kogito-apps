import {
  BulkProcessInstanceActionResponse,
  ProcessInstance,
  ProcessInstanceFilter,
  ProcessListSortBy
} from '@kogito-apps/management-console-shared/dist/types';
import { OperationType } from '@kogito-apps/management-console-shared/dist/components/BulkList';
export interface ProcessListChannelApi {
  processList__initialLoad(
    filter: ProcessInstanceFilter,
    sortBy: ProcessListSortBy
  ): Promise<void>;
  processList__openProcess(process: ProcessInstance): Promise<void>;
  processList__applyFilter(filter: ProcessInstanceFilter): Promise<void>;
  processList__applySorting(sortBy: ProcessListSortBy): Promise<void>;
  processList__handleProcessSkip(
    processInstance: ProcessInstance
  ): Promise<void>;
  processList__handleProcessRetry(
    processInstance: ProcessInstance
  ): Promise<void>;
  processList__handleProcessAbort(
    processInstance: ProcessInstance
  ): Promise<void>;
  processList__handleProcessMultipleAction(
    processInstances: ProcessInstance[],
    operationType: OperationType
  ): Promise<BulkProcessInstanceActionResponse>;
  processList__query(offset: number, limit: number): Promise<ProcessInstance[]>;
  processList__getChildProcessesQuery(
    rootProcessInstanceId: string
  ): Promise<ProcessInstance[]>;
  processList__openTriggerCloudEvent(processInstance?: ProcessInstance): void;
}
