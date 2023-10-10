import { MessageBusClientApi } from '@kie-tools-core/envelope-bus/dist/api';
import {
  Job,
  JobStatus,
  BulkCancel,
  JobCancel,
  JobsSortBy
} from '@kogito-apps/management-console-shared/dist/types';
import { JobsManagementChannelApi, JobsManagementDriver } from '../api';

export default class JobsManagementEnvelopeViewDriver
  implements JobsManagementDriver
{
  constructor(
    private readonly channelApi: MessageBusClientApi<JobsManagementChannelApi>
  ) {}

  initialLoad(filter: JobStatus[], orderBy: JobsSortBy): Promise<void> {
    return this.channelApi.requests.jobList__initialLoad(filter, orderBy);
  }

  applyFilter(filter: JobStatus[]): Promise<void> {
    return this.channelApi.requests.jobList__applyFilter(filter);
  }

  bulkCancel(jobsToBeActioned: Job[]): Promise<BulkCancel> {
    return this.channelApi.requests.jobList__bulkCancel(jobsToBeActioned);
  }

  cancelJob(job: Pick<Job, 'id' | 'endpoint'>): Promise<JobCancel> {
    return this.channelApi.requests.jobList_cancelJob(job);
  }

  rescheduleJob(
    job,
    repeatInterval: number | string,
    repeatLimit: number | string,
    scheduleDate: Date
  ): Promise<{ modalTitle: string; modalContent: string }> {
    return this.channelApi.requests.jobList_rescheduleJob(
      job,
      repeatInterval,
      repeatLimit,
      scheduleDate
    );
  }

  sortBy(orderBy: JobsSortBy): Promise<void> {
    return this.channelApi.requests.jobList_sortBy(orderBy);
  }

  query(offset: number, limit: number): Promise<Job[]> {
    return this.channelApi.requests.jobList__query(offset, limit);
  }
}
