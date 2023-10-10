import { ApolloClient } from 'apollo-client';
import {
  Job,
  JobStatus,
  JobsSortBy
} from '@kogito-apps/management-console-shared/dist/types';
import { getJobsWithFilters } from '@kogito-apps/runtime-gateway-api';

export interface JobsManagementQueries {
  getJobs(
    start: number,
    end: number,
    filters: JobStatus[],
    sortBy: JobsSortBy | any
  ): Promise<Job[]>;
}

export class GraphQLJobsManagementQueries implements JobsManagementQueries {
  private readonly client: ApolloClient<any>;

  constructor(client: ApolloClient<any>) {
    this.client = client;
  }

  async getJobs(
    offset: number,
    limit: number,
    filters: JobStatus[],
    orderBy: JobsSortBy
  ): Promise<Job[]> {
    return getJobsWithFilters(offset, limit, filters, orderBy, this.client);
  }
}
