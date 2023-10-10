import React from 'react';
import { act } from 'react-dom/test-utils';
import { mount } from 'enzyme';
import { MockedMessageBusClientApi } from './mocks/Mocks';
import JobsManagementEnvelopeView, {
  JobsManagementEnvelopeViewApi
} from '../JobsManagementEnvelopeView';
import JobsManagement from '../components/JobsManagement/JobsManagement';

jest.mock('../components/JobsManagement/JobsManagement');

describe('JobsManagementEnvelopeView tests', () => {
  it('Snapshot', () => {
    const channelApi = new MockedMessageBusClientApi();

    const forwardRef = React.createRef<JobsManagementEnvelopeViewApi>();

    const wrapper = mount(
      <JobsManagementEnvelopeView channelApi={channelApi} ref={forwardRef} />
    ).find('JobsManagementEnvelopeView');

    expect(wrapper).toMatchSnapshot();

    act(() => {
      if (forwardRef.current) {
        forwardRef.current.initialize();
      }
    });

    const envelopeView = wrapper.update().find(JobsManagementEnvelopeView);

    expect(envelopeView).toMatchSnapshot();

    const inbox = envelopeView.find(JobsManagement);

    expect(inbox.exists()).toBeTruthy();
    expect(inbox.props().isEnvelopeConnectedToChannel).toBeTruthy();
    expect(inbox.props().driver).not.toBeNull();
  });
});
