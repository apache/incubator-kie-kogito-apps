import React from 'react';
import ErrorPopover from '../ErrorPopover';
import { GraphQL, getWrapper } from '@kogito-apps/common';
import ProcessInstanceState = GraphQL.ProcessInstanceState;
import { mount } from 'enzyme';
import { Popover } from '@patternfly/react-core';
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
  },
  onSkipClick: jest.fn(),
  onRetryClick: jest.fn()
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

  it('Skip call success', () => {
    let wrapper = mount(<ErrorPopover {...props} />);
    wrapper
      .find(Popover)
      .props()
      ['footerContent'][0]['props']['onClick']();
    wrapper = wrapper.update();
    expect(props.onSkipClick).toHaveBeenCalled();
  });

  it('Retry call success', () => {
    let wrapper = mount(<ErrorPopover {...props} />);
    wrapper
      .find(Popover)
      .props()
      ['footerContent'][1]['props']['onClick']();
    wrapper = wrapper.update();
    expect(props.onRetryClick).toHaveBeenCalled();
  });
});
