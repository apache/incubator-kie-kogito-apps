/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import {
  MessageBusClientApi,
  NotificationPropertyNames,
  RequestPropertyNames
} from '@kogito-tooling/envelope-bus/dist/api';
import {
  ProcessDetailsChannelApi,
  ProcessDetailsEnvelopeApi
} from '../../../api';
import {
  ProcessInstanceState,
  MilestoneStatus
} from '@kogito-apps/management-console-shared';
import { EnvelopeBusController } from '@kogito-tooling/envelope-bus/dist/envelope';
import { ProcessDetailsEnvelopeViewApi } from '../../ProcessDetailsEnvelopeView';

export const ProcessDetails = {
  id: 'a1e139d5-4e77-48c9-84ae-34578e904e5a',
  processId: 'hotelBooking',
  processName: 'HotelBooking',
  businessKey: 'T1234HotelBooking01',
  parentProcessInstanceId: 'e4448857-fa0c-403b-ad69-f0a353458b9d',
  parentProcessInstance: {
    id: 'e4448857-fa0c-403b-ad69-f0a353458b9d',
    processName: 'travels',
    businessKey: 'T1234'
  },
  roles: [],
  variables:
    '{"trip":{"begin":"2019-10-22T22:00:00Z[UTC]","city":"Bangalore","country":"India","end":"2019-10-30T22:00:00Z[UTC]","visaRequired":false},"hotel":{"address":{"city":"Bangalore","country":"India","street":"street","zipCode":"12345"},"bookingNumber":"XX-012345","name":"Perfect hotel","phone":"09876543"},"traveller":{"address":{"city":"Bangalore","country":"US","street":"Bangalore","zipCode":"560093"},"email":"ajaganat@redhat.com","firstName":"Ajay","lastName":"Jaganathan","nationality":"US"}}',
  state: ProcessInstanceState.Completed,
  start: new Date('2019-10-22T03:40:44.089Z'),
  lastUpdate: new Date('Thu, 22 Apr 2021 14:53:04 GMT'),
  end: new Date('2019-10-22T05:40:44.089Z'),
  addons: [],
  endpoint: 'http://localhost:4000',
  serviceUrl: null,
  error: {
    nodeDefinitionId: 'a1e139d5-4e77-48c9-84ae-34578e904e6b',
    message: 'some thing went wrong',
    __typename: 'ProcessInstanceError'
  },
  childProcessInstances: [],
  nodes: [
    {
      id: '27107f38-d888-4edf-9a4f-11b9e6d751b6',
      nodeId: '1',
      name: 'End Event 1',
      enter: new Date('2019-10-22T03:37:30.798Z'),
      exit: new Date('2019-10-22T03:37:30.798Z'),
      type: 'EndNode',
      definitionId: 'EndEvent_1',
      __typename: 'NodeInstance'
    },
    {
      id: '41b3f49e-beb3-4b5f-8130-efd28f82b971',
      nodeId: '2',
      name: 'Book hotel',
      enter: new Date('2019-10-22T03:37:30.795Z'),
      exit: new Date('2019-10-22T03:37:30.798Z'),
      type: 'WorkItemNode',
      definitionId: 'ServiceTask_1',
      __typename: 'NodeInstance'
    },
    {
      id: '4165a571-2c79-4fd0-921e-c6d5e7851b67',
      nodeId: '2',
      name: 'StartProcess',
      enter: new Date('2019-10-22T03:37:30.793Z'),
      exit: new Date('2019-10-22T03:37:30.795Z'),
      type: 'StartNode',
      definitionId: 'StartEvent_1',
      __typename: 'NodeInstance'
    }
  ],
  milestones: [
    {
      id: '27107f38-d888-4edf-9a4f-11b9e6d75i86',
      name: 'Manager decision',
      status: MilestoneStatus.Completed,
      __typename: 'Milestone'
    },
    {
      id: '27107f38-d888-4edf-9a4f-11b9e6d75m36',
      name: 'Milestone 1: Order placed',
      status: MilestoneStatus.Active,
      __typename: 'Milestone'
    },
    {
      id: '27107f38-d888-4edf-9a4f-11b9e6d75m66',
      name: 'Milestone 2: Order shipped',
      status: MilestoneStatus.Available,
      __typename: 'Milestone'
    }
  ]
};

export const MockedApiRequests = jest.fn<
  Pick<
    ProcessDetailsChannelApi,
    RequestPropertyNames<ProcessDetailsChannelApi>
  >,
  []
>(() => ({
  processDetails__initialLoad: jest.fn(),
  processDetails__processDetailsQuery: jest.fn(),
  processDetails__getProcessDiagram: jest.fn(),
  processDetails__cancelJob: jest.fn(),
  processDetails__rescheduleJob: jest.fn(),
  processDetails__jobsQuery: jest.fn()
}));

export const MockedApiNotifications = jest.fn<
  Pick<
    ProcessDetailsChannelApi,
    NotificationPropertyNames<ProcessDetailsChannelApi>
  >,
  []
>(() => ({
  jobList__sortBy: jest.fn()
}));

export const MockedMessageBusClientApi = jest.fn<
  MessageBusClientApi<ProcessDetailsChannelApi>,
  []
>(() => ({
  requests: new MockedApiRequests(),
  notifications: new MockedApiNotifications(),
  subscribe: jest.fn(),
  unsubscribe: jest.fn()
}));

export const MockedEnvelopeBusController = jest.fn<
  EnvelopeBusController<ProcessDetailsEnvelopeApi, ProcessDetailsChannelApi>,
  []
>(() => ({
  bus: jest.fn(),
  // @ts-ignore
  manager: jest.fn(),
  associate: jest.fn(),
  channelApi: new MockedMessageBusClientApi(),
  startListening: jest.fn(),
  stopListening: jest.fn(),
  send: jest.fn(),
  receive: jest.fn()
}));

export const MockedProcessDetailsEnvelopeViewApi = jest.fn<
  ProcessDetailsEnvelopeViewApi,
  []
>(() => ({
  initialize: jest.fn()
}));
