/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
import React from 'react';
import * as H from 'history';
import { MemoryRouter } from 'react-router';
import { act } from 'react-dom/test-utils';
import wait from 'waait';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { UserTaskInstance } from '@kogito-apps/task-console-shared';
import { TaskInboxGatewayApi } from '../../../../channel/TaskInbox';
import * as TaskInboxContext from '../../../../channel/TaskInbox/TaskInboxContext';
import TaskDetailsPage from '../TaskDetailsPage';
import DevUIAppContextProvider from '../../../contexts/DevUIAppContextProvider';
import {
  DefaultUser,
  User
} from '@kogito-apps/consoles-common/dist/environment/auth';

jest.mock('../../../containers/TaskFormContainer/TaskFormContainer');

const userTask: UserTaskInstance = {
  id: '45a73767-5da3-49bf-9c40-d533c3e77ef3',
  description: null,
  name: 'VisaApplication',
  referenceName: 'Apply for visa',
  priority: '1',
  processInstanceId: '9ae7ce3b-d49c-4f35-b843-8ac3d22fa427',
  processId: 'travels',
  rootProcessInstanceId: null,
  rootProcessId: null,
  state: 'Ready',
  actualOwner: null,
  adminGroups: [],
  adminUsers: [],
  completed: null,
  started: new Date('2020-02-19T11:11:56.282Z'),
  excludedUsers: [],
  potentialGroups: [],
  potentialUsers: [],
  inputs:
    '{"Skippable":"true","trip":{"city":"Boston","country":"US","begin":"2020-02-19T23:00:00.000+01:00","end":"2020-02-26T23:00:00.000+01:00","visaRequired":true},"TaskName":"VisaApplication","NodeName":"Apply for visa","traveller":{"firstName":"Rachel","lastName":"White","email":"rwhite@gorle.com","nationality":"Polish","address":{"street":"Cabalone","city":"Zerf","zipCode":"765756","country":"Poland"}},"Priority":"1"}',
  outputs: '{}',
  lastUpdate: new Date('2020-02-19T11:11:56.282Z'),
  endpoint:
    'http://localhost:8080/travels/9ae7ce3b-d49c-4f35-b843-8ac3d22fa427/VisaApplication/45a73767-5da3-49bf-9c40-d533c3e77ef3'
};

const props = {
  match: {
    params: {
      taskId: '45a73767-5da3-49bf-9c40-d533c3e77ef3'
    },
    url: '',
    isExact: false,
    path: ''
  },
  location: {
    hash: '',
    pathname: '/',
    search: '',
    state: undefined
  },
  history: H.createBrowserHistory(),
  ouiaId: ''
};

const pushSpy = jest.spyOn(props.history, 'push');
pushSpy.mockImplementation(() => {
  // do nothing
});

const getUserTaskByIdMock = jest.fn();

const MockTaskInboxGatewayApi = jest.fn<TaskInboxGatewayApi, []>(() => ({
  setInitialState: jest.fn(),
  applyFilter: jest.fn(),
  applySorting: jest.fn(),
  query: jest.fn(),
  getTaskById: getUserTaskByIdMock,
  openTask: jest.fn(),
  clearOpenTask: jest.fn(),
  onOpenTaskListen: jest.fn(),
  taskInboxState: undefined
}));

jest
  .spyOn(TaskInboxContext, 'useTaskInboxGatewayApi')
  .mockImplementation(() => gatewayApi);

let gatewayApi: TaskInboxGatewayApi;

const user: User = new DefaultUser('jon', []);
const appContextProps = {
  devUIUrl: 'http://localhost:9000',
  openApiPath: '/mocked',
  isProcessEnabled: false,
  isTracingEnabled: false,
  omittedProcessTimelineEvents: [],
  availablePages: [],
  customLabels: {
    singularProcessLabel: 'test-singular',
    pluralProcessLabel: 'test-plural'
  },
  diagramPreviewSize: { width: 100, height: 100 }
};

const getTaskDetailsPageWrapper = async () => {
  let container;
  await act(async () => {
    container = render(
      <MemoryRouter keyLength={0} initialEntries={['/']}>
        <DevUIAppContextProvider users={[user]} {...appContextProps}>
          <TaskDetailsPage {...props} />
        </DevUIAppContextProvider>
      </MemoryRouter>
    ).container;
    wait();
  });

  return container;
};

