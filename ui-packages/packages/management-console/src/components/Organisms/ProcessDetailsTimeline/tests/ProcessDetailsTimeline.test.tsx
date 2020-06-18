import React from 'react';
import { shallow } from 'enzyme';
import ProcessDetailsTimeline from '../ProcessDetailsTimeline';
import { GraphQL, getWrapper } from '@kogito-apps/common';
import ProcessInstanceState = GraphQL.ProcessInstanceState;
import { act } from 'react-dom/test-utils';
import { Dropdown } from '@patternfly/react-core';
import axios from 'axios';
import {
  handleSkip,
  handleRetry,
  handleNodeInstanceCancel
} from '../../../../utils/Utils';
import wait from 'waait';

jest.mock('../../../Atoms/ProcessListModal/ProcessListModal');
jest.mock('../../../../utils/Utils');
jest.mock('axios');
const mockedAxios = axios as jest.Mocked<typeof axios>;
const props1 = {
  data: {
    id: '8035b580-6ae4-4aa8-9ec0-e18e19809e0b',
    processId: 'travels',
    parentProcessInstanceId: null,
    parentProcessInstance: null,
    processName: 'travels',
    roles: [],
    state: ProcessInstanceState.Active,
    rootProcessInstanceId: null,
    endpoint: 'http://localhost:4000',
    addons: ['jobs-management', 'prometheus-monitoring', 'process-management'],
    error: {
      nodeDefinitionId: 'abc-efg-hij',
      message: 'Something went wrong'
    },
    start: '2019-10-22T03:40:44.089Z',
    serviceUrl: '2019-10-22T03:40:44.089Z',
    end: null,
    variables:
      '{"flight":{"arrival":"2019-10-30T22:00:00Z[UTC]","departure":"2019-10-22T22:00:00Z[UTC]","flightNumber":"MX555"},"hotel":{"address":{"city":"Berlin","country":"Germany","street":"street","zipCode":"12345"},"bookingNumber":"XX-012345","name":"Perfect hotel","phone":"09876543"},"trip":{"begin":"2019-10-22T22:00:00Z[UTC]","city":"Berlin","country":"Germany","end":"2019-10-30T22:00:00Z[UTC]","visaRequired":false},"traveller":{"address":{"city":"Karkow","country":"Poland","street":"palna","zipCode":"200300"},"email":"rob@redhat.com","firstName":"Rob","lastName":"Rob","nationality":"Polish"}}',
    nodes: [
      {
        nodeId: '111-555-898',
        name: 'Confirm travel',
        definitionId: 'abc-efg-hij',
        id: '69e0a0f5-2360-4174-a8f8-a892a31fc2f9r25e',
        enter: '2019-10-22T03:40:44.089Z',
        exit: null,
        type: 'HumanTaskNode'
      },
      {
        nodeId: '111-555-898',
        name: 'Confirm travel',
        definitionId: '_69e0a0f5-2360-4174-a8f8-a892a31fc2f964rc',
        id: '69e0a0f5-2360-4174-a8f8-a892a31fc2f9',
        enter: '2019-10-22T03:40:44.089Z',
        exit: null,
        type: 'HumanTaskNode'
      },
      {
        name: 'End Event 1',
        definitionId: '_7244ba1b-75ec-4789-8c65-499a0c5b1a6f',
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
  },
  setModalTitle: jest.fn(),
  setTitleType: jest.fn(),
  setModalContent: jest.fn(),
  handleModalToggle: jest.fn()
};

const props2 = {
  data: {
    id: '8035b580-6ae4-4aa8-9ec0-e18e19809e0b',
    processId: 'travels',
    parentProcessInstanceId: null,
    parentProcessInstance: null,
    processName: 'travels',
    roles: [],
    state: ProcessInstanceState.Active,
    rootProcessInstanceId: null,
    serviceUrl: null,
    endpoint: 'http://localhost:4000',
    addons: ['jobs-management', 'prometheus-monitoring', 'process-management'],
    error: {
      nodeDefinitionId: 'abc-efg-hij',
      message: 'Something went wrong'
    },
    start: '2019-10-22T03:40:44.089Z',
    end: null,
    variables:
      '{"flight":{"arrival":"2019-10-30T22:00:00Z[UTC]","departure":"2019-10-22T22:00:00Z[UTC]","flightNumber":"MX555"},"hotel":{"address":{"city":"Berlin","country":"Germany","street":"street","zipCode":"12345"},"bookingNumber":"XX-012345","name":"Perfect hotel","phone":"09876543"},"trip":{"begin":"2019-10-22T22:00:00Z[UTC]","city":"Berlin","country":"Germany","end":"2019-10-30T22:00:00Z[UTC]","visaRequired":false},"traveller":{"address":{"city":"Karkow","country":"Poland","street":"palna","zipCode":"200300"},"email":"rob@redhat.com","firstName":"Rob","lastName":"Rob","nationality":"Polish"}}',
    nodes: [
      {
        name: 'End Event 1',
        definitionId: 'abc-efg-hij',
        id: '7244ba1b-75ec-4789-8c65-499a0c5b1a6f',
        nodeId: '123-456-789',
        enter: '2019-10-22T04:43:01.144Z',
        exit: null,
        type: 'HumanTaskNode'
      },
      {
        name: 'Book flight',
        definitionId: 'ServiceTask_1',
        id: '2f588da5-a323-4111-9017-3093ef9319d1',
        nodeId: '123-456-789',
        enter: '2019-10-22T04:43:01.144Z',
        exit: null,
        type: 'WorkItemNode'
      },
      {
        name: 'StartProcess',
        definitionId: 'StartEvent_1',
        id: '6ed7aa17-4bb1-48e3-b34a-5a4c5773dff2',
        nodeId: '123-456-789',
        enter: '2019-10-22T04:43:01.144Z',
        exit: null,
        type: 'HumanTaskNode'
      }
    ],
    childProcessInstances: []
  },
  setModalTitle: jest.fn(),
  setTitleType: jest.fn(),
  setModalContent: jest.fn(),
  handleModalToggle: jest.fn()
};

const props3 = {
  data: {
    id: '8035b580-6ae4-4aa8-9ec0-e18e19809e0b',
    processId: 'travels',
    parentProcessInstanceId: null,
    parentProcessInstance: null,
    processName: 'travels',
    roles: [],
    state: ProcessInstanceState.Completed,
    rootProcessInstanceId: null,
    serviceUrl: null,
    endpoint: 'http://localhost:4000',
    addons: ['jobs-management', 'prometheus-monitoring', 'process-management'],
    error: {
      nodeDefinitionId: 'abc-efg-hij',
      message: 'Something went wrong'
    },
    start: '2019-10-22T03:40:44.089Z',
    end: null,
    variables:
      '{"flight":{"arrival":"2019-10-30T22:00:00Z[UTC]","departure":"2019-10-22T22:00:00Z[UTC]","flightNumber":"MX555"},"hotel":{"address":{"city":"Berlin","country":"Germany","street":"street","zipCode":"12345"},"bookingNumber":"XX-012345","name":"Perfect hotel","phone":"09876543"},"trip":{"begin":"2019-10-22T22:00:00Z[UTC]","city":"Berlin","country":"Germany","end":"2019-10-30T22:00:00Z[UTC]","visaRequired":false},"traveller":{"address":{"city":"Karkow","country":"Poland","street":"palna","zipCode":"200300"},"email":"rob@redhat.com","firstName":"Rob","lastName":"Rob","nationality":"Polish"}}',
    nodes: [
      {
        name: 'End Event 1',
        definitionId: 'abc-efg-hij',
        id: '7244ba1b-75ec-4789-8c65-499a0c5b1a6f',
        nodeId: '123-456-789',
        enter: '2019-10-22T04:43:01.144Z',
        exit: null,
        type: 'HumanTaskNode'
      },
      {
        name: 'Book flight',
        definitionId: 'ServiceTask_1',
        id: '2f588da5-a323-4111-9017-3093ef9319d1',
        nodeId: '123-456-789',
        enter: '2019-10-22T04:43:01.144Z',
        exit: null,
        type: 'WorkItemNode'
      },
      {
        name: 'StartProcess',
        definitionId: 'StartEvent_1',
        id: '6ed7aa17-4bb1-48e3-b34a-5a4c5773dff2',
        nodeId: '123-456-789',
        enter: '2019-10-22T04:43:01.144Z',
        exit: null,
        type: 'HumanTaskNode'
      }
    ],
    childProcessInstances: []
  },
  setModalTitle: jest.fn(),
  setTitleType: jest.fn(),
  setModalContent: jest.fn(),
  handleModalToggle: jest.fn()
};
/* tslint:disable */

describe('ProcessDetailsTimeline component tests', () => {
  it('Snapshot testing for service url available', () => {
    const wrapper = shallow(<ProcessDetailsTimeline {...props1} />);
    expect(wrapper).toMatchSnapshot();
  });

  it('Snapshot testing for no service url', () => {
    const wrapper = shallow(<ProcessDetailsTimeline {...props2} />);
    expect(wrapper).toMatchSnapshot();
  });
  it('Snapshot testing for completed state', () => {
    const wrapper = shallow(<ProcessDetailsTimeline {...props3} />);
    expect(wrapper).toMatchSnapshot();
  });
  it('onSelect click test', () => {
    const wrapper = getWrapper(
      <ProcessDetailsTimeline {...props1} />,
      'ProcessDetailsTimeline'
    );
    act(() => {
      wrapper
        .find(Dropdown)
        .first()
        .props()
        ['onSelect']();
      wrapper
        .find(Dropdown)
        .at(1)
        .props()
        ['onSelect']();
    });
    expect(
      wrapper
        .find(Dropdown)
        .first()
        .props()['isOpen']
    ).toBeFalsy();
  });

  it('onToggle click test - node management', () => {
    const contentForNodeManagement = {
      nodeId: '111-555-898',
      name: 'Confirm travel',
      definitionId: '_69e0a0f5-2360-4174-a8f8-a892a31fc2f964rc',
      id: '69e0a0f5-2360-4174-a8f8-a892a31fc2f9',
      enter: '2019-10-22T03:40:44.089Z',
      exit: null,
      type: 'HumanTaskNode'
    };
    const wrapper = getWrapper(
      <ProcessDetailsTimeline {...props1} />,
      'ProcessDetailsTimeline'
    );
    act(() => {
      wrapper
        .find('#timeline-kebab-toggle-nodemanagement-1')
        .find('KebabToggle')
        .props()
        ['onToggle'](
          false,
          contentForNodeManagement,
          'timeline-kebab-toggle-nodemanagement-1'
        );
      wrapper
        .find('#timeline-kebab-toggle-nodemanagement-1')
        .find('KebabToggle')
        .props()
        ['onToggle'](
          true,
          contentForNodeManagement,
          'timeline-kebab-toggle-nodemanagement-1'
        );
    });
    expect(
      wrapper
        .find('#timeline-kebab-toggle-nodemanagement-1')
        .find('KebabToggle')
        .prop('isOpen')
    ).toBeFalsy();
  });

  it('onToggle click test - process management', () => {
    const contentForProcessManagement = {
      nodeId: '111-555-898',
      name: 'Confirm travel',
      definitionId: 'abc-efg-hij',
      id: '69e0a0f5-2360-4174-a8f8-a892a31fc2f9r25e',
      enter: '2019-10-22T03:40:44.089Z',
      exit: null,
      type: 'HumanTaskNode'
    };
    const wrapper = getWrapper(
      <ProcessDetailsTimeline {...props1} />,
      'ProcessDetailsTimeline'
    );
    act(() => {
      wrapper
        .find('#timeline-kebab-toggle-0')
        .find('KebabToggle')
        .props()
        ['onToggle'](
          false,
          contentForProcessManagement,
          'timeline-kebab-toggle-0'
        );
    });
    expect(
      wrapper
        .find('#timeline-kebab-toggle-0')
        .find('KebabToggle')
        .prop('isOpen')
    ).toBeFalsy();
  });

  it('handle skip , handle retry and nodecancel click test', async () => {
    const wrapper = shallow(<ProcessDetailsTimeline {...props1} />);
    mockedAxios.post.mockResolvedValue({});
    mockedAxios.post.mockRejectedValue({});
    mockedAxios.delete.mockResolvedValue({});
    mockedAxios.delete.mockRejectedValue({});
    wrapper
      .find(Dropdown)
      .first()
      .props()
      ['dropdownItems'][0]['props']['onClick']();
    wrapper
      .find(Dropdown)
      .first()
      .props()
      ['dropdownItems'][1]['props']['onClick']();
    wrapper
      .find(Dropdown)
      .at(1)
      .props()
      ['dropdownItems'][0]['props']['onClick']();
    expect(handleSkip).toHaveBeenCalled();
    expect(handleRetry).toHaveBeenCalled();
    expect(handleNodeInstanceCancel).toHaveBeenCalled();
  });

  it('handleModalToggle click test', async () => {
    const wrapper = getWrapper(
      <ProcessDetailsTimeline {...props1} />,
      'ProcessDetailsTimeline'
    );
    await act(async () => {
      wrapper
        .find('MockedProcessListModal')
        .props()
        ['handleModalToggle']();
      await wait(1);
      wrapper.update();
    });
    expect(
      wrapper.find('MockedProcessListModal').props()['isModalOpen']
    ).toBeFalsy();
  });
});
