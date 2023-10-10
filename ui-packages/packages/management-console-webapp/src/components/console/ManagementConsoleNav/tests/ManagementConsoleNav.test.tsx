import React from 'react';
import { mount } from 'enzyme';
import ManagementConsoleNav from '../ManagementConsoleNav';
import { MemoryRouter } from 'react-router-dom';

describe('ManagementConsoleNav tests', () => {
  it('Snapshot testing with process list props', () => {
    const wrapper = mount(
      <MemoryRouter>
        <ManagementConsoleNav pathname={'/ProcessInstances'} />
      </MemoryRouter>
    ).find('ManagementConsoleNav');

    expect(wrapper).toMatchSnapshot();

    const managementConsoleNav = wrapper.findWhere(
      (nested) => nested.key() === 'process-instances-nav'
    );

    expect(managementConsoleNav.exists()).toBeTruthy();
    expect(managementConsoleNav.props().isActive).toBeTruthy();
  });

  it('Snapshot testing with jobs management props', () => {
    const wrapper = mount(
      <MemoryRouter>
        <ManagementConsoleNav pathname={'/JobsManagement'} />
      </MemoryRouter>
    ).find('ManagementConsoleNav');

    expect(wrapper).toMatchSnapshot();

    const managementConsoleNav = wrapper.findWhere(
      (nested) => nested.key() === 'jobs-management-nav'
    );

    expect(managementConsoleNav.exists()).toBeTruthy();
    expect(managementConsoleNav.props().isActive).toBeTruthy();
  });
});
