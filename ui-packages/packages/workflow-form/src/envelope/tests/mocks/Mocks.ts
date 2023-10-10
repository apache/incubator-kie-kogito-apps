import {
  ApiNotificationConsumers,
  MessageBusClientApi,
  NotificationPropertyNames,
  RequestPropertyNames
} from '@kie-tools-core/envelope-bus/dist/api';
import { EnvelopeClient } from '@kie-tools-core/envelope-bus/dist/envelope';
import { WorkflowFormChannelApi, WorkflowFormEnvelopeApi } from '../../../api';
import { WorkflowFormEnvelopeViewApi } from '../../WorkflowFormEnvelopeView';

export const workflowForm__resetBusinessKey = jest.fn();
export const workflowForm__getCustomWorkflowSchema = jest.fn();
export const workflowForm__startWorkflow = jest.fn();

export const workflowSchema = {
  title: 'Expression',
  description: 'Schema for expression test',
  type: 'object',
  properties: {
    numbers: {
      description: 'The array of numbers to be operated with',
      type: 'array',
      items: {
        type: 'object',
        properties: {
          x: { type: 'number' },
          y: { type: 'number' }
        }
      }
    }
  },
  required: ['numbers']
};
export const MockedApiRequests = jest.fn<
  Pick<WorkflowFormChannelApi, RequestPropertyNames<WorkflowFormChannelApi>>,
  []
>(() => ({
  workflowForm__resetBusinessKey: workflowForm__resetBusinessKey,
  workflowForm__getCustomWorkflowSchema: workflowForm__getCustomWorkflowSchema,
  workflowForm__startWorkflow: workflowForm__startWorkflow
}));

export const MockedApiNotifications = jest.fn<
  ApiNotificationConsumers<WorkflowFormChannelApi>,
  []
>(() => ({}));

export const MockedMessageBusClientApi = jest.fn<
  MessageBusClientApi<WorkflowFormChannelApi>,
  []
>(() => ({
  requests: new MockedApiRequests(),
  notifications: new MockedApiNotifications(),
  subscribe: jest.fn(),
  unsubscribe: jest.fn(),
  shared: jest.fn()
}));

export const MockedEnvelopeClient = jest.fn<
  EnvelopeClient<WorkflowFormEnvelopeApi, WorkflowFormChannelApi>,
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

export const MockedWorkflowFormEnvelopeViewApi = jest.fn<
  WorkflowFormEnvelopeViewApi,
  []
>(() => ({
  initialize: jest.fn()
}));
