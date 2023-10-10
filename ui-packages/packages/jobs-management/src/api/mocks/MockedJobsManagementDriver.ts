import { JobsManagementDriver } from '../JobsManagementDriver';

export const MockedJobsManagementDriver = jest.fn<JobsManagementDriver, []>(
  () => ({
    initialLoad: jest.fn(),
    applyFilter: jest.fn(),
    bulkCancel: jest.fn(),
    cancelJob: jest.fn(),
    rescheduleJob: jest.fn(),
    sortBy: jest.fn(),
    query: jest.fn()
  })
);
