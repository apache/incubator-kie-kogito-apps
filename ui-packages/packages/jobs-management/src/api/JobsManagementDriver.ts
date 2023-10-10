import {
  Job,
  JobStatus,
  BulkCancel,
  JobCancel,
  JobsSortBy
} from '@kogito-apps/management-console-shared/dist/types';
export interface JobsManagementDriver {
  initialLoad(filter: JobStatus[], orderBy: JobsSortBy): Promise<void>;
  applyFilter(filter: JobStatus[]): Promise<void>;
  bulkCancel(jobsToBeActioned: Job[]): Promise<BulkCancel>;
  cancelJob(job: Pick<Job, 'id' | 'endpoint'>): Promise<JobCancel>;
  rescheduleJob(
    job,
    repeatInterval: number | string,
    repeatLimit: number | string,
    scheduleDate: Date
  ): Promise<{ modalTitle: string; modalContent: string }>;
  sortBy(orderBy: JobsSortBy): Promise<void>;
  query(offset: number, limit: number): Promise<Job[]>;
}
