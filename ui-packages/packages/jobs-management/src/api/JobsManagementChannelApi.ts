import {
  Job,
  JobStatus,
  BulkCancel,
  JobCancel,
  JobsSortBy
} from '@kogito-apps/management-console-shared/dist/types';
export interface JobsManagementChannelApi {
  jobList__initialLoad(filter: JobStatus[], orderBy: JobsSortBy): Promise<void>;
  jobList__applyFilter(filter: JobStatus[]): Promise<void>;
  jobList__bulkCancel(jobsToBeActioned: Job[]): Promise<BulkCancel>;
  jobList_cancelJob(job: Pick<Job, 'id' | 'endpoint'>): Promise<JobCancel>;
  jobList_rescheduleJob(
    job,
    repeatInterval: number | string,
    repeatLimit: number | string,
    scheduleDate: Date
  ): Promise<{ modalTitle: string; modalContent: string }>;
  jobList_sortBy(orderBy: JobsSortBy): Promise<void>;
  jobList__query(offset: number, limit: number): Promise<Job[]>;
}
