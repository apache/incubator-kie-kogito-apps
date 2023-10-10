import React from 'react';
import { act } from 'react-dom/test-utils';
import { mount } from 'enzyme';
import { MockedMessageBusClientApi } from './mocks/Mocks';
import FormDetails from '../components/FormDetails/FormDetails';
import FormDetailsEnvelopeView, {
  FormDetailsEnvelopeViewApi
} from '../FormDetailsEnvelopeView';

jest.mock('../components/FormDetails/FormDetails');

describe('FormDetailsEnvelopeView tests', () => {
  it('Snapshot', () => {
    const channelApi = new MockedMessageBusClientApi();

    const forwardRef = React.createRef<FormDetailsEnvelopeViewApi>();

    let wrapper = mount(
      <FormDetailsEnvelopeView
        channelApi={channelApi}
        ref={forwardRef}
        targetOrigin="http://localhost:9000"
      />
    ).find('FormDetailsEnvelopeView');

    expect(wrapper).toMatchSnapshot();

    act(() => {
      if (forwardRef.current) {
        forwardRef.current.initialize();
      }
    });

    wrapper = wrapper.update();

    const envelopeView = wrapper.find(FormDetailsEnvelopeView);

    expect(envelopeView).toMatchSnapshot();

    const formDetails = envelopeView.find(FormDetails);

    expect(formDetails.exists()).toBeTruthy();
    expect(formDetails.props().isEnvelopeConnectedToChannel).toBeTruthy();
    expect(formDetails.props().driver).not.toBeNull();
  });
});
