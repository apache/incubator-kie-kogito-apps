import {
  MessageBusClientApi,
  RequestPropertyNames
} from '@kie-tools-core/envelope-bus/dist/api';
import { WorkflowFormChannelApi } from '../../api';
import { WorkflowFormEnvelopeViewDriver } from '../WorkflowFormEnvelopeViewDriver';
import { MockedMessageBusClientApi } from './mocks/Mocks';

let channelApi: MessageBusClientApi<WorkflowFormChannelApi>;
let requests: Pick<
  WorkflowFormChannelApi,
  RequestPropertyNames<WorkflowFormChannelApi>
>;
let driver: WorkflowFormEnvelopeViewDriver;

describe('WorkflowFormEnvelopeViewDriver tests', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    channelApi = new MockedMessageBusClientApi();
    requests = channelApi.requests;
    driver = new WorkflowFormEnvelopeViewDriver(channelApi);
  });

  it('start custom workflow', () => {
    driver.startWorkflow('http://localhost:8080/test', { name: 'John' });
    expect(requests.workflowForm__startWorkflow).toHaveBeenCalledWith(
      'http://localhost:8080/test',
      { name: 'John' }
    );
  });
});
