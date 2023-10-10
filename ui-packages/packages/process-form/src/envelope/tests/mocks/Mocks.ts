import {
  ApiNotificationConsumers,
  MessageBusClientApi,
  RequestPropertyNames
} from '@kie-tools-core/envelope-bus/dist/api';
import { EnvelopeClient } from '@kie-tools-core/envelope-bus/dist/envelope';
import { ProcessFormChannelApi, ProcessFormEnvelopeApi } from '../../../api';
import { ProcessFormEnvelopeViewApi } from '../../ProcessFormEnvelopeView';

export const MockedApiRequests = jest.fn<
  Pick<ProcessFormChannelApi, RequestPropertyNames<ProcessFormChannelApi>>,
  []
>(() => ({
  processForm__getProcessFormSchema: jest.fn(),
  processForm__startProcess: jest.fn()
}));

export const MockedApiNotifications = jest.fn<
  ApiNotificationConsumers<ProcessFormChannelApi>,
  []
>(() => ({}));

export const MockedMessageBusClientApi = jest.fn<
  MessageBusClientApi<ProcessFormChannelApi>,
  []
>(() => ({
  requests: new MockedApiRequests(),
  notifications: new MockedApiNotifications(),
  subscribe: jest.fn(),
  unsubscribe: jest.fn(),
  shared: jest.fn()
}));

export const MockedEnvelopeClient = jest.fn<
  EnvelopeClient<ProcessFormEnvelopeApi, ProcessFormChannelApi>,
  []
>(() => ({
  bus: jest.fn(),
  // eslint-disable-next-line @typescript-eslint/ban-ts-comment
  // @ts-ignore
  manager: jest.fn(),
  associate: jest.fn(),
  channelApi: new MockedMessageBusClientApi(),
  startListening: jest.fn(),
  stopListening: jest.fn(),
  send: jest.fn(),
  receive: jest.fn()
}));

export const MockedProcessFormEnvelopeViewApi = jest.fn<
  ProcessFormEnvelopeViewApi,
  []
>(() => ({
  initialize: jest.fn()
}));
