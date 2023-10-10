import { mount } from 'enzyme';
import React from 'react';
import { EmbeddedTaskForm, EmbeddedTaskFormProps } from '../EmbeddedTaskForm';
import { MockedTaskFormDriver, testUserTask } from './mocks/Mocks';

describe('EmbeddedTaskForm tests', () => {
  it('Snapshot', () => {
    const props: EmbeddedTaskFormProps = {
      driver: new MockedTaskFormDriver(),
      userTask: testUserTask,
      user: {
        id: 'test',
        groups: ['group1', 'group2']
      },
      targetOrigin: 'origin'
    };

    const wrapper = mount(<EmbeddedTaskForm {...props} />);

    expect(wrapper).toMatchSnapshot();

    expect(wrapper.props().driver).toStrictEqual(props.driver);
    expect(wrapper.props().targetOrigin).toStrictEqual(props.targetOrigin);

    const contentDiv = wrapper.find('div');

    expect(contentDiv.exists()).toBeTruthy();
  });
});
