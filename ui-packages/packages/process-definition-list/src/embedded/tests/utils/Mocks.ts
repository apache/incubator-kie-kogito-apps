import { ProcessDefinitionListDriver } from '../../../api';

export const MockedProcessDefinitionListDriver = jest.fn<
  ProcessDefinitionListDriver,
  []
>(() => ({
  getProcessDefinitionsQuery: jest.fn(),
  openProcessForm: jest.fn(),
  setProcessDefinitionFilter: jest.fn(),
  getProcessDefinitionFilter: jest.fn(),
  openTriggerCloudEvent: jest.fn()
}));
