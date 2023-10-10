import {
  ApiNotificationConsumers,
  MessageBusClientApi,
  RequestPropertyNames
} from '@kie-tools-core/envelope-bus/dist/api';
import {
  ProcessDefinitionListChannelApi,
  ProcessDefinitionListEnvelopeApi
} from '../../../api';
import { EnvelopeClient } from '@kie-tools-core/envelope-bus/dist/envelope';
import { ProcessDefinitionListEnvelopeViewApi } from '../../ProcessDefinitionListEnvelopeView';

export const MockedApiRequests = jest.fn<
  Pick<
    ProcessDefinitionListChannelApi,
    RequestPropertyNames<ProcessDefinitionListChannelApi>
  >,
  []
>(() => ({
  processDefinitionList__getProcessDefinitionsQuery: jest.fn(),
  processDefinitionList__openProcessForm: jest.fn(),
  processDefinitionList__setProcessDefinitionFilter: jest.fn(),
  processDefinitionList__getProcessDefinitionFilter: jest.fn()
}));

const mockNotificationConsumer = {
  subscribe: jest.fn(),
  unsubscribe: jest.fn(),
  send: jest.fn()
};

export const MockedApiNotifications = jest.fn<
  ApiNotificationConsumers<ProcessDefinitionListChannelApi>,
  []
>(() => ({
  processDefinitionsList__openTriggerCloudEvent: mockNotificationConsumer
}));

export const MockedMessageBusClientApi = jest.fn<
  MessageBusClientApi<ProcessDefinitionListChannelApi>,
  []
>(() => ({
  requests: new MockedApiRequests(),
  notifications: new MockedApiNotifications(),
  subscribe: jest.fn(),
  unsubscribe: jest.fn(),
  shared: jest.fn()
}));

export const MockedEnvelopeClient = jest.fn<
  EnvelopeClient<
    ProcessDefinitionListEnvelopeApi,
    ProcessDefinitionListChannelApi
  >,
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

export const MockedProcessDefinitionListEnvelopeViewApi = jest.fn<
  ProcessDefinitionListEnvelopeViewApi,
  []
>(() => ({
  initialize: jest.fn()
}));
