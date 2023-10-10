import { ProcessDefinition } from '@kogito-apps/process-definition-list';
import { getProcessSchema, startProcessInstance } from '../../apis';
import {
  ProcessFormGatewayApi,
  ProcessFormGatewayApiImpl
} from '../ProcessFormGatewayApi';

jest.mock('../../apis/apis', () => ({
  getProcessSchema: jest.fn(),
  startProcessInstance: jest.fn()
}));

let gatewayApi: ProcessFormGatewayApi;

describe('ProcessFormListGatewayApi tests', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    gatewayApi = new ProcessFormGatewayApiImpl();
  });

  it('get process definition query', async () => {
    const processDefinitionData = {
      processName: 'process1',
      endpoint: 'http://localhost:8080/hiring'
    };
    await gatewayApi.getProcessFormSchema(processDefinitionData);
    expect(getProcessSchema).toHaveBeenCalledWith(processDefinitionData);
  });

  it('getter and setter on filter', async () => {
    const businesskey = 'AAA';
    gatewayApi.setBusinessKey(businesskey);
    expect(await gatewayApi.getBusinessKey()).toEqual(businesskey);
  });

  it('start process instance', async () => {
    const processDefinitionData: ProcessDefinition = {
      processName: 'process1',
      endpoint: 'http://localhost:8080/hiring'
    };
    gatewayApi.setBusinessKey('AAA');
    await gatewayApi.startProcess({}, processDefinitionData);
    expect(startProcessInstance).toHaveBeenCalledWith(
      {},
      'AAA',
      processDefinitionData
    );
  });
});
