import { MessageBusClientApi } from '@kie-tools-core/envelope-bus/dist/api';
import {
  BulkProcessInstanceActionResponse,
  ProcessInstance,
  ProcessInstanceFilter,
  ProcessListSortBy
} from '@kogito-apps/management-console-shared/dist/types';
import { OperationType } from '@kogito-apps/management-console-shared/dist/components/BulkList';
import { ProcessListChannelApi, ProcessListDriver } from '../api';

export default class ProcessListEnvelopeViewDriver
  implements ProcessListDriver
{
  constructor(
    private readonly channelApi: MessageBusClientApi<ProcessListChannelApi>
  ) {}
  initialLoad(
    filter: ProcessInstanceFilter,
    sortBy: ProcessListSortBy
  ): Promise<void> {
    return this.channelApi.requests.processList__initialLoad(filter, sortBy);
  }
  openProcess(process: ProcessInstance): Promise<void> {
    return this.channelApi.requests.processList__openProcess(process);
  }
  applyFilter(filter: ProcessInstanceFilter): Promise<void> {
    return this.channelApi.requests.processList__applyFilter(filter);
  }
  applySorting(sortBy: ProcessListSortBy): Promise<void> {
    return this.channelApi.requests.processList__applySorting(sortBy);
  }
  handleProcessSkip(processInstance: ProcessInstance): Promise<void> {
    return this.channelApi.requests.processList__handleProcessSkip(
      processInstance
    );
  }
  handleProcessRetry(processInstance: ProcessInstance): Promise<void> {
    return this.channelApi.requests.processList__handleProcessRetry(
      processInstance
    );
  }
  handleProcessAbort(processInstance: ProcessInstance): Promise<void> {
    return this.channelApi.requests.processList__handleProcessAbort(
      processInstance
    );
  }
  handleProcessMultipleAction(
    processInstances: ProcessInstance[],
    operationType: OperationType
  ): Promise<BulkProcessInstanceActionResponse> {
    return this.channelApi.requests.processList__handleProcessMultipleAction(
      processInstances,
      operationType
    );
  }
  query(offset: number, limit: number): Promise<ProcessInstance[]> {
    return this.channelApi.requests.processList__query(offset, limit);
  }
  getChildProcessesQuery(
    rootProcessInstanceId: string
  ): Promise<ProcessInstance[]> {
    return this.channelApi.requests.processList__getChildProcessesQuery(
      rootProcessInstanceId
    );
  }

  openTriggerCloudEvent(processInstance?: ProcessInstance): void {
    this.channelApi.notifications.processList__openTriggerCloudEvent.send(
      processInstance
    );
  }
}
