import React from 'react';
import DomainExplorerLandingPage from '../DomainExplorerLandingPage';
import { MemoryRouter as Router } from 'react-router-dom';
import { MockedProvider } from '@apollo/react-testing';
import { getWrapper } from '@kogito-apps/common';

describe('Domain Explorer Landing Page Component', () => {
  const props = {
    ouiaContext: {
      isOuia: false,
      ouiaId: null
    } as any
  };
  it('Snapshot test with default props', () => {
    const wrapper = getWrapper(
      <MockedProvider mocks={[]} addTypename={false}>
        <Router keyLength={0}>
          <DomainExplorerLandingPage {...props} />
        </Router>
      </MockedProvider>,
      'DomainExplorerLandingPage'
    );
    expect(wrapper).toMatchSnapshot();
  });
});
