import React from 'react';
import { mount } from 'enzyme';
import TaskConsoleNav from '../TaskConsoleNav';
import { MemoryRouter } from 'react-router-dom';

describe('TaskConsoleNav tests', () => {
  it('Snapshot', () => {
    const wrapper = mount(
      <MemoryRouter>
        <TaskConsoleNav />
      </MemoryRouter>
    ).find('TaskConsoleNav');

    expect(wrapper).toMatchSnapshot();

    const taskInboxNav = wrapper.findWhere(
      (nested) => nested.key() === 'task-inbox-nav'
    );

    expect(taskInboxNav.exists()).toBeTruthy();
    expect(taskInboxNav.props().isActive).toBeFalsy();
  });

  it('Snapshot with pathname', () => {
    const wrapper = mount(
      <MemoryRouter>
        <TaskConsoleNav pathname={'/TaskInbox'} />
      </MemoryRouter>
    ).find('TaskConsoleNav');

    expect(wrapper).toMatchSnapshot();

    const taskInboxNav = wrapper.findWhere(
      (nested) => nested.key() === 'task-inbox-nav'
    );

    expect(taskInboxNav.exists()).toBeTruthy();
    expect(taskInboxNav.props().isActive).toBeTruthy();
  });
});
