import React from 'react';
import { shallow } from 'enzyme';
import ProcessListTableItems from '../ProcessListTableItems';
import { GraphQL } from '@kogito-apps/common';
import ProcessInstanceState = GraphQL.ProcessInstanceState;

const initData = {
  ProcessInstances: [
    {
      id: '8035b580-6ae4-4aa8-9ec0-e18e19809e0b',
      processId: 'travels',
      parentProcessInstanceId: null,
      parentProcessInstance: null,
      processName: 'travels',
      roles: [],
      state: ProcessInstanceState.Active,
      rootProcessInstanceId: null,
      endpoint: 'http://localhost:4000',
      addons: [
        'jobs-management',
        'prometheus-monitoring',
        'process-management'
      ],
      error: {
        nodeDefinitionId: 'a1e139d5-4e77-48c9-84ae-3459188e90433n',
        message: 'Something went wrong'
      },
      start: '2019-10-22T03:40:44.089Z',
      end: null,
      variables:
        '{"flight":{"arrival":"2019-10-30T22:00:00Z[UTC]","departure":"2019-10-22T22:00:00Z[UTC]","flightNumber":"MX555"},"hotel":{"address":{"city":"Berlin","country":"Germany","street":"street","zipCode":"12345"},"bookingNumber":"XX-012345","name":"Perfect hotel","phone":"09876543"},"trip":{"begin":"2019-10-22T22:00:00Z[UTC]","city":"Berlin","country":"Germany","end":"2019-10-30T22:00:00Z[UTC]","visaRequired":false},"traveller":{"address":{"city":"Karkow","country":"Poland","street":"palna","zipCode":"200300"},"email":"rob@redhat.com","firstName":"Rob","lastName":"Rob","nationality":"Polish"}}',
      nodes: [
        {
          name: 'End Event 1',
          definitionId: 'EndEvent_1',
          id: '7244ba1b-75ec-4789-8c65-499a0c5b1a6f',
          nodeId: '123-456-789',
          enter: '2019-10-22T04:43:01.144Z',
          exit: '2019-10-22T04:43:01.144Z',
          type: 'EndNode'
        },
        {
          name: 'Book flight',
          definitionId: 'ServiceTask_1',
          id: '2f588da5-a323-4111-9017-3093ef9319d1',
          nodeId: '123-456-789',
          enter: '2019-10-22T04:43:01.144Z',
          exit: '2019-10-22T04:43:01.144Z',
          type: 'WorkItemNode'
        },
        {
          name: 'StartProcess',
          definitionId: 'StartEvent_1',
          id: '6ed7aa17-4bb1-48e3-b34a-5a4c5773dff2',
          nodeId: '123-456-789',
          enter: '2019-10-22T04:43:01.144Z',
          exit: '2019-10-22T04:43:01.144Z',
          type: 'StartNode'
        }
      ],
      childProcessInstances: []
    }
  ]
};

const props = {
  id: 0,
  checkedArray: [ProcessInstanceState.Active],
  processInstanceData: {
    id: 'c54ca5b0-b975-46e2-a9a0-6a86bf7ac21e',
    processId: 'flightBooking',
    parentProcessInstanceId: '8035b580-6ae4-4aa8-9ec0-e18e19809e0b',
    parentProcessInstance: null,
    isChecked: false,
    lastUpdate: '2019-10-22T03:40:44.089Z',
    processName: 'FlightBooking',
    rootProcessInstanceId: '8035b580-6ae4-4aa8-9ec0-e18e19809e0b',
    roles: [],
    state: ProcessInstanceState.Completed,
    serviceUrl: null,
    endpoint: 'http://localhost:4000',
    addons: [],
    error: {
      nodeDefinitionId: 'a1e139d5-81c77-48c9-84ae-34578e90433n',
      message: 'Something went wrong'
    },
    start: '2019-10-22T03:40:44.089Z',
    end: '2019-10-22T05:40:44.089Z',
    variables:
      '{"flight":{"arrival":"2019-10-30T22:00:00Z[UTC]","departure":"2019-10-22T22:00:00Z[UTC]","flightNumber":"MX555"},"trip":{"begin":"2019-10-22T22:00:00Z[UTC]","city":"Berlin","country":"Germany","end":"2019-10-30T22:00:00Z[UTC]","visaRequired":false},"traveller":{"address":{"city":"Karkow","country":"Poland","street":"palna","zipCode":"200300"},"email":"rob@redhat.com","firstName":"Rob","lastName":"Rob","nationality":"Polish"}}',
    nodes: [
      {
        nodeId: '1',
        name: 'End Event 1',
        definitionId: 'EndEvent_1',
        id: '7244ba1b-75ec-4789-8c65-499a0c5b1a6f',
        enter: '2019-10-22T04:43:01.144Z',
        exit: '2019-10-22T04:43:01.144Z',
        type: 'EndNode'
      },
      {
        nodeId: '2',
        name: 'Book flight',
        definitionId: 'ServiceTask_1',
        id: '2f588da5-a323-4111-9017-3093ef9319d1',
        enter: '2019-10-22T04:43:01.144Z',
        exit: '2019-10-22T04:43:01.144Z',
        type: 'WorkItemNode'
      },
      {
        nodeId: '3',
        name: 'StartProcess',
        definitionId: 'StartEvent_1',
        id: '6ed7aa17-4bb1-48e3-b34a-5a4c5773dff2',
        enter: '2019-10-22T04:43:01.144Z',
        exit: '2019-10-22T04:43:01.144Z',
        type: 'StartNode'
      }
    ],
    childProcessInstances: []
  },
  initData,
  setInitData: jest.fn(),
  setAbortedObj: jest.fn(),
  abortedObj: { '8035b580-6ae4-4aa8-9ec0-e18e19809e0b': 'travels' },
  loadingInitData: false,
  setIsAllChecked: jest.fn(),
  selectedNumber: 0,
  setSelectedNumber: jest.fn()
};

describe('DataListItem component tests', () => {
  it('Snapshot tests', () => {
    const wrapper = shallow(<ProcessListTableItems {...props} />);
    expect(wrapper).toMatchSnapshot();
  });
});
