import {
  MessageBusClientApi,
  RequestPropertyNames
} from '@kie-tools-core/envelope-bus/dist/api';
import { FormDetailsChannelApi, FormDetailsEnvelopeApi } from '../../../api';
import { MessageBusServer } from '@kie-tools-core/envelope-bus/dist/api';
import { EnvelopeBusMessageManager } from '@kie-tools-core/envelope-bus/dist/common';
import { EnvelopeClient } from '@kie-tools-core/envelope-bus/dist/envelope';
import { FormDetailsEnvelopeViewApi } from '../../FormDetailsEnvelopeView';

export const MockedApiRequests = jest.fn<
  Pick<FormDetailsChannelApi, RequestPropertyNames<FormDetailsChannelApi>>,
  []
>(() => ({
  formDetails__getFormContent: jest.fn(),
  formDetails__saveFormContent: jest.fn()
}));

export const MockedMessageBusClientApi = jest.fn<
  MessageBusClientApi<FormDetailsChannelApi>,
  []
>(() => ({
  requests: new MockedApiRequests(),
  notifications: jest.fn(),
  subscribe: jest.fn(),
  unsubscribe: jest.fn(),
  shared: jest.fn()
}));

export const MockedMessageBusServer = jest.fn<
  MessageBusServer<FormDetailsEnvelopeApi, FormDetailsChannelApi>,
  []
>(() => ({
  receive: jest.fn()
}));

export const MockedEnvelopeBusMessageManager = jest.fn<
  Partial<
    EnvelopeBusMessageManager<FormDetailsEnvelopeApi, FormDetailsChannelApi>
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
  Partial<EnvelopeClient<FormDetailsEnvelopeApi, FormDetailsChannelApi>>,
  []
>(() => ({
  bus: jest.fn(),
  manager: new MockedEnvelopeBusMessageManager() as EnvelopeBusMessageManager<
    FormDetailsEnvelopeApi,
    FormDetailsChannelApi
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
    FormDetailsEnvelopeApi,
    FormDetailsChannelApi
  >;

export const MockedFormDetailsEnvelopeViewApi = jest.fn<
  FormDetailsEnvelopeViewApi,
  []
>(() => ({
  initialize: jest.fn()
}));
