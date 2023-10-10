import React from 'react';
import { EmbeddedTaskInbox } from '../EmbeddedTaskInbox';
import { MockedTaskInboxDriver } from './utils/Mocks';
import { mount } from 'enzyme';

describe('EmbeddedTaskInbox tests', () => {
  it('Snapshot', () => {
    const props = {
      targetOrigin: 'origin',
      envelopePath: 'path',
      driver: new MockedTaskInboxDriver(),
      allTaskStates: ['Ready', 'Completed'],
      activeTaskStates: ['Ready']
    };

    const wrapper = mount(<EmbeddedTaskInbox {...props} />);

    expect(wrapper).toMatchSnapshot();

    expect(wrapper.props().allTaskStates).toStrictEqual(props.allTaskStates);
    expect(wrapper.props().activeTaskStates).toStrictEqual(
      props.activeTaskStates
    );
    expect(wrapper.props().driver).toStrictEqual(props.driver);
    expect(wrapper.props().targetOrigin).toStrictEqual(props.targetOrigin);

    const contentDiv = wrapper.find('div');

    expect(contentDiv.exists()).toBeTruthy();
  });
});
