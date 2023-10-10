import React from 'react';
import { act } from 'react-dom/test-utils';
import { mount } from 'enzyme';
import { MockedMessageBusClientApi } from './mocks/Mocks';
import FormsList from '../components/FormsList/FormsList';
import FormsListEnvelopeView, {
  FormsListEnvelopeViewApi
} from '../FormsListEnvelopeView';

jest.mock('../components/FormsList/FormsList');

describe('FormsListEnvelopeView tests', () => {
  it('Snapshot', () => {
    const channelApi = new MockedMessageBusClientApi();

    const forwardRef = React.createRef<FormsListEnvelopeViewApi>();

    let wrapper = mount(
      <FormsListEnvelopeView channelApi={channelApi} ref={forwardRef} />
    ).find('FormsListEnvelopeView');

    expect(wrapper).toMatchSnapshot();

    act(() => {
      if (forwardRef.current) {
        forwardRef.current.initialize();
      }
    });

    wrapper = wrapper.update();

    const envelopeView = wrapper.find(FormsListEnvelopeView);

    expect(envelopeView).toMatchSnapshot();

    const formsList = envelopeView.find(FormsList);

    expect(formsList.exists()).toBeTruthy();
    expect(formsList.props().isEnvelopeConnectedToChannel).toBeTruthy();
    expect(formsList.props().driver).not.toBeNull();
  });
});
