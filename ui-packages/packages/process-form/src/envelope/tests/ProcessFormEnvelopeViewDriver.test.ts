import {
  MessageBusClientApi,
  RequestPropertyNames
} from '@kie-tools-core/envelope-bus/dist/api';
import { ProcessFormChannelApi } from '../../api';
import { ProcessFormEnvelopeViewDriver } from '../ProcessFormEnvelopeViewDriver';
import { MockedMessageBusClientApi } from './mocks/Mocks';

let channelApi: MessageBusClientApi<ProcessFormChannelApi>;
let requests: Pick<
  ProcessFormChannelApi,
  RequestPropertyNames<ProcessFormChannelApi>
>;
let driver: ProcessFormEnvelopeViewDriver;

describe('ProcessFormEnvelopeViewDriver tests', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    channelApi = new MockedMessageBusClientApi();
    requests = channelApi.requests;
    driver = new ProcessFormEnvelopeViewDriver(channelApi);
  });

  it('getProcessFormSchema', () => {
    const processDefinitionData = {
      processName: 'process1',
      endpoint: 'http://localhost:4000'
    };
    driver.getProcessFormSchema(processDefinitionData);

    expect(requests.processForm__getProcessFormSchema).toHaveBeenCalledWith(
      processDefinitionData
    );
  });

  it('doSubmit', () => {
    const formJSON = {
      something: 'something'
    };
    driver.startProcess(formJSON);

    expect(requests.processForm__startProcess).toHaveBeenCalledWith(formJSON);
  });
});
