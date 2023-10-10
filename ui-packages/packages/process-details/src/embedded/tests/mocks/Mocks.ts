import { ProcessDetailsDriver } from '../../../api';

export const MockedProcessDetailsDriver = jest.fn<ProcessDetailsDriver, []>(
  () => ({
    getProcessDiagram: jest.fn(),
    cancelJob: jest.fn(),
    rescheduleJob: jest.fn(),
    getTriggerableNodes: jest.fn(),
    handleNodeTrigger: jest.fn(),
    processDetailsQuery: jest.fn(),
    jobsQuery: jest.fn(),
    handleProcessAbort: jest.fn(),
    handleProcessVariableUpdate: jest.fn(),
    openProcessInstanceDetails: jest.fn(),
    handleProcessRetry: jest.fn(),
    handleNodeInstanceCancel: jest.fn(),
    handleProcessSkip: jest.fn(),
    handleNodeInstanceRetrigger: jest.fn()
  })
);
