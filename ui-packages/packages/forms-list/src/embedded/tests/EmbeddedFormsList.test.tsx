import React from 'react';
import { EmbeddedFormsList } from '../EmbeddedFormsList';
import { MockedFormsListDriver } from './utils/Mocks';
import { mount } from 'enzyme';

describe('EmbeddedFormsList tests', () => {
  it('Snapshot', () => {
    const props = {
      targetOrigin: 'origin',
      envelopePath: 'path',
      driver: new MockedFormsListDriver()
    };

    const wrapper = mount(<EmbeddedFormsList {...props} />);

    expect(wrapper).toMatchSnapshot();
    expect(wrapper.props().driver).toStrictEqual(props.driver);
    expect(wrapper.props().targetOrigin).toStrictEqual(props.targetOrigin);

    const contentDiv = wrapper.find('div');

    expect(contentDiv.exists()).toBeTruthy();
  });
});
