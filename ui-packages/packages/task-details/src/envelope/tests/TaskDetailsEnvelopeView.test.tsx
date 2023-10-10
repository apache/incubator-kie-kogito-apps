import React from 'react';
import { mount } from 'enzyme';
import { act } from 'react-dom/test-utils';
import {
  TaskDetailsEnvelopeView,
  TaskDetailsEnvelopeViewApi
} from '../TaskDetailsEnvelopeView';
import { MockedMessageBusClientApi, userTask } from './utils/Mocks';
import TaskDetails from '../component/TaskDetails';

jest.mock('../component/TaskDetails');

describe('TaskDetailsEnvelopeApiImpl tests', () => {
  it('Snapshot test', () => {
    const channelApi = new MockedMessageBusClientApi();

    const forwardRef = React.createRef<TaskDetailsEnvelopeViewApi>();

    let wrapper = mount(
      <TaskDetailsEnvelopeView channelApi={channelApi} ref={forwardRef} />
    )
      .update()
      .find(TaskDetailsEnvelopeView);

    expect(wrapper).toMatchSnapshot();

    act(() => {
      if (forwardRef.current) {
        forwardRef.current.setTask(userTask);
      }
    });

    wrapper = wrapper.update().find(TaskDetailsEnvelopeView);

    expect(wrapper).toMatchSnapshot();

    const details = wrapper.find(TaskDetails);

    expect(details.exists()).toBeTruthy();
    expect(details.props().userTask).toBe(userTask);
  });
});
