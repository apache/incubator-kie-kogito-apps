import React from 'react';
import ErrorPopover from '../ErrorPopover';
import { GraphQL, getWrapper } from '@kogito-apps/common';
import ProcessInstanceState = GraphQL.ProcessInstanceState;
import axios from 'axios';
import { mount } from 'enzyme';
import { Popover } from '@patternfly/react-core';
import { act } from 'react-dom/test-utils';
jest.mock('../../../Atoms/ProcessListModal/ProcessListModal');
jest.mock('axios');
const mockedAxios = axios as jest.Mocked<typeof axios>;

const props = {
  processInstanceData: {
    id: 'e4448857-fa0c-403b-ad69-f0a353458b9d',
    processId: 'travels',
    businessKey: 'T1234',
    parentProcessInstanceId: null,
    parentProcessInstance: null,
    processName: 'travels',
    roles: [],
    state: ProcessInstanceState.Error,
    rootProcessInstanceId: null,
    addons: ['jobs-management', 'prometheus-monitoring', 'process-management'],
    start: '2019-10-22T03:40:44.089Z',
    end: '2019-10-22T05:40:44.089Z',
    lastUpdate: '2019-10-22T03:40:44.089Z',
    error: {
      nodeDefinitionId: '_2140F05A-364F-40B3-BB7B-B12927065DF8',
      message: 'Something went wrong'
    },
    serviceUrl: 'http://localhost:4000',
    endpoint: 'http://localhost:4000',
    variables:
      '{"flight":{"arrival":"2019-10-30T22:00:00Z[UTC]","departure":"2019-10-22T22:00:00Z[UTC]","flightNumber":"MX555"},"trip":{"begin":"2019-10-22T22:00:00Z[UTC]","city":"Bangalore","country":"India","end":"2019-10-30T22:00:00Z[UTC]","visaRequired":false},"hotel":{"address":{"city":"Bangalore","country":"India","street":"street","zipCode":"12345"},"bookingNumber":"XX-012345","name":"Perfect hotel","phone":"09876543"},"traveller":{"address":{"city":"Bangalore","country":"US","street":"Bangalore","zipCode":"560093"},"email":"ajaganat@redhat.com","firstName":"Ajay","lastName":"Jaganathan","nationality":"US"}}',
    nodes: [],
    childProcessInstances: []
  }
};

describe('Errorpopover component tests', () => {
  it('snapshot testing with error object', () => {
    const wrapper = getWrapper(<ErrorPopover {...props} />, 'ErrorPopover');
    expect(wrapper).toMatchSnapshot();
  });

  it('snapshot testing without error object', () => {
    const wrapper = getWrapper(
      <ErrorPopover
        {...{
          ...props,
          processInstanceData: { ...props.processInstanceData, error: null }
        }}
      />,
      'ErrorPopover'
    );
    expect(wrapper).toMatchSnapshot();
  });

  describe('Skip process test', () => {
    it('Skip call success', async () => {
      let wrapper = mount(<ErrorPopover {...props} />);
      mockedAxios.post.mockResolvedValue({});
      await act(async () => {
        wrapper
          .find(Popover)
          .props()
          ['footerContent'][0]['props']['onClick']();
      });
      wrapper = wrapper.update();
      expect(
        wrapper.find('MockedProcessListModal').props()['modalContent']
      ).toEqual('The process travels was successfully skipped.');
    });

    it('Skip call failure', async () => {
      let wrapper = mount(<ErrorPopover {...props} />);
      mockedAxios.post.mockRejectedValue({
        message: '404 error'
      });
      await act(async () => {
        wrapper
          .find(Popover)
          .props()
          ['footerContent'][0]['props']['onClick']();
      });
      wrapper = wrapper.update();
      expect(
        wrapper.find('MockedProcessListModal').props()['modalContent']
      ).toEqual('The process travels failed to skip. Message: "404 error"');
    });
  });

  describe('Retry process test', () => {
    it('Retry call success', async () => {
      let wrapper = mount(<ErrorPopover {...props} />);
      mockedAxios.post.mockResolvedValue({});
      await act(async () => {
        wrapper
          .find(Popover)
          .props()
          ['footerContent'][1]['props']['onClick']();
      });
      wrapper = wrapper.update();
      expect(
        wrapper.find('MockedProcessListModal').props()['modalContent']
      ).toEqual('The process travels was successfully re-executed.');
    });

    it('Retry call failure', async () => {
      let wrapper = mount(<ErrorPopover {...props} />);
      mockedAxios.post.mockRejectedValue({
        message: '404 error'
      });
      await act(async () => {
        wrapper
          .find(Popover)
          .props()
          ['footerContent'][1]['props']['onClick']();
      });
      wrapper = wrapper.update();
      expect(
        wrapper.find('MockedProcessListModal').props()['modalContent']
      ).toEqual(
        'The process travels failed to re-execute. Message: "404 error"'
      );
    });
  });
});
