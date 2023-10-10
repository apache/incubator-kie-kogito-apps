import React from 'react';
import { EmbeddedFormDetails } from '../EmbeddedFormDetails';
import { MockedFormDetailsDriver } from './utils/Mocks';
import { mount } from 'enzyme';

describe('EmbeddedFormDetails tests', () => {
  it('Snapshot', () => {
    const props = {
      targetOrigin: 'origin',
      envelopePath: 'path',
      driver: new MockedFormDetailsDriver(),
      formData: {
        name: 'form1',
        type: 'html' as any,
        lastModified: new Date('2020-07-11T18:30:00.000Z')
      }
    };

    const wrapper = mount(<EmbeddedFormDetails {...props} />);

    expect(wrapper).toMatchSnapshot();
    expect(wrapper.props().driver).toStrictEqual(props.driver);
    expect(wrapper.props().targetOrigin).toStrictEqual(props.targetOrigin);
    expect(wrapper.props().formData).toStrictEqual(props.formData);

    const contentDiv = wrapper.find('div');

    expect(contentDiv.exists()).toBeTruthy();
  });
});
