import React from 'react';
import PageLayout from '../PageLayout';
import { getWrapper } from '@kogito-apps/common';
import { MemoryRouter as Router } from 'react-router-dom';

const props: any = {
  location: {
    pathname: '/'
  },
  history: []
};

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

  return wrapper;
}

describe('PageLayout tests', () => {
  it('test default route', () => {
    testRoute('/');
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

  it('Brand click testing', () => {
    const wrapper = testRoute('/');
    wrapper
      .find('KogitoPageLayout')
      .props()
      [
        // tslint:disable-next-line
        'BrandClick'
      ]();
  });
});
