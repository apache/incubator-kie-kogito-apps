import { ProcessDefinition } from '@kogito-apps/process-definition-list';
import { getProcessDefinitionList } from '../../apis';
import {
  OnOpenProcessFormListener,
  ProcessDefinitionListGatewayApi,
  ProcessDefinitionListGatewayApiImpl
} from '../ProcessDefinitionListGatewayApi';

jest.mock('../../apis/apis', () => ({
  getProcessDefinitionList: jest.fn()
}));

let gatewayApi: ProcessDefinitionListGatewayApi;

describe('ProcessDefinitionListListGatewayApi tests', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    gatewayApi = new ProcessDefinitionListGatewayApiImpl(
      'http://localhost:8080',
      '/docs/openapi.json'
    );
  });

  it('get process definition query', async () => {
    gatewayApi.getProcessDefinitionsQuery();
    expect(getProcessDefinitionList).toHaveBeenCalled();
  });

  it('getter and setter on filter', async () => {
    const filter = ['process1'];
    gatewayApi.setProcessDefinitionFilter(filter);
    expect(await gatewayApi.getProcessDefinitionFilter()).toEqual(filter);
  });
  it('openForm', () => {
    const processDefinitionData: ProcessDefinition = {
      processName: 'process1',
      endpoint: 'http://localhost:8080/hiring'
    };
    const listener: OnOpenProcessFormListener = {
      onOpen: jest.fn()
    };

    const unsubscribe = gatewayApi.onOpenProcessFormListen(listener);

    gatewayApi.openProcessForm(processDefinitionData);

    expect(listener.onOpen).toHaveBeenLastCalledWith(processDefinitionData);

    unsubscribe.unSubscribe();
  });
});
