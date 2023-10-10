import { mount } from 'enzyme';
import React from 'react';
import {
  EmbeddedProcessForm,
  EmbeddedProcessFormProps
} from '../EmbeddedProcessForm';
import { MockedProcessFormDriver } from './mocks/Mocks';

describe('EmbeddedProcessForm tests', () => {
  it('Snapshot', () => {
    const props: EmbeddedProcessFormProps = {
      driver: new MockedProcessFormDriver(),
      processDefinition: null,
      targetOrigin: 'origin'
    };

    const wrapper = mount(<EmbeddedProcessForm {...props} />);

    expect(wrapper).toMatchSnapshot();

    expect(wrapper.props().driver).toStrictEqual(props.driver);
    expect(wrapper.props().targetOrigin).toStrictEqual(props.targetOrigin);

    const contentDiv = wrapper.find('div');

    expect(contentDiv.exists()).toBeTruthy();
  });
});
