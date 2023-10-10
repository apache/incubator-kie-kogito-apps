import { TaskInboxDriver } from '../../../api';

export const MockedTaskInboxDriver = jest.fn<TaskInboxDriver, []>(() => ({
  setInitialState: jest.fn(),
  applySorting: jest.fn(),
  applyFilter: jest.fn(),
  query: jest.fn(),
  refresh: jest.fn(),
  openTask: jest.fn()
}));
