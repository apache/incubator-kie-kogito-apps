import React from 'react';
import { EmbeddedJobsManagement } from '../EmbeddedJobsManagement';
import { MockedJobsManagementDriver } from './mocks/Mocks';
import { mount } from 'enzyme';

describe('EmbeddedJobsManagement tests', () => {
  it('Snapshot', () => {
    const props = {
      targetOrigin: 'origin',
      driver: new MockedJobsManagementDriver()
    };

    const wrapper = mount(<EmbeddedJobsManagement {...props} />);

    expect(wrapper).toMatchSnapshot();
    expect(wrapper.props().driver).toStrictEqual(props.driver);
    expect(wrapper.props().targetOrigin).toStrictEqual(props.targetOrigin);
    const div = wrapper.find('div');

    expect(div.exists()).toBeTruthy();
  });
});