describe('TaskDetailsPage tests', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    gatewayApi = new MockTaskInboxGatewayApi();
  });

  it('Empty state', async () => {
    const container = await getTaskDetailsPageWrapper();

    expect(container).toMatchSnapshot();

    const checkEmptyState = container.querySelector(
      'div[data-ouia-component-type="kogito-empty-state"]'
    );
    expect(checkEmptyState).toBeTruthy();

    const checkEmptyStateDescription = container.querySelector(
      'div[class="pf-c-empty-state__body"]'
    ).textContent;
    expect(checkEmptyStateDescription).toEqual(
      "Cannot find task with id '45a73767-5da3-49bf-9c40-d533c3e77ef3'"
    );
    expect(container.querySelector('h5').textContent).toEqual(
      'Cannot find task'
    );
  });

  it('Error state', async () => {
    getUserTaskByIdMock.mockImplementation(() => {
      throw new Error('Error: Something went wrong on server!');
    });

    const container = await getTaskDetailsPageWrapper();

    expect(container).toMatchSnapshot();

    const checkServerErrorsComponent = container.querySelector(
      'div[data-ouia-component-type="server-errors"]'
    );

    expect(checkServerErrorsComponent).toBeTruthy();

    const checkTitle = container.querySelector('h1').textContent;
    expect(checkTitle).toEqual('Error fetching data');

    const checkBody = screen.getByTestId('empty-state-body').textContent;
    expect(checkBody).toEqual(
      'An error occurred while accessing data. See more details'
    );
  });

  it('Normal State', async () => {
    getUserTaskByIdMock.mockReturnValue(userTask);

    const container = await getTaskDetailsPageWrapper();
    await waitFor(() => {
      // expect(container).toMatchSnapshot();
    });
    const viewDetailsButton = screen.getByText('View details');
    expect(viewDetailsButton).toBeTruthy();
  });

  it('Success notification', async () => {
    getUserTaskByIdMock.mockReturnValue(userTask);

    const container = await getTaskDetailsPageWrapper();

    const checkTaskHeading = screen.getByText('Apply for visa');
    expect(checkTaskHeading).toBeTruthy();

    const viewDetailsButton = screen.getByText('View details');
    fireEvent.click(viewDetailsButton);

    const sucessAlert = screen.getByLabelText('Success Alert');
    expect(sucessAlert).toBeTruthy();

    const AlertMessage = container.querySelector('h4').textContent;
    expect(AlertMessage).toEqual(
      `Success alert:Task 'Apply for visa' successfully transitioned to phase 'complete'.`
    );

    const button = screen.getByTestId('close-button');
    fireEvent.click(button);

    expect(() => screen.getByLabelText('Success Alert')).toThrow(
      'Unable to find a label with the text of: Success Alert'
    );
  });

  it('Success notification - go to inbox link', async () => {
    getUserTaskByIdMock.mockReturnValue(userTask);

    const container = await getTaskDetailsPageWrapper();

    const taskName = container.querySelector('h1').textContent;
    expect(taskName).toEqual('Apply for visa');

    const successAlert = container.querySelector('h4').textContent;
    expect(successAlert).toEqual(
      `Success alert:Task 'Apply for visa' successfully transitioned to phase 'complete'.`
    );

    const goToInboxButton = screen.getByText('Go to Task Inbox');
    expect(goToInboxButton).toBeTruthy();

    fireEvent.click(goToInboxButton);

    expect(pushSpy).toBeCalledWith('/TaskInbox');
  });

  it('Task details Drawer', async () => {
    getUserTaskByIdMock.mockReturnValue(userTask);

    const container = await getTaskDetailsPageWrapper();

    const detailsButton = screen.getByText('View details');
    fireEvent.click(detailsButton);

    const detailsTab = container.querySelector('h3').textContent;

    expect(detailsTab).toEqual('Details');

    const taskName = container.querySelector('h1').textContent;
    expect(taskName).toEqual('Apply for visa');
  });
});
