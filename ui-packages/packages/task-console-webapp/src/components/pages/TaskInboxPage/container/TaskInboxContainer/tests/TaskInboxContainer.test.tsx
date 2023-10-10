import React from 'react';
import { mount } from 'enzyme';
import { DefaultUser } from '@kogito-apps/consoles-common/dist/environment/auth';
import TaskInboxContainer from '../TaskInboxContainer';
import * as TaskInboxContext from '../../../../../../channel/inbox/TaskInboxContext';
import { TaskInboxQueries } from '../../../../../../channel/inbox/TaskInboxQueries';
import { TaskInboxGatewayApiImpl } from '../../../../../../channel/inbox/TaskInboxGatewayApi';

const MockQueries = jest.fn<TaskInboxQueries, []>(() => ({
  getUserTaskById: jest.fn(),
  getUserTasks: jest.fn()
}));

jest
  .spyOn(TaskInboxContext, 'useTaskInboxGatewayApi')
  .mockImplementation(
    () =>
      new TaskInboxGatewayApiImpl(new DefaultUser('jon', []), new MockQueries())
  );

describe('TaskInboxContainer tests', () => {
  it('Snapshot', () => {
    const wrapper = mount(<TaskInboxContainer />).find('TaskInboxContainer');

    expect(wrapper).toMatchSnapshot();

    const forwardRef = wrapper.childAt(0);
    expect(forwardRef.props().activeTaskStates).toStrictEqual([
      'Ready',
      'Reserved'
    ]);
    expect(forwardRef.props().allTaskStates).toStrictEqual([
      'Ready',
      'Reserved',
      'Completed',
      'Aborted',
      'Skipped'
    ]);
    expect(forwardRef.props().driver).not.toBeNull();
    expect(forwardRef.props().targetOrigin).toBe('http://localhost');
  });
});
