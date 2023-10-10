import { ApolloClient } from 'apollo-client';
import {
  BulkProcessInstanceActionResponse,
  ProcessInstance,
  ProcessListSortBy,
  ProcessInstanceFilter
} from '@kogito-apps/management-console-shared/dist/types';
import { OperationType } from '@kogito-apps/management-console-shared/dist/components/BulkList';
import {
  handleProcessAbort,
  handleProcessMultipleAction,
  handleProcessSkip,
  handleProcessRetry,
  getProcessInstances,
  getChildProcessInstances
} from '@kogito-apps/runtime-gateway-api';

export interface ProcessListQueries {
  getProcessInstances(
    start: number,
    end: number,
    filters: ProcessInstanceFilter,
    sortBy: ProcessListSortBy
  ): Promise<ProcessInstance[]>;
  getChildProcessInstances(
    rootProcessInstanceId: string
  ): Promise<ProcessInstance[]>;

  handleProcessSkip(processInstance: ProcessInstance): Promise<void>;
  handleProcessAbort(processInstance: ProcessInstance): Promise<void>;
  handleProcessRetry(processInstance: ProcessInstance): Promise<void>;

  handleProcessMultipleAction(
    processInstances: ProcessInstance[],
    operationType: OperationType
  ): Promise<BulkProcessInstanceActionResponse>;
}

export class GraphQLProcessListQueries implements ProcessListQueries {
  private readonly client: ApolloClient<any>;

  constructor(client: ApolloClient<any>) {
    this.client = client;
  }

  getProcessInstances(
    offset: number,
    limit: number,
    filters: ProcessInstanceFilter,
    sortBy: ProcessListSortBy
  ): Promise<ProcessInstance[]> {
    return getProcessInstances(offset, limit, filters, sortBy, this.client);
  }

  getChildProcessInstances(
    rootProcessInstanceId: string
  ): Promise<ProcessInstance[]> {
    return getChildProcessInstances(rootProcessInstanceId, this.client);
  }

  async handleProcessSkip(processInstance: ProcessInstance): Promise<void> {
    return handleProcessSkip(processInstance, this.client);
  }

  async handleProcessAbort(processInstance: ProcessInstance): Promise<void> {
    return handleProcessAbort(processInstance, this.client);
  }

  async handleProcessRetry(processInstance: ProcessInstance): Promise<void> {
    return handleProcessRetry(processInstance, this.client);
  }

  async handleProcessMultipleAction(
    processInstances: ProcessInstance[],
    operationType: OperationType
  ) {
    return handleProcessMultipleAction(
      processInstances,
      operationType,
      this.client
    );
  }
}
