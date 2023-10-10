import {
  MessageBusClientApi,
  RequestPropertyNames
} from '@kie-tools-core/envelope-bus/dist/api';
import { FormsListChannelApi, FormsListEnvelopeApi } from '../../../api';
import { MessageBusServer } from '@kie-tools-core/envelope-bus/dist/api';
import { EnvelopeBusMessageManager } from '@kie-tools-core/envelope-bus/dist/common';
import { EnvelopeClient } from '@kie-tools-core/envelope-bus/dist/envelope';
import { FormsListEnvelopeViewApi } from '../../FormsListEnvelopeView';

export const MockedApiRequests = jest.fn<
  Pick<FormsListChannelApi, RequestPropertyNames<FormsListChannelApi>>,
  []
>(() => ({
  formsList__getFormFilter: jest.fn(),
  formsList__applyFilter: jest.fn(),
  formsList__openForm: jest.fn(),
  formsList__getFormsQuery: jest.fn()
}));

export const MockedMessageBusClientApi = jest.fn<
  MessageBusClientApi<FormsListChannelApi>,
  []
>(() => ({
  requests: new MockedApiRequests(),
  notifications: jest.fn(),
  subscribe: jest.fn(),
  unsubscribe: jest.fn(),
  shared: jest.fn()
}));

export const MockedMessageBusServer = jest.fn<
  MessageBusServer<FormsListEnvelopeApi, FormsListChannelApi>,
  []
>(() => ({
  receive: jest.fn()
}));

export const MockedEnvelopeBusMessageManager = jest.fn<
  Partial<EnvelopeBusMessageManager<FormsListEnvelopeApi, FormsListChannelApi>>,
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
  Partial<EnvelopeClient<FormsListEnvelopeApi, FormsListChannelApi>>,
  []
>(() => ({
  bus: jest.fn(),
  manager: new MockedEnvelopeBusMessageManager() as EnvelopeBusMessageManager<
    FormsListEnvelopeApi,
    FormsListChannelApi
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
    FormsListEnvelopeApi,
    FormsListChannelApi
  >;

export const MockedFormsListEnvelopeViewApi = jest.fn<
  FormsListEnvelopeViewApi,
  []
>(() => ({
  initialize: jest.fn()
}));
