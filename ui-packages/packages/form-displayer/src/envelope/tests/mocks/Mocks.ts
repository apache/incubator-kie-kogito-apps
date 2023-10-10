import {
  MessageBusClientApi,
  RequestPropertyNames,
  NotificationPropertyNames
} from '@kie-tools-core/envelope-bus/dist/api';
import {
  FormDisplayerChannelApi,
  FormDisplayerEnvelopeApi
} from '../../../api';
import { MessageBusServer } from '@kie-tools-core/envelope-bus/dist/api';
import { EnvelopeBusMessageManager } from '@kie-tools-core/envelope-bus/dist/common';
import { EnvelopeClient } from '@kie-tools-core/envelope-bus/dist/envelope';
import { FormDisplayerEnvelopeViewApi } from '../../FormDisplayerEnvelopeView';
import { EnvelopeApiFactoryArgs } from '@kie-tools-core/envelope/dist/EnvelopeApiFactory';

export const MockedApiRequests = jest.fn<
  Pick<FormDisplayerChannelApi, RequestPropertyNames<FormDisplayerChannelApi>>,
  []
>(() => ({}));

export const MockedApiNotifications = jest.fn<any, []>(() => ({
  notifyOnOpenForm: jest.fn()
}));

export const MockedMessageBusClientApi = jest.fn<
  MessageBusClientApi<FormDisplayerChannelApi>,
  []
>(() => ({
  requests: new MockedApiRequests(),
  notifications: new MockedApiNotifications(),
  subscribe: jest.fn(),
  unsubscribe: jest.fn(),
  shared: jest.fn()
}));

export const MockedMessageBusServer = jest.fn<
  MessageBusServer<FormDisplayerEnvelopeApi, FormDisplayerChannelApi>,
  []
>(() => ({
  receive: jest.fn()
}));

export const MockedEnvelopeBusMessageManager = jest.fn<
  Partial<
    EnvelopeBusMessageManager<FormDisplayerEnvelopeApi, FormDisplayerChannelApi>
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
  Partial<EnvelopeClient<FormDisplayerEnvelopeApi, FormDisplayerChannelApi>>,
  []
>(() => ({
  bus: jest.fn(),
  manager: new MockedEnvelopeBusMessageManager() as EnvelopeBusMessageManager<
    FormDisplayerEnvelopeApi,
    FormDisplayerChannelApi
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
    FormDisplayerEnvelopeApi,
    FormDisplayerChannelApi
  >;

export const MockedFormDisplayerEnvelopeViewApi = jest.fn<
  FormDisplayerEnvelopeViewApi,
  []
>(() => ({
  initForm: jest.fn(),
  startSubmit: jest.fn(),
  notifySubmitResponse: jest.fn()
}));
