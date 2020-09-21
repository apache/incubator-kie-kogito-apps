/**
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import React from 'react';
import * as H from 'history';
import ProcessDetailsPage from '../ProcessDetailsPage';
import { MockedProvider } from '@apollo/react-testing';
import { BrowserRouter } from 'react-router-dom';
import { getWrapperAsync, GraphQL } from '@kogito-apps/common';
import GetProcessInstanceByIdDocument = GraphQL.GetProcessInstanceByIdDocument;
import ProcessInstanceState = GraphQL.ProcessInstanceState;
import MilestoneStatus = GraphQL.MilestoneStatus;
import { Button } from '@patternfly/react-core';
import axios from 'axios';
jest.mock('axios');
import * as Utils from '../../../../utils/Utils';
import { act } from 'react-dom/test-utils';
// tslint:disable: no-string-literal
const mockedAxios = axios as jest.Mocked<typeof axios>;
jest.mock('../../../Atoms/ProcessListModal/ProcessListModal');
jest.mock('../../../Atoms/ProcessListBulkInstances/ProcessListBulkInstances');
jest.mock('../../../Organisms/ProcessDetails/ProcessDetails');
jest.mock(
  '../../../Organisms/ProcessDetailsProcessDiagram/ProcessDetailsProcessDiagram'
);
jest.mock(
  '../../../Organisms/ProcessDetailsMilestones/ProcessDetailsMilestones'
);
jest.mock(
  '../../../Organisms/ProcessDetailsProcessVariables/ProcessDetailsProcessVariables'
);
jest.mock('../../../Organisms/ProcessDetailsTimeline/ProcessDetailsTimeline');

const MockedComponent = (): React.ReactElement => {
  return <></>;
};

jest.mock('@patternfly/react-icons', () => ({
  ...jest.requireActual('@patternfly/react-icons'),
  OnRunningIcon: () => {
    return <MockedComponent />;
  },
  CheckCircleIcon: () => {
    return <MockedComponent />;
  },
  BanIcon: () => {
    return <MockedComponent />;
  },
  PausedIcon: () => {
    return <MockedComponent />;
  },
  ErrorCircleOIcon: () => {
    return <MockedComponent />;
  },
  AngleRightIcon: () => {
    return <MockedComponent />;
  }
}));

jest.mock('@kogito-apps/common', () => ({
  ...jest.requireActual('@kogito-apps/common'),
  ItemDescriptor: () => {
    return <MockedComponent />;
  },
  KogitoSpinner: () => {
    return <MockedComponent />;
  },
  ServerErrors: () => {
    return <MockedComponent />;
  }
}));

const props = {
  match: {
    params: {
      instanceID: '8035b580-6ae4-4aa8-9ec0-e18e19809e0b'
    },
    url: '',
    isExact: false,
    path: ''
  },
  location: H.createLocation(''),
  history: H.createBrowserHistory()
};
props.location.state = {
  filters: {
    status: [ProcessInstanceState.Active],
    businessKey: []
  }
};
const props1 = {
  match: {
    params: {
      instanceID: '8035b580-6ae4-4aa8-9ec0-e18e19809e0bc'
    },
    url: '',
    isExact: false,
    path: ''
  },
  location: H.createLocation(''),
  history: H.createBrowserHistory()
};
props.location.state = {
  filters: {
    status: [ProcessInstanceState.Active],
    businessKey: ['tra']
  }
};
const mocks1 = [
  {
    request: {
      query: GetProcessInstanceByIdDocument,
      variables: {
        id: '8035b580-6ae4-4aa8-9ec0-e18e19809e0b'
      },
      fetchPolicy: 'network-only'
    },
    result: {
      loading: false,
      data: {
        ProcessInstances: [
          {
            id: '8035b580-6ae4-4aa8-9ec0-e18e19809e0b',
            processId: 'Travels',
            processName: 'travels',
            businessKey: null,
            parentProcessInstanceId: null,
            parentProcessInstance: null,
            roles: [],
            variables:
              '{"flight":{"arrival":"2019-10-30T22:00:00Z[UTC]","departure":"2019-10-22T22:00:00Z[UTC]","flightNumber":"MX555"},"hotel":{"address":{"city":"Berlin","country":"Germany","street":"street","zipCode":"12345"},"bookingNumber":"XX-012345","name":"Perfect hotel","phone":"09876543"},"trip":{"begin":"2019-10-22T22:00:00Z[UTC]","city":"Berlin","country":"Germany","end":"2019-10-30T22:00:00Z[UTC]","visaRequired":false},"traveller":{"address":{"city":"Karkow","country":"Poland","street":"palna","zipCode":"200300"},"email":"rob@redhat.com","firstName":"Rob","lastName":"Rob","nationality":"Polish"}}',
            state: ProcessInstanceState.Active,
            start: '2019-10-22T03:40:44.089Z',
            lastUpdate: '2019-10-22T03:40:44.089Z',
            end: null,
            endpoint: 'http://localhost:4000',
            addons: ['process-management'],
            serviceUrl: 'http://localhost:4000',
            error: {
              nodeDefinitionId: 'a1e139d5-4e77-48c9-84ae-3459188e90433n',
              message: 'Something went wrong'
            },
            childProcessInstances: [],
            nodes: [
              {
                id: '90e5a337-1c26-4fcc-8ee2-d20e6ba2a1a3',
                nodeId: '9',
                name: 'StartProcess',
                enter: '2019-10-22T04:43:01.135Z',
                exit: '2019-10-22T04:43:01.135Z',
                type: 'StartNode',
                definitionId: 'StartEvent_1'
              }
            ],
            milestones: [
              {
                id: '27107f38-d888-4edf-9a4f-11b9e6d75m36',
                name: 'Milestone 1: Order placed',
                status: MilestoneStatus['Active'],
                __typename: 'Milestones'
              },
              {
                id: '27107f38-d888-4edf-9a4f-11b9e6d75m66',
                name: 'Milestone 2: Order shipped',
                status: MilestoneStatus['Available'],
                __typename: 'Milestones'
              },
              {
                id: '27107f38-d888-4edf-9a4f-11b9e6d75i86',
                name: 'Manager decision',
                status: MilestoneStatus['Completed'],
                __typename: 'Milestones'
              }
            ]
          }
        ]
      }
    }
  }
];

const mocks2 = [
  {
    request: {
      query: GetProcessInstanceByIdDocument,
      variables: {
        id: '8035b580-6ae4-4aa8-9ec0-e18e19809e0b'
      },
      fetchPolicy: 'network-only'
    },
    result: {
      data: {
        ProcessInstances: [
          {
            id: '8035b580-6ae4-4aa8-9ec0-e18e19809e0b',
            processId: 'Travels',
            processName: 'travels',
            businessKey: null,
            parentProcessInstanceId: null,
            parentProcessInstance: null,
            roles: [],
            variables:
              '{"flight":{"arrival":"2019-10-30T22:00:00Z[UTC]","departure":"2019-10-22T22:00:00Z[UTC]","flightNumber":"MX555"},"hotel":{"address":{"city":"Berlin","country":"Germany","street":"street","zipCode":"12345"},"bookingNumber":"XX-012345","name":"Perfect hotel","phone":"09876543"},"trip":{"begin":"2019-10-22T22:00:00Z[UTC]","city":"Berlin","country":"Germany","end":"2019-10-30T22:00:00Z[UTC]","visaRequired":false},"traveller":{"address":{"city":"Karkow","country":"Poland","street":"palna","zipCode":"200300"},"email":"rob@redhat.com","firstName":"Rob","lastName":"Rob","nationality":"Polish"}}',
            state: ProcessInstanceState.Error,
            start: '2019-10-22T03:40:44.089Z',
            lastUpdate: '2019-10-22T03:40:44.089Z',
            end: null,
            endpoint: 'http://localhost:4000',
            addons: [],
            serviceUrl: null,
            error: {
              nodeDefinitionId: 'a1e139d5-4e77-48c9-84ae-3459188e90433n',
              message: 'Something went wrong'
            },
            childProcessInstances: [],
            nodes: [
              {
                id: '90e5a337-1c26-4fcc-8ee2-d20e6ba2a1a3',
                nodeId: '9',
                name: 'StartProcess',
                enter: '2019-10-22T04:43:01.135Z',
                exit: '2019-10-22T04:43:01.135Z',
                type: 'StartNode',
                definitionId: 'StartEvent_1'
              }
            ],
            milestones: [
              {
                id: '27107f38-d888-4edf-9a4f-11b9e6d75m36',
                name: 'Milestone 1: Order placed',
                status: MilestoneStatus['Active'],
                __typename: 'Milestones'
              },
              {
                id: '27107f38-d888-4edf-9a4f-11b9e6d75m66',
                name: 'Milestone 2: Order shipped',
                status: MilestoneStatus['Available'],
                __typename: 'Milestones'
              },
              {
                id: '27107f38-d888-4edf-9a4f-11b9e6d75i86',
                name: 'Manager decision',
                status: MilestoneStatus['Completed'],
                __typename: 'Milestones'
              }
            ]
          }
        ]
      }
    }
  }
];

const mocks3 = [
  {
    request: {
      query: GetProcessInstanceByIdDocument,
      variables: {
        id: '8035b580-6ae4-4aa8-9ec0-e18e19809e0b'
      },
      fetchPolicy: 'network-only'
    },
    result: {
      data: {
        ProcessInstances: [
          {
            id: '8035b580-6ae4-4aa8-9ec0-e18e19809e0b',
            processId: 'Travels',
            processName: 'travels',
            businessKey: null,
            parentProcessInstanceId: null,
            parentProcessInstance: null,
            roles: [],
            variables:
              '{"flight":{"arrival":"2019-10-30T22:00:00Z[UTC]","departure":"2019-10-22T22:00:00Z[UTC]","flightNumber":"MX555"},"hotel":{"address":{"city":"Berlin","country":"Germany","street":"street","zipCode":"12345"},"bookingNumber":"XX-012345","name":"Perfect hotel","phone":"09876543"},"trip":{"begin":"2019-10-22T22:00:00Z[UTC]","city":"Berlin","country":"Germany","end":"2019-10-30T22:00:00Z[UTC]","visaRequired":false},"traveller":{"address":{"city":"Karkow","country":"Poland","street":"palna","zipCode":"200300"},"email":"rob@redhat.com","firstName":"Rob","lastName":"Rob","nationality":"Polish"}}',
            state: ProcessInstanceState.Suspended,
            start: '2019-10-22T03:40:44.089Z',
            lastUpdate: '2019-10-22T03:40:44.089Z',
            end: null,
            endpoint: 'http://localhost:4000',
            addons: ['process-management'],
            serviceUrl: 'http://localhost:4000',
            error: {
              nodeDefinitionId: 'a1e139d5-4e77-48c9-84ae-3459188e90433n',
              message: 'Something went wrong'
            },
            childProcessInstances: [],
            nodes: [
              {
                id: '90e5a337-1c26-4fcc-8ee2-d20e6ba2a1a3',
                nodeId: '9',
                name: 'StartProcess',
                enter: '2019-10-22T04:43:01.135Z',
                exit: '2019-10-22T04:43:01.135Z',
                type: 'StartNode',
                definitionId: 'StartEvent_1'
              }
            ],
            milestones: [
              {
                id: '27107f38-d888-4edf-9a4f-11b9e6d75m36',
                name: 'Milestone 1: Order placed',
                status: MilestoneStatus['Active'],
                __typename: 'Milestones'
              },
              {
                id: '27107f38-d888-4edf-9a4f-11b9e6d75m66',
                name: 'Milestone 2: Order shipped',
                status: MilestoneStatus['Available'],
                __typename: 'Milestones'
              },
              {
                id: '27107f38-d888-4edf-9a4f-11b9e6d75i86',
                name: 'Manager decision',
                status: MilestoneStatus['Completed'],
                __typename: 'Milestones'
              }
            ]
          }
        ]
      }
    }
  }
];
/* tslint:disable */
describe('Process Details Page component tests', () => {
  let originalLocalStorage;
  beforeEach(() => {
    originalLocalStorage = Storage.prototype.getItem;
  });

  afterEach(() => {
    Storage.prototype.getItem = originalLocalStorage;
  });
  Date.now = jest.fn(() => 1487076708000);
  Storage.prototype.getItem = jest.fn(() =>
    JSON.stringify({
      prev: '/ProcessInstances/8035b580-6ae4-4aa8-9ec0-e18e19809e0b'
    })
  );
  it('snapshot testing in Active state', async () => {
    const wrapper = await getWrapperAsync(
      <MockedProvider mocks={mocks1} addTypename={false}>
        <BrowserRouter>
          <ProcessDetailsPage {...props} />
        </BrowserRouter>
      </MockedProvider>,
      'ProcessDetailsPage'
    );
    expect(wrapper).toMatchSnapshot();
  });
  describe('abort button click', () => {
    it('on successfull abort', async () => {
      mockedAxios.delete.mockResolvedValue({});
      const wrapper = await getWrapperAsync(
        <MockedProvider mocks={mocks1} addTypename={false}>
          <BrowserRouter>
            <ProcessDetailsPage {...props} />
          </BrowserRouter>
        </MockedProvider>,
        'ProcessDetailsPage'
      );
      const handleAbortSpy = jest.spyOn(Utils, 'handleAbort');
      await act(async () => {
        wrapper
          .find(Button)
          .find('#abort-button')
          .first()
          .simulate('click');
      });
      wrapper.update();
      expect(handleAbortSpy).toHaveBeenCalled();
    });
    it('on failed abort', async () => {
      mockedAxios.delete.mockRejectedValue({ message: '404 error' });
      const wrapper = await getWrapperAsync(
        <MockedProvider mocks={mocks1} addTypename={false}>
          <BrowserRouter>
            <ProcessDetailsPage {...props} />
          </BrowserRouter>
        </MockedProvider>,
        'ProcessDetailsPage'
      );
      const handleAbortSpy = jest.spyOn(Utils, 'handleAbort');
      await act(async () => {
        wrapper
          .find(Button)
          .find('#abort-button')
          .first()
          .simulate('click');
      });
      wrapper.update();
      expect(handleAbortSpy).toHaveBeenCalled();
    });
  });
  it('snapshot testing in Error state', async () => {
    const wrapper = await getWrapperAsync(
      <MockedProvider mocks={mocks2} addTypename={false}>
        <BrowserRouter>
          <ProcessDetailsPage {...props} />
        </BrowserRouter>
      </MockedProvider>,
      'ProcessDetailsPage'
    );
    expect(wrapper).toMatchSnapshot();
  });

  it('snapshot testing in Suspended state', async () => {
    const wrapper = await getWrapperAsync(
      <MockedProvider mocks={mocks3} addTypename={false}>
        <BrowserRouter>
          <ProcessDetailsPage {...props} />
        </BrowserRouter>
      </MockedProvider>,
      'ProcessDetailsPage'
    );
    expect(wrapper).toMatchSnapshot();
  });

  it('snapshot testing for error occurance', async () => {
    const wrapper = await getWrapperAsync(
      <MockedProvider mocks={mocks3} addTypename={false}>
        <BrowserRouter>
          <ProcessDetailsPage {...props1} />
        </BrowserRouter>
      </MockedProvider>,
      'ProcessDetailsPage'
    );
    expect(wrapper).toMatchSnapshot();
  });
  it('Test refresh and save button', async () => {
    mockedAxios.post.mockResolvedValue({});
    jest.setTimeout(2000);
    const { location } = window;
    delete window.location;
    // @ts-ignore
    window.location = { reload: jest.fn() };
    const wrapper = await getWrapperAsync(
      <MockedProvider mocks={mocks1} addTypename={false}>
        <BrowserRouter>
          <ProcessDetailsPage {...props} />
        </BrowserRouter>
      </MockedProvider>,
      'ProcessDetailsPage'
    );
    const handleVariableUpdateSpy = jest.spyOn(Utils, 'handleVariableUpdate');
    wrapper
      .find('#refresh-button')
      .first()
      .simulate('click');
    await act(async () => {
      wrapper
        .find('#save-button')
        .first()
        .simulate('click');
    });
    act(() => {
      wrapper
        .find('Modal')
        .at(0)
        .props()
        ['onClose']();
    });
    wrapper
      .find('Modal')
      .at(1)
      .props()
      ['onClose']();
    window.location = location;
    expect(handleVariableUpdateSpy).toHaveBeenCalled();
  });
  it('Test error axios response', async () => {
    mockedAxios.post.mockRejectedValue({ message: '404 error' });
    jest.setTimeout(2000);
    const { location } = window;
    delete window.location;
    // @ts-ignore
    window.location = { reload: jest.fn() };
    const wrapper = await getWrapperAsync(
      <MockedProvider mocks={mocks1} addTypename={false}>
        <BrowserRouter>
          <ProcessDetailsPage {...props} />
        </BrowserRouter>
      </MockedProvider>,
      'ProcessDetailsPage'
    );
    const handleVariableUpdateSpy = jest.spyOn(Utils, 'handleVariableUpdate');
    wrapper
      .find('#refresh-button')
      .first()
      .simulate('click');
    await act(async () => {
      wrapper
        .find('#save-button')
        .first()
        .simulate('click');
    });
    window.location = location;
    expect(handleVariableUpdateSpy).toHaveBeenCalled();
  });
});
