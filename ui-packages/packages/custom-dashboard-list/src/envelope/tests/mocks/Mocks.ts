import {
  MessageBusClientApi,
  RequestPropertyNames
} from '@kie-tools-core/envelope-bus/dist/api';
import {
  CustomDashboardListChannelApi,
  CustomDashboardListEnvelopeApi
} from '../../../api';
import { MessageBusServer } from '@kie-tools-core/envelope-bus/dist/api';
import { EnvelopeBusMessageManager } from '@kie-tools-core/envelope-bus/dist/common';
import { EnvelopeClient } from '@kie-tools-core/envelope-bus/dist/envelope';
import { CustomDashboardListEnvelopeViewApi } from '../../CustomDashboardListEnvelopeView';

export const MockedApiRequests = jest.fn<
  Pick<
    CustomDashboardListChannelApi,
    RequestPropertyNames<CustomDashboardListChannelApi>
  >,
  []
>(() => ({
  customDashboardList__getFilter: jest.fn(),
  customDashboardList__applyFilter: jest.fn(),
  customDashboardList__getCustomDashboardQuery: jest.fn(),
  customDashboardList__openDashboard: jest.fn()
}));

export const MockedMessageBusClientApi = jest.fn<
  MessageBusClientApi<CustomDashboardListChannelApi>,
  []
>(() => ({
  requests: new MockedApiRequests(),
  notifications: jest.fn(),
  subscribe: jest.fn(),
  unsubscribe: jest.fn(),
  shared: jest.fn()
}));

export const MockedMessageBusServer = jest.fn<
  MessageBusServer<
    CustomDashboardListEnvelopeApi,
    CustomDashboardListChannelApi
  >,
  []
>(() => ({
  receive: jest.fn()
}));

export const MockedEnvelopeBusMessageManager = jest.fn<
  Partial<
    EnvelopeBusMessageManager<
      CustomDashboardListEnvelopeApi,
      CustomDashboardListChannelApi
    >
  >,
  []
>(() => ({
  callbacks: jest.fn(),
  remoteSubscriptions: jest.fn(),
  localSubscriptions: jest.fn(),
  send: jest.fn(),
  name: jest.fn(),
  requestIdCounter: jest.fn(),
  clientApi: new MockedMessageBusClientApi(),
  server: new MockedMessageBusServer(),
  requests: jest.fn(),
  notifications: jest.fn(),
  subscribe: jest.fn(),
  unsubscribe: jest.fn(),
  request: jest.fn(),
  notify: jest.fn(),
  respond: jest.fn(),
  callback: jest.fn(),
  receive: jest.fn(),
  getNextRequestId: jest.fn()
}));

export const MockedEnvelopeClientDefinition = jest.fn<
  Partial<
    EnvelopeClient<
      CustomDashboardListEnvelopeApi,
      CustomDashboardListChannelApi
    >
  >,
  []
>(() => ({
  bus: jest.fn(),
  manager: new MockedEnvelopeBusMessageManager() as EnvelopeBusMessageManager<
    CustomDashboardListEnvelopeApi,
    CustomDashboardListChannelApi
  >,
  associate: jest.fn(),
  channelApi: new MockedMessageBusClientApi(),
  startListening: jest.fn(),
  stopListening: jest.fn(),
  send: jest.fn(),
  receive: jest.fn()
}));

export const MockedEnvelopeClient =
  new MockedEnvelopeClientDefinition() as EnvelopeClient<
    CustomDashboardListEnvelopeApi,
    CustomDashboardListChannelApi
  >;

export const MockedCustomDashboardListEnvelopeViewApi = jest.fn<
  CustomDashboardListEnvelopeViewApi,
  []
>(() => ({
  initialize: jest.fn()
}));
