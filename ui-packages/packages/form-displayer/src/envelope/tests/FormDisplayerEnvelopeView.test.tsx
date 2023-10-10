import React from 'react';
import { act } from 'react-dom/test-utils';
import { mount } from 'enzyme';
import { MockedMessageBusClientApi } from './mocks/Mocks';
import FormDisplayer from '../components/FormDisplayer/FormDisplayer';
import {
  FormDisplayerEnvelopeView,
  FormDisplayerEnvelopeViewApi
} from '../FormDisplayerEnvelopeView';
import { FormType } from '../../api';
import ErrorBoundary from '../components/ErrorBoundary/ErrorBoundary';

jest.mock('../components/FormDisplayer/FormDisplayer');

describe('FormDisplayerEnvelopeView tests', () => {
  it('Snapshot', () => {
    const channelApi = new MockedMessageBusClientApi();

    const forwardRef = React.createRef<FormDisplayerEnvelopeViewApi>();

    let wrapper = mount(
      <FormDisplayerEnvelopeView channelApi={channelApi} ref={forwardRef} />
    ).find('FormDisplayerEnvelopeView');

    expect(wrapper).toMatchSnapshot();

    const formContent = {
      formInfo: {
        lastModified: new Date('2021-08-23T13:26:02.130Z'),
        name: 'react_hiring_HRInterview',
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
    };

    act(() => {
      if (forwardRef.current) {
        forwardRef.current.initForm({
          form: formContent
        });
      }
    });

    wrapper = wrapper.update();

    const envelopeView = wrapper.find(FormDisplayerEnvelopeView);

    expect(envelopeView).toMatchSnapshot();

    const boundary = wrapper.find(ErrorBoundary);
    expect(boundary.exists()).toBeTruthy();

    const formDisplayer = envelopeView.find(FormDisplayer);

    expect(formDisplayer.exists()).toBeTruthy();
    expect(formDisplayer.props().isEnvelopeConnectedToChannel).toBeTruthy();
  });
});
