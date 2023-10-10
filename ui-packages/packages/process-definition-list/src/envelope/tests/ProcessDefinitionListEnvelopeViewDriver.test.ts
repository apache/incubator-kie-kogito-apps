import {
  MessageBusClientApi,
  RequestPropertyNames
} from '@kie-tools-core/envelope-bus/dist/api';
import { MockedMessageBusClientApi } from './mocks/Mocks';
import ProcessDefinitionListEnvelopeViewDriver from '../ProcessDefinitionListEnvelopeViewDriver';
import { ProcessDefinition, ProcessDefinitionListChannelApi } from '../../api';

let channelApi: MessageBusClientApi<ProcessDefinitionListChannelApi>;
let requests: Pick<
  ProcessDefinitionListChannelApi,
  RequestPropertyNames<ProcessDefinitionListChannelApi>
>;
let driver: ProcessDefinitionListEnvelopeViewDriver;

describe('ProcessDefinitionListEnvelopeViewDriver tests', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    channelApi = new MockedMessageBusClientApi();
    requests = channelApi.requests;
    driver = new ProcessDefinitionListEnvelopeViewDriver(channelApi);
  });

  describe('Requests', () => {
    it('get ProcessDefinition query', () => {
      driver.getProcessDefinitionsQuery();
      expect(
        requests.processDefinitionList__getProcessDefinitionsQuery
      ).toHaveBeenCalled();
    });

    it('get ProcessDefinition filter', () => {
      driver.getProcessDefinitionFilter();
      expect(
        requests.processDefinitionList__getProcessDefinitionFilter
      ).toHaveBeenCalled();
    });

    it('set ProcessDefinition filter', () => {
      const filter = ['process1'];
      driver.setProcessDefinitionFilter(filter);
      expect(
        requests.processDefinitionList__setProcessDefinitionFilter
      ).toHaveBeenCalledWith(filter);
    });

    it('open form', () => {
      const processDefinition: ProcessDefinition = {
        processName: 'process1',
        endpoint: 'http://localhost:4000'
      };
      driver.openProcessForm(processDefinition);
      expect(
        requests.processDefinitionList__openProcessForm
      ).toHaveBeenCalledWith(processDefinition);
    });
  });
});
