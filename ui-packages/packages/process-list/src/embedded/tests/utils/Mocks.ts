import { ProcessListDriver } from '../../../api';

export const MockedProcessListDriver = jest.fn<ProcessListDriver, []>(() => ({
  initialLoad: jest.fn(),
  openProcess: jest.fn(),
  applySorting: jest.fn(),
  applyFilter: jest.fn(),
  handleProcessSkip: jest.fn(),
  handleProcessRetry: jest.fn(),
  handleProcessAbort: jest.fn(),
  handleProcessMultipleAction: jest.fn(),
  query: jest.fn(),
  getChildProcessesQuery: jest.fn(),
  openTriggerCloudEvent: jest.fn()
}));
