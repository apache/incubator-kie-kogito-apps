import React from 'react';
import { mount } from 'enzyme';
import TaskInboxPage from '../TaskInboxPage';
import TaskInboxContainer from '../container/TaskInboxContainer/TaskInboxContainer';

jest.mock('../container/TaskInboxContainer/TaskInboxContainer');

describe('TaskInboxPage tests', () => {
  it('Snapshot', () => {
    const wrapper = mount(<TaskInboxPage />).find('TaskInboxPage');

    expect(wrapper).toMatchSnapshot();
    expect(wrapper.find(TaskInboxContainer).exists()).toBeTruthy();
  });
});
