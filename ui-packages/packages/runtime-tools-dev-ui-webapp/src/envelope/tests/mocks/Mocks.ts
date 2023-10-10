import {
  ApiNotificationConsumers,
  MessageBusClientApi,
  RequestPropertyNames
} from '@kie-tools-core/envelope-bus/dist/api';
import {
  RuntimeToolsDevUIChannelApi,
  RuntimeToolsDevUIEnvelopeApi
} from '../../../api';
import { EnvelopeClient } from '@kie-tools-core/envelope-bus/dist/envelope';
import { RuntimeToolsDevUIEnvelopeViewApi } from '../../RuntimeToolsDevUIEnvelopeViewApi';

export const MockedApiRequests = jest.fn<
  Pick<
    RuntimeToolsDevUIChannelApi,
    RequestPropertyNames<RuntimeToolsDevUIChannelApi>
  >,
  []
>(() => ({}));

export const MockedApiNotifications = jest.fn<
  ApiNotificationConsumers<RuntimeToolsDevUIChannelApi>,
  []
>(() => ({}));

export const MockedMessageBusClientApi = jest.fn<
  MessageBusClientApi<RuntimeToolsDevUIChannelApi>,
  []
>(() => ({
  requests: new MockedApiRequests(),
  notifications: new MockedApiNotifications(),
  subscribe: jest.fn(),
  unsubscribe: jest.fn(),
  shared: jest.fn()
}));

export const MockedEnvelopeClient = jest.fn<
  EnvelopeClient<RuntimeToolsDevUIEnvelopeApi, RuntimeToolsDevUIChannelApi>,
  []
>(() => ({
  bus: jest.fn(),
  manager: jest.fn(),
  associate: jest.fn(),
  channelApi: new MockedMessageBusClientApi(),
  startListening: jest.fn(),
  stopListening: jest.fn(),
  send: jest.fn(),
  receive: jest.fn()
}));

export const MockedRuntimeToolsDevUIEnvelopeViewApi = jest.fn<
  RuntimeToolsDevUIEnvelopeViewApi,
  []
>(() => ({
  setProcessEnabled: jest.fn(),
  setTracingEnabled: jest.fn(),
  setDataIndexUrl: jest.fn(),
  setTrustyServiceUrl: jest.fn(),
  setUsers: jest.fn(),
  navigateTo: jest.fn()
}));
