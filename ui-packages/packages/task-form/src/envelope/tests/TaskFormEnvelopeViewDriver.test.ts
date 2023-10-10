import {
  MessageBusClientApi,
  RequestPropertyNames
} from '@kie-tools-core/envelope-bus/dist/api';
import { TaskFormChannelApi } from '../../api';
import { TaskFormEnvelopeViewDriver } from '../TaskFormEnvelopeViewDriver';
import { MockedMessageBusClientApi } from './mocks/Mocks';

let channelApi: MessageBusClientApi<TaskFormChannelApi>;
let requests: Pick<
  TaskFormChannelApi,
  RequestPropertyNames<TaskFormChannelApi>
>;
let driver: TaskFormEnvelopeViewDriver;

describe('TaskFormEnvelopeViewDriver tests', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    channelApi = new MockedMessageBusClientApi();
    requests = channelApi.requests;
    driver = new TaskFormEnvelopeViewDriver(channelApi);
  });

  it('getTaskFormSchema', () => {
    driver.getTaskFormSchema();

    expect(requests.taskForm__getTaskFormSchema).toHaveBeenCalled();
  });

  it('doSubmit', () => {
    driver.doSubmit('complete', {});

    expect(requests.taskForm__doSubmit).toHaveBeenCalledWith('complete', {});
  });
});
