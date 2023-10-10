import { getCustomWorkflowSchema, startWorkflowRest } from '../../apis';
import {
  WorkflowFormGatewayApi,
  WorkflowFormGatewayApiImpl
} from '../WorkflowFormGatewayApi';

jest.mock('../../apis/apis', () => ({
  getCustomWorkflowSchema: jest.fn(),
  startWorkflowRest: jest.fn()
}));

let gatewayApi: WorkflowFormGatewayApi;

describe('WorkflowFormListGatewayApi tests', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    gatewayApi = new WorkflowFormGatewayApiImpl('baseUrl', '/q/dev');
  });

  it('get custom workflow schema', async () => {
    const workflowName = 'expression';
    await gatewayApi.getCustomWorkflowSchema(workflowName);
    expect(getCustomWorkflowSchema).toHaveBeenCalledWith(
      'baseUrl',
      '/q/dev',
      'expression'
    );
  });

  it('start workflow rest', async () => {
    await gatewayApi.startWorkflow('http://localhost:8080/test', {
      name: 'John'
    });
    expect(startWorkflowRest).toHaveBeenCalledWith(
      { name: 'John' },
      'http://localhost:8080/test',
      ''
    );
  });
});
