import { JobsManagementChannelApi, JobsManagementDriver } from '../api';
import {
  Job,
  JobStatus,
  BulkCancel,
  JobCancel,
  JobsSortBy
} from '@kogito-apps/management-console-shared/dist/types';

export class JobsManagementChannelApiImpl implements JobsManagementChannelApi {
  constructor(private readonly driver: JobsManagementDriver) {}

  jobList__initialLoad(
    filter: JobStatus[],
    orderBy: JobsSortBy
  ): Promise<void> {
    return this.driver.initialLoad(filter, orderBy);
  }

  jobList__applyFilter(filter: JobStatus[]): Promise<void> {
    return this.driver.applyFilter(filter);
  }

  jobList__bulkCancel(jobsToBeActioned: Job[]): Promise<BulkCancel> {
    return this.driver.bulkCancel(jobsToBeActioned);
  }

  jobList_cancelJob(job: Pick<Job, 'id' | 'endpoint'>): Promise<JobCancel> {
    return this.driver.cancelJob(job);
  }

  jobList_rescheduleJob(
    job,
    repeatInterval: number | string,
    repeatLimit: number | string,
    scheduleDate: Date
  ): Promise<{ modalTitle: string; modalContent: string }> {
    return this.driver.rescheduleJob(
      job,
      repeatInterval,
      repeatLimit,
      scheduleDate
    );
  }

  jobList_sortBy(orderBy: JobsSortBy): Promise<void> {
    return this.driver.sortBy(orderBy);
  }

  jobList__query(offset: number, limit: number): Promise<Job[]> {
    return this.driver.query(offset, limit);
  }
}
