import React from 'react';
import { act } from 'react-dom/test-utils';
import { mount } from 'enzyme';
import { MockedMessageBusClientApi } from './mocks/Mocks';
import ProcessFormEnvelopeView, {
  ProcessFormEnvelopeViewApi
} from '../ProcessFormEnvelopeView';
import ProcessForm from '../components/ProcessForm/ProcessForm';

jest.mock('../components/ProcessForm/ProcessForm');

describe('ProcessFormEnvelopeView tests', () => {
  it('Snapshot', () => {
    const channelApi = MockedMessageBusClientApi();
    const forwardRef = React.createRef<ProcessFormEnvelopeViewApi>();

    let wrapper = mount(
      <ProcessFormEnvelopeView channelApi={channelApi} ref={forwardRef} />
    ).find('ProcessFormEnvelopeView');

    act(() => {
      if (forwardRef.current) {
        forwardRef.current.initialize({
          processName: 'process1',
          endpoint: 'http://localhost:4000'
        });
      }
    });

    wrapper = wrapper.update();

    expect(wrapper.find(ProcessFormEnvelopeView)).toMatchSnapshot();

    const processForm = wrapper.find(ProcessForm);

    expect(processForm.exists()).toBeTruthy();
    expect(processForm.props().isEnvelopeConnectedToChannel).toBeTruthy();
    expect(processForm.props().driver).not.toBeNull();
  });
});
