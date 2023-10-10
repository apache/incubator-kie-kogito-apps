import {
  BulkProcessInstanceActionResponse,
  ProcessInstance,
  ProcessInstanceFilter,
  ProcessListSortBy
} from '@kogito-apps/management-console-shared/dist/types';
import { OperationType } from '@kogito-apps/management-console-shared/dist/components/BulkList';
import { ProcessListDriver } from '../../../../../api';

export default class TestProcessListDriver implements ProcessListDriver {
  private readonly processInstances: ProcessInstance[];
  private readonly childProcessInstances: ProcessInstance[];
  private offset: number = 0;
  private limit: number = 10;

  constructor(
    processInstances: ProcessInstance[],
    childProcessInstances: ProcessInstance[]
  ) {
    this.processInstances = processInstances;
    this.childProcessInstances = childProcessInstances;
  }
  handleProcessMultipleAction(
    processInstances: ProcessInstance[],
    operationType: OperationType
  ): Promise<BulkProcessInstanceActionResponse> {
    return Promise.resolve({
      successProcessInstances: [],
      failedProcessInstances: []
    });
  }
  handleProcessSkip(processInstance: ProcessInstance): Promise<void> {
    return Promise.resolve();
  }
  handleProcessRetry(processInstance: ProcessInstance): Promise<void> {
    return Promise.resolve();
  }
  handleProcessAbort(processInstance: ProcessInstance): Promise<void> {
    return Promise.resolve();
  }
  /* eslint-disable  @typescript-eslint/no-unused-vars */
  private doSetState(
    processListFilter: ProcessInstanceFilter,
    sortBy: ProcessListSortBy
  ) {
    // do nothing
  }

  initialLoad(
    filter: ProcessInstanceFilter,
    sortBy: ProcessListSortBy
  ): Promise<void> {
    this.doSetState(filter, sortBy);
    return Promise.resolve();
  }

  openProcess(process: ProcessInstance): Promise<void> {
    return Promise.resolve();
  }
  applyFilter(filter: ProcessInstanceFilter): Promise<void> {
    // do nothing
    return Promise.resolve();
  }

  applySorting(sortBy: ProcessListSortBy): Promise<void> {
    // do nothing
    return Promise.resolve(undefined);
  }

  query(offset: number, limit: number): Promise<ProcessInstance[]> {
    this.offset = offset;
    this.limit = limit;
    return this.doQuery(offset, this.getQueryLimit());
  }

  getChildProcessesQuery(
    rootProcessInstanceId: string
  ): Promise<ProcessInstance[]> {
    return Promise.resolve(this.childProcessInstances.slice(0, 10));
  }
  /* eslint-enable  @typescript-eslint/no-unused-vars */
  private doQuery(start: number, limit: number): Promise<ProcessInstance[]> {
    const queryLimit =
      limit > this.processInstances.length
        ? this.processInstances.length
        : limit;

    return Promise.resolve(this.processInstances.slice(start, queryLimit));
  }

  private getQueryLimit = (): number => {
    return this.offset + this.limit;
  };

  openTriggerCloudEvent(): Promise<void> {
    return Promise.resolve();
  }
}
