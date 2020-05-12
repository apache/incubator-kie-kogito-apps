import React from 'react';
import PageLayoutComponent from '../PageLayoutComponent';
import { MockedProvider } from '@apollo/react-testing';
import { getWrapperAsync } from '@kogito-apps/common'
import { MemoryRouter as Router } from 'react-router-dom';


const props: any = {
  location: {
    pathname: '/ProcessInstances'
  }
};

const mocks = [];

jest.mock('../../DataListContainer/DataListContainer.tsx');
jest.mock('../../ProcessDetailsPage/ProcessDetailsPage');
jest.mock('../../DomainExplorerDashboard/DomainExplorerDashboard');
jest.mock('../../DomainExplorerLandingPage/DomainExplorerLandingPage');

describe('PageLayoutComponent component tests', () => {
  it('snapshot testing', async () => {
    const wrapper = await getWrapperAsync(
      // keyLength set to zero to have stable snapshots
      <MockedProvider mocks={mocks}>
        <Router keyLength={0}>
          <PageLayoutComponent {...props} />
        </Router>
      </MockedProvider>
      , 'PageLayoutComponent');
    expect(wrapper).toMatchSnapshot();
  });
});
