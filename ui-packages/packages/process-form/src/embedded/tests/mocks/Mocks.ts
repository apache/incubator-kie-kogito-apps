import { ProcessFormDriver } from '../../../api';

export const MockedProcessFormDriver = jest.fn<ProcessFormDriver, []>(() => ({
  getProcessFormSchema: jest.fn(),
  startProcess: jest.fn()
}));
