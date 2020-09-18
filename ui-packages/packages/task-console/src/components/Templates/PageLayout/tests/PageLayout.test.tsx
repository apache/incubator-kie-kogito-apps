/**
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import React from 'react';
import { MemoryRouter as Router } from 'react-router-dom';
import { getWrapper } from '@kogito-apps/common';
import PageLayout from '../PageLayout';
import taskConsoleLogo from '../../../../static/taskConsoleLogo.svg';

const props: any = {
  location: {
    pathname: '/'
  },
  history: []
};

const MockedComponent = (): React.ReactElement => {
  return <></>;
};

jest.mock('@kogito-apps/common', () => ({
  ...jest.requireActual('@kogito-apps/common'),
  KogitoPageLayout: () => {
    return <MockedComponent />;
  }
}));

jest.mock('../../DataListContainerExpandable/DataListContainerExpandable.tsx');

function testRoute(route: string) {
  props.location.pathname = route;

  const wrapper = getWrapper(
    <Router keyLength={0}>
      <PageLayout {...props} />
    </Router>,
    'PageLayout'
  );

  expect(wrapper).toMatchSnapshot();

  const mockedKogitoPageLayout = wrapper.find('KogitoPageLayout').getElement();

  expect(mockedKogitoPageLayout).not.toBeNull();

  expect(mockedKogitoPageLayout.props.BrandAltText).toBe('Task Console Logo');
  expect(mockedKogitoPageLayout.props.BrandSrc).toBe(taskConsoleLogo);
  expect(mockedKogitoPageLayout.props.PageNav).not.toBeNull();
  expect(mockedKogitoPageLayout.props.BrandClick).not.toBeNull();
}

describe('PageLayout tests', () => {
  it('test default route', () => {
    testRoute('/');
  });

  it('test TaskInbox route', () => {
    testRoute('/TaskInbox');
  });

  it('test UserTasks route', () => {
    testRoute('/UserTasks');
  });

  it('test UserTasksFilters route', () => {
    testRoute('/UserTasksFilters');
  });

  it('test TaskDetails route', () => {
    testRoute('/Task/taskID');
  });

  it('test UserTasksTable route', () => {
    testRoute('/UserTasksTable');
  });
});
