import React from 'react';
import { shallow, mount } from 'enzyme';
import ProcessDetailsTimeline from '../ProcessDetailsTimeline';
import { GraphQL } from '@kogito-apps/common';
import ProcessInstanceState = GraphQL.ProcessInstanceState;
import { act } from 'react-dom/test-utils';
import { Dropdown, DropdownItem, KebabToggle } from '@patternfly/react-core';
import axios from 'axios';
import wait from 'waait';
import * as Utils from '../../../../utils/Utils';
jest.mock('../../../Atoms/ProcessListModal/ProcessListModal');
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
  }
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
  }
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
  }
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

  it('onKebabToggle click test', async () => {
    let wrapper = mount(<ProcessDetailsTimeline {...props1} />);
    await act(async () => {
      wrapper
        .find(Dropdown)
        .at(0)
        .find(KebabToggle)
        .find('button')
        .simulate('click');
      await wait(0);
      wrapper = wrapper.update();
    });
    expect(
      wrapper
        .find(Dropdown)
        .at(0)
        .props()['isOpen']
    ).toBeTruthy();
    await act(async () => {
      wrapper
        .find(Dropdown)
        .at(0)
        .find(KebabToggle)
        .find('button')
        .simulate('click');
      await wait(0);
      wrapper = wrapper.update();
    });
    expect(
      wrapper
        .find(Dropdown)
        .at(0)
        .props()['isOpen']
    ).toBeFalsy();
  });

  describe('handleSkip tests', () => {
    const handleSkipSpy = jest.spyOn(Utils, 'handleSkip');
    it('success test', async () => {
      mockedAxios.post.mockResolvedValue({});
      let wrapper = mount(<ProcessDetailsTimeline {...props1} />);
      await act(async () => {
        wrapper
          .find(Dropdown)
          .at(0)
          .find(KebabToggle)
          .find('button')
          .simulate('click');
        await wait(0);
        wrapper = wrapper.update();
        wrapper
          .find(DropdownItem)
          .at(1)
          .simulate('click');
        await wait(0);
        wrapper = wrapper.update();
      });
      expect(
        wrapper.find('MockedProcessListModal').props()['modalContent']
      ).toEqual('The node Confirm travel was successfully skipped.');
      expect(handleSkipSpy).toHaveBeenCalled();
    });
    it('failure', async () => {
      mockedAxios.post.mockRejectedValue({ message: '403 error' });
      let wrapper = mount(<ProcessDetailsTimeline {...props1} />);
      await act(async () => {
        wrapper
          .find(Dropdown)
          .at(0)
          .find(KebabToggle)
          .find('button')
          .simulate('click');
        await wait(0);
        wrapper = wrapper.update();
        wrapper
          .find(DropdownItem)
          .at(1)
          .simulate('click');
        await wait(0);
        wrapper = wrapper.update();
      });
      expect(
        wrapper.find('MockedProcessListModal').props()['modalContent']
      ).toEqual('The node Confirm travel failed to skip. Message: "403 error"');
      expect(handleSkipSpy).toHaveBeenCalled();
    });
  });

  describe('nodeInstanceRetrigger tests', () => {
    const handleNodeInstanceRetriggerSpy = jest.spyOn(
      Utils,
      'handleNodeInstanceRetrigger'
    );
    it('success test', async () => {
      mockedAxios.post.mockResolvedValue({});
      let wrapper = mount(<ProcessDetailsTimeline {...props1} />);
      await act(async () => {
        wrapper
          .find(Dropdown)
          .at(1)
          .find(KebabToggle)
          .find('button')
          .simulate('click');
        await wait(0);
        wrapper = wrapper.update();
        wrapper
          .find(DropdownItem)
          .at(0)
          .simulate('click');
        await wait(0);
        wrapper = wrapper.update();
      });
      expect(
        wrapper.find('MockedProcessListModal').props()['modalContent']
      ).toEqual('The node Confirm travel was successfully retriggered.');
      expect(handleNodeInstanceRetriggerSpy).toHaveBeenCalled();
    });
    it('failure', async () => {
      mockedAxios.post.mockRejectedValue({ message: '403 error' });
      let wrapper = mount(<ProcessDetailsTimeline {...props1} />);
      await act(async () => {
        wrapper
          .find(Dropdown)
          .at(1)
          .find(KebabToggle)
          .find('button')
          .simulate('click');
        await wait(0);
        wrapper = wrapper.update();
        wrapper
          .find(DropdownItem)
          .at(0)
          .simulate('click');
        await wait(0);
        wrapper = wrapper.update();
      });
      expect(
        wrapper.find('MockedProcessListModal').props()['modalContent']
      ).toEqual(
        'The node Confirm travel failed to retrigger. Message: "403 error"'
      );
      expect(handleNodeInstanceRetriggerSpy).toHaveBeenCalled();
    });
  });

  describe('nodeInstanceCancel tests', () => {
    const handleNodeInstanceCancelSpy = jest.spyOn(
      Utils,
      'handleNodeInstanceCancel'
    );
    it('success test', async () => {
      mockedAxios.delete.mockResolvedValue({});
      let wrapper = mount(<ProcessDetailsTimeline {...props1} />);
      await act(async () => {
        wrapper
          .find(Dropdown)
          .at(1)
          .find(KebabToggle)
          .find('button')
          .simulate('click');
        await wait(0);
        wrapper = wrapper.update();
        wrapper
          .find(DropdownItem)
          .at(1)
          .simulate('click');
        await wait(0);
        wrapper = wrapper.update();
      });
      expect(
        wrapper.find('MockedProcessListModal').props()['modalContent']
      ).toEqual('The node Confirm travel was successfully canceled.');
      expect(handleNodeInstanceCancelSpy).toHaveBeenCalled();
    });
    it('failure', async () => {
      mockedAxios.delete.mockRejectedValue({ message: '403 error' });
      let wrapper = mount(<ProcessDetailsTimeline {...props1} />);
      await act(async () => {
        wrapper
          .find(Dropdown)
          .at(1)
          .find(KebabToggle)
          .find('button')
          .simulate('click');
        await wait(0);
        wrapper = wrapper.update();
        wrapper
          .find(DropdownItem)
          .at(1)
          .simulate('click');
        await wait(0);
        wrapper = wrapper.update();
      });
      expect(
        wrapper.find('MockedProcessListModal').props()['modalContent']
      ).toEqual(
        'The node Confirm travel failed to cancel. Message: "403 error"'
      );
      expect(handleNodeInstanceCancelSpy).toHaveBeenCalled();
    });
  });

  describe('handleRetry tests', () => {
    const handleRetrySpy = jest.spyOn(Utils, 'handleRetry');
    it('success test', async () => {
      mockedAxios.post.mockResolvedValue({});
      let wrapper = mount(<ProcessDetailsTimeline {...props1} />);
      await act(async () => {
        wrapper
          .find(Dropdown)
          .at(0)
          .find(KebabToggle)
          .find('button')
          .simulate('click');
        await wait(0);
        wrapper = wrapper.update();
        wrapper
          .find(DropdownItem)
          .at(0)
          .simulate('click');
        await wait(0);
        wrapper = wrapper.update();
      });
      expect(
        wrapper.find('MockedProcessListModal').props()['modalContent']
      ).toEqual('The node Confirm travel was successfully re-executed.');
      expect(handleRetrySpy).toHaveBeenCalled();
    });
    it('failure', async () => {
      mockedAxios.post.mockRejectedValue({ message: '403 error' });
      let wrapper = mount(<ProcessDetailsTimeline {...props1} />);
      await act(async () => {
        wrapper
          .find(Dropdown)
          .at(0)
          .find(KebabToggle)
          .find('button')
          .simulate('click');
        await wait(0);
        wrapper = wrapper.update();
        wrapper
          .find(DropdownItem)
          .at(0)
          .simulate('click');
        await wait(0);
        wrapper = wrapper.update();
      });
      expect(
        wrapper.find('MockedProcessListModal').props()['modalContent']
      ).toEqual(
        'The node Confirm travel failed to re-execute. Message: "403 error"'
      );
      expect(handleRetrySpy).toHaveBeenCalled();
    });
  });
});
