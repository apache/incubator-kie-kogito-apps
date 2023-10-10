import { WorkflowFormDriver } from '../../api';
import { EmbeddedWorkflowFormChannelApiImpl } from '../EmbeddedWorkflowFormChannelApiImpl';
import { MockedWorkflowFormDriver } from './mocks/Mocks';

let driver: WorkflowFormDriver;
let api: EmbeddedWorkflowFormChannelApiImpl;

describe('EmbeddedWorkflowFormChannelApiImpl tests', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    driver = new MockedWorkflowFormDriver();
    api = new EmbeddedWorkflowFormChannelApiImpl(driver);
  });

  it('WorkflowForm__getCustomWorkflowSchema', () => {
    api.workflowForm__getCustomWorkflowSchema();

    expect(driver.getCustomWorkflowSchema).toHaveBeenCalled();
  });

  it('WorkflowForm__startWorkflowRest', () => {
    api.workflowForm__startWorkflow('http://localhost:8080/test', {
      name: 'John'
    });

    expect(driver.startWorkflow).toHaveBeenCalledWith(
      'http://localhost:8080/test',
      { name: 'John' }
    );
  });

  it('WorkflowForm__resetBusinessKey', () => {
    api.workflowForm__resetBusinessKey();

    expect(driver.resetBusinessKey).toHaveBeenCalled();
  });
});
