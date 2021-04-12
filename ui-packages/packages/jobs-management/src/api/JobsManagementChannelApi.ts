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

import { Job, JobStatus } from '../types';
import { SortBy, BulkCancel, JobCancel } from './JobsManagementEnvelopeApi';
export interface JobsManagementChannelApi {
  jobList__initialLoad(filter: JobStatus[], orderBy: SortBy): Promise<void>;
  jobList__applyFilter(filter: JobStatus[]): Promise<void>;
  jobList__bulkCancel(jobsToBeActioned: Job[]): Promise<BulkCancel>;
  jobList_cancelJob(job: Pick<Job, 'id' | 'endpoint'>): Promise<JobCancel>;
  jobList_rescheduleJob(job: Job): Promise<void>;
  jobList_sortBy(orderBy: SortBy): Promise<void>;
  jobList__query(offset: number, limit: number): Promise<Job[]>;
}
