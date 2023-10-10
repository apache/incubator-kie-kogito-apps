import React from 'react';
import { mount } from 'enzyme';
import ManagementConsoleRoutes from '../ManagementConsoleRoutes';
import { MemoryRouter, Route } from 'react-router-dom';

jest.mock('../../../pages/ProcessListPage/ProcessListPage');
jest.mock('../../../pages/JobsManagementPage/JobsManagementPage');

const MockedComponent = (): React.ReactElement => {
  return <></>;
};

jest.mock(
  '@kogito-apps/consoles-common/dist/components/pages/PageNotFound',
  () =>
    Object.assign({}, jest.requireActual('@kogito-apps/consoles-common'), {
      PageNotFound: () => {
        return <MockedComponent />;
      }
    })
);

jest.mock('@kogito-apps/consoles-common/dist/components/pages/NoData', () =>
  Object.assign({}, jest.requireActual('@kogito-apps/consoles-common'), {
    NoData: () => {
      return <MockedComponent />;
    }
  })
);
describe('ManagementConsoleRoutes tests', () => {
  it('Test Jobs management route', () => {
    const wrapper = mount(
      <MemoryRouter keyLength={0} initialEntries={['/']}>
        <ManagementConsoleRoutes />
      </MemoryRouter>
    ).find('ManagementConsoleRoutes');

    expect(wrapper).toMatchSnapshot();

    const route = wrapper.find(Route);
    expect(route.exists()).toBeTruthy();

    const MockedProcessListPage = wrapper.find('MockedProcessListPage');
    expect(MockedProcessListPage.exists()).toBeTruthy();
  });
  it('process list test', () => {
    const wrapper = mount(
      <MemoryRouter keyLength={0} initialEntries={['/ProcessInstances']}>
        <ManagementConsoleRoutes />
      </MemoryRouter>
    ).find('ManagementConsoleRoutes');

    expect(wrapper).toMatchSnapshot();
    const route = wrapper.find(Route);
    expect(route.exists()).toBeTruthy();

    const MockedProcessListPage = wrapper.find('MockedProcessListPage');
    expect(MockedProcessListPage.exists()).toBeTruthy();
  });
  it('jobs management page test', () => {
    const wrapper = mount(
      <MemoryRouter keyLength={0} initialEntries={['/JobsManagement']}>
        <ManagementConsoleRoutes />
      </MemoryRouter>
    ).find('ManagementConsoleRoutes');

    expect(wrapper).toMatchSnapshot();
    const route = wrapper.find(Route);
    expect(route.exists()).toBeTruthy();

    const MockedJobsManagementPage = wrapper.find('MockedJobsManagementPage');
    expect(MockedJobsManagementPage.exists()).toBeTruthy();
  });

  it('no data page test', () => {
    const wrapper = mount(
      <MemoryRouter keyLength={0} initialEntries={['/NoData']}>
        <ManagementConsoleRoutes />
      </MemoryRouter>
    ).find('ManagementConsoleRoutes');

    expect(wrapper).toMatchSnapshot();
    const route = wrapper.find(Route);
    expect(route.exists()).toBeTruthy();
    const noDataComponent = wrapper.find('NoData');
    expect(noDataComponent.exists()).toBeTruthy();
  });

  it('page not found page test', () => {
    const wrapper = mount(
      <MemoryRouter keyLength={0} initialEntries={['*']}>
        <ManagementConsoleRoutes />
      </MemoryRouter>
    ).find('ManagementConsoleRoutes');

    expect(wrapper).toMatchSnapshot();
    const route = wrapper.find(Route);
    expect(route.exists()).toBeTruthy();
    const pageNotFound = wrapper.find('PageNotFound');
    expect(pageNotFound.exists()).toBeTruthy();
  });

  it('Test NoData route', () => {
    const wrapper = mount(
      <MemoryRouter keyLength={0} initialEntries={['/NoData']}>
        <ManagementConsoleRoutes />
      </MemoryRouter>
    ).find('ManagementConsoleRoutes');

    expect(wrapper).toMatchSnapshot();
    const route = wrapper.find(Route);
    expect(route.exists()).toBeTruthy();
    const noDataComponent = wrapper.find('NoData');
    expect(noDataComponent.exists()).toBeTruthy();
  });

  it('Test PageNotFound route', () => {
    const wrapper = mount(
      <MemoryRouter keyLength={0} initialEntries={['*']}>
        <ManagementConsoleRoutes />
      </MemoryRouter>
    ).find('ManagementConsoleRoutes');

    expect(wrapper).toMatchSnapshot();
    const route = wrapper.find(Route);
    expect(route.exists()).toBeTruthy();
    const pageNotFound = wrapper.find('PageNotFound');
    expect(pageNotFound.exists()).toBeTruthy();
  });
});
