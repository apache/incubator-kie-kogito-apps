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
import DomainExplorerLandingPage from '../DomainExplorerLandingPage';
import { MemoryRouter as Router } from 'react-router-dom';
import { MockedProvider } from '@apollo/react-testing';
import { getWrapper } from '@kogito-apps/common';

const MockedDomainExplorerListDomains = (): React.ReactElement => {
  return <></>;
};
jest.mock('@kogito-apps/common', () => ({
  ...jest.requireActual('@kogito-apps/common'),
  DomainExplorerListDomains: () => {
    return <MockedDomainExplorerListDomains />;
  }
}));

const MockedBreadcrumb = (): React.ReactElement => {
  return <></>;
};

jest.mock('@patternfly/react-core', () => ({
  ...jest.requireActual('@patternfly/react-core'),
  Breadcrumb: () => <MockedBreadcrumb />
}));

describe('Domain Explorer Landing Page Component', () => {
  const props = {
    ouiaId: null,
    ouiaSafe: true
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
