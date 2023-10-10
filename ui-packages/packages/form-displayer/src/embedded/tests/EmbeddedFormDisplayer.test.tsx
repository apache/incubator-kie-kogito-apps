import React from 'react';
import { EmbeddedFormDisplayer } from '../EmbeddedFormDisplayer';
import { mount } from 'enzyme';
import { FormType } from '../../api';

describe('EmbeddedFormDisplayer tests', () => {
  it('Snapshot', () => {
    const props = {
      targetOrigin: 'origin',
      envelopePath: '/resources/form-displayer.html',
      formContent: {
        formInfo: {
          name: 'react_hiring_HRInterview',
          lastModified: new Date('2021-08-23T13:26:02.130Z'),
          type: FormType.TSX
        },
        configuration: {
          resources: {
            scripts: {},
            styles: {}
          },
          schema: 'json schema'
        },
        source: 'react source code'
      }
    };

    const wrapper = mount(<EmbeddedFormDisplayer {...props} />);

    expect(wrapper).toMatchSnapshot();
    expect(wrapper.props().targetOrigin).toStrictEqual(props.targetOrigin);
    const contentIframe = wrapper.find('iframe');

    expect(contentIframe.exists()).toBeTruthy();
  });
});
