/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import {
  QueryFilter,
  Job,
  SortBy,
  BulkCancel,
  JobCancel
} from '@kogito-apps/jobs-management';
import { JobsManagementQueries } from './JobsManagementQueries';
import { performMultipleCancel, jobCancel } from '../../apis/apis';
import { GraphQL } from '@kogito-apps/consoles-common';

export interface JobsManagementGatewayApi {
  jobsManagementState: any;
  initialLoad: (filter: GraphQL.JobStatus[], orderBy: SortBy) => Promise<void>;
  applyFilter: (filter: GraphQL.JobStatus[]) => Promise<void>;
  bulkCancel: (jobsToBeActioned: GraphQL.Job[]) => Promise<BulkCancel>;
  cancelJob: (job: Pick<GraphQL.Job, 'id' | 'endpoint'>) => Promise<JobCancel>;
  rescheduleJob: () => Promise<void>;
  sortBy: (orderBy: SortBy) => Promise<void>;
  query(offset: number, limit: number): Promise<Job[]>;
}
export interface JobsManagementState {
  filters: QueryFilter;
  orderBy: SortBy | any;
}
export class JobsManagementGatewayApiImpl implements JobsManagementGatewayApi {
  private readonly queries: JobsManagementQueries;
  private _JobsManagementState: JobsManagementState;

  constructor(queries: JobsManagementQueries) {
    this.queries = queries;
    this._JobsManagementState = { filters: [], orderBy: {} };
  }

  get jobsManagementState(): JobsManagementState {
    return this._JobsManagementState;
  }

  initialLoad = (filter: QueryFilter, orderBy: SortBy): Promise<any> => {
    this._JobsManagementState.filters = filter;
    this._JobsManagementState.orderBy = orderBy;
    return Promise.resolve();
  };

  applyFilter = (filter: QueryFilter): Promise<void> => {
    this._JobsManagementState.filters = filter;
    return Promise.resolve();
  };

  cancelJob = async (
    job: Pick<GraphQL.Job, 'id' | 'endpoint'>
  ): Promise<JobCancel> => {
    const cancelResult: JobCancel = await jobCancel(job);
    return cancelResult;
  };

  rescheduleJob = () => {
    return Promise.resolve();
  };

  bulkCancel = (
    jobsToBeActioned: (GraphQL.Job & { errorMessage?: string })[]
  ): Promise<BulkCancel> => {
    return performMultipleCancel(jobsToBeActioned);
  };

  sortBy = (orderBy: SortBy): Promise<void> => {
    this._JobsManagementState.orderBy = orderBy;
    return Promise.resolve();
  };

  query(offset: number, limit: number): Promise<Job[]> {
    return new Promise<Job[]>((resolve, reject) => {
      this.queries
        .getJobs(
          offset,
          limit,
          this._JobsManagementState.filters,
          this._JobsManagementState.orderBy
        )
        .then(value => {
          resolve(value);
        })
        .catch(reason => {
          reject(reason);
        });
    });
  }
}
