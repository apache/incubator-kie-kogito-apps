import React from 'react';
import { act } from 'react-dom/test-utils';
import { mount } from 'enzyme';
import { MockedMessageBusClientApi } from './mocks/Mocks';
import { TaskInboxState } from '../../api';
import TaskInboxEnvelopeView, {
  TaskInboxEnvelopeViewApi
} from '../TaskInboxEnvelopeView';
import TaskInbox from '../components/TaskInbox/TaskInbox';

jest.mock('../components/TaskInbox/TaskInbox');

const initialState: TaskInboxState = {
  filters: {
    taskNames: [],
    taskStates: []
  },
  sortBy: {
    property: 'lastUpdate',
    direction: 'asc'
  },
  currentPage: {
    offset: 0,
    limit: 10
  }
};
const activeTaskStates = ['Ready'];
const allTaskStates = ['Ready', 'Finished'];

describe('TaskInboxEnvelopeView tests', () => {
  it('Snapshot', () => {
    const channelApi = new MockedMessageBusClientApi();

    const forwardRef = React.createRef<TaskInboxEnvelopeViewApi>();

    let wrapper = mount(
      <TaskInboxEnvelopeView channelApi={channelApi} ref={forwardRef} />
    ).find('TaskInboxEnvelopeView');

    expect(wrapper).toMatchSnapshot();

    act(() => {
      if (forwardRef.current) {
        forwardRef.current.initialize(
          initialState,
          allTaskStates,
          activeTaskStates
        );
      }
    });

    wrapper = wrapper.update();

    const envelopeView = wrapper.find(TaskInboxEnvelopeView);

    expect(envelopeView).toMatchSnapshot();

    const inbox = envelopeView.find(TaskInbox);

    expect(inbox.exists()).toBeTruthy();
    expect(inbox.props().isEnvelopeConnectedToChannel).toBeTruthy();
    expect(inbox.props().driver).not.toBeNull();
    expect(inbox.props().allTaskStates).toBe(allTaskStates);
    expect(inbox.props().activeTaskStates).toBe(activeTaskStates);
  });
});
