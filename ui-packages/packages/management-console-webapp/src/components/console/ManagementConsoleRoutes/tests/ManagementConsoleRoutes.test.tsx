/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import React from 'react';
import { getWrapper } from '@kogito-apps/components-common';
import ManagementConsoleRoutes from '../ManagementConsoleRoutes';
import { MemoryRouter, Route } from 'react-router-dom';

jest.mock('../../../pages/ProcessListPage/ProcessListPage');
jest.mock('../../../pages/JobsManagementPage/JobsManagementPage');

const MockedComponent = (): React.ReactElement => {
  return <></>;
};

jest.mock('@kogito-apps/consoles-common', () => ({
  ...jest.requireActual('@kogito-apps/consoles-common'),
  NoData: () => {
    return <MockedComponent />;
  },
  PageNotFound: () => {
    return <MockedComponent />;
  }
}));
describe('ManagementConsoleRoutes tests', () => {
  it('Test Jobs management route', () => {
    const wrapper = getWrapper(
      <MemoryRouter keyLength={0} initialEntries={['/']}>
        <ManagementConsoleRoutes />
      </MemoryRouter>,
      'ManagementConsoleRoutes'
    );

    expect(wrapper).toMatchSnapshot();

    const route = wrapper.find(Route);
    expect(route.exists()).toBeTruthy();

    const MockedProcessListPage = wrapper.find('MockedProcessListPage');
    expect(MockedProcessListPage.exists()).toBeTruthy();
  });
  it('process list test', () => {
    const wrapper = getWrapper(
      <MemoryRouter keyLength={0} initialEntries={['/ProcessInstances']}>
        <ManagementConsoleRoutes />
      </MemoryRouter>,
      'ManagementConsoleRoutes'
    );

    expect(wrapper).toMatchSnapshot();
    const route = wrapper.find(Route);
    expect(route.exists()).toBeTruthy();

    const MockedProcessListPage = wrapper.find('MockedProcessListPage');
    expect(MockedProcessListPage.exists()).toBeTruthy();
  });
  it('jobs management page test', () => {
    const wrapper = getWrapper(
      <MemoryRouter keyLength={0} initialEntries={['/JobsManagement']}>
        <ManagementConsoleRoutes />
      </MemoryRouter>,
      'ManagementConsoleRoutes'
    );

    expect(wrapper).toMatchSnapshot();
    const route = wrapper.find(Route);
    expect(route.exists()).toBeTruthy();

    const MockedJobsManagementPage = wrapper.find('MockedJobsManagementPage');
    expect(MockedJobsManagementPage.exists()).toBeTruthy();
  });

  it('no data page test', () => {
    const wrapper = getWrapper(
      <MemoryRouter keyLength={0} initialEntries={['/NoData']}>
        <ManagementConsoleRoutes />
      </MemoryRouter>,
      'ManagementConsoleRoutes'
    );

    expect(wrapper).toMatchSnapshot();
    const route = wrapper.find(Route);
    expect(route.exists()).toBeTruthy();
    const noDataComponent = wrapper.find('NoData');
    expect(noDataComponent.exists()).toBeTruthy();
  });

  it('page not found page test', () => {
    const wrapper = getWrapper(
      <MemoryRouter keyLength={0} initialEntries={['*']}>
        <ManagementConsoleRoutes />
      </MemoryRouter>,
      'ManagementConsoleRoutes'
    );

    expect(wrapper).toMatchSnapshot();
    const route = wrapper.find(Route);
    expect(route.exists()).toBeTruthy();
    const pageNotFound = wrapper.find('PageNotFound');
    expect(pageNotFound.exists()).toBeTruthy();
  });

  it('Test NoData route', () => {
    const wrapper = getWrapper(
      <MemoryRouter keyLength={0} initialEntries={['/NoData']}>
        <ManagementConsoleRoutes />
      </MemoryRouter>,
      'ManagementConsoleRoutes'
    );

    expect(wrapper).toMatchSnapshot();
    const route = wrapper.find(Route);
    expect(route.exists()).toBeTruthy();
    const noDataComponent = wrapper.find('NoData');
    expect(noDataComponent.exists()).toBeTruthy();
  });

  it('Test PageNotFound route', () => {
    const wrapper = getWrapper(
      <MemoryRouter keyLength={0} initialEntries={['*']}>
        <ManagementConsoleRoutes />
      </MemoryRouter>,
      'ManagementConsoleRoutes'
    );

    expect(wrapper).toMatchSnapshot();
    const route = wrapper.find(Route);
    expect(route.exists()).toBeTruthy();
    const pageNotFound = wrapper.find('PageNotFound');
    expect(pageNotFound.exists()).toBeTruthy();
  });
});
