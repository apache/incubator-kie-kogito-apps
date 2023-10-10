import React from 'react';
import { mount } from 'enzyme';
import TaskConsoleRoutes from '../TaskConsoleRoutes';
import { MemoryRouter, Route } from 'react-router-dom';
import { TaskDetailsPage, TaskInboxPage } from '../../../pages';

jest.mock('../../../pages/TaskInboxPage/TaskInboxPage');
jest.mock('../../../pages/TaskDetailsPage/TaskDetailsPage');

describe('TaskConsoleRoutes tests', () => {
  it('Default route test', () => {
    const wrapper = mount(
      <MemoryRouter keyLength={0} initialEntries={['/']}>
        <TaskConsoleRoutes />
      </MemoryRouter>
    ).find('TaskConsoleRoutes');

    expect(wrapper).toMatchSnapshot();

    const route = wrapper.find(Route);
    expect(route.exists()).toBeTruthy();

    const taskInboxPage = wrapper.find(TaskInboxPage);
    expect(taskInboxPage.exists()).toBeTruthy();
  });

  it('TaskInbox route test', () => {
    const wrapper = mount(
      <MemoryRouter keyLength={0} initialEntries={['/TaskInbox']}>
        <TaskConsoleRoutes />
      </MemoryRouter>
    ).find('TaskConsoleRoutes');

    expect(wrapper).toMatchSnapshot();

    const route = wrapper.find(Route);
    expect(route.exists()).toBeTruthy();

    const taskInboxPage = wrapper.find(TaskInboxPage);
    expect(taskInboxPage.exists()).toBeTruthy();
  });

  it('TaskDetails route test', () => {
    const wrapper = mount(
      <MemoryRouter keyLength={0} initialEntries={['/TaskDetails/id']}>
        <TaskConsoleRoutes />
      </MemoryRouter>
    ).find('TaskConsoleRoutes');

    expect(wrapper).toMatchSnapshot();

    const route = wrapper.find(Route);
    expect(route.exists()).toBeTruthy();

    const taskInboxPage = wrapper.find(TaskDetailsPage);
    expect(taskInboxPage.exists()).toBeTruthy();
  });
});
