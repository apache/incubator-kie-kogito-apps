import { WorkflowFormDriver } from '../../../api';

export const MockedWorkflowFormDriver = jest.fn<WorkflowFormDriver, []>(() => ({
  resetBusinessKey: jest.fn(),
  getCustomWorkflowSchema: jest.fn(),
  startWorkflow: jest.fn()
}));
