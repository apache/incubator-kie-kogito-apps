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
import { shallow } from 'enzyme';
import { BrowserRouter } from 'react-router-dom';
import { MockedProvider } from '@apollo/react-testing';
import DomainExplorerPage from '../DomainExplorerPage';
import { getWrapper } from '@kogito-apps/common';

const MockedDomainExplorer = (): React.ReactElement => {
  return <></>;
};
jest.mock('@kogito-apps/common', () => ({
  ...jest.requireActual('@kogito-apps/common'),
  DomainExplorer: () => {
    return <MockedDomainExplorer />;
  }
}));
const MockedBreadcrumb = (): React.ReactElement => {
  return <></>;
};

jest.mock('@patternfly/react-core', () => ({
  ...jest.requireActual('@patternfly/react-core'),
  Breadcrumb: () => <MockedBreadcrumb />
}));
const props = {
  domains: ['Travels', 'VisaApplications'],
  loadingState: false
};

const routeComponentPropsMock = {
  history: {} as any,
  location: {
    pathname: '/DomainExplorer/Travels',
    state: {
      parameters: [{ flight: ['arrival'] }, { flight: ['departure'] }]
    }
  } as any,
  match: {
    params: {
      domainName: 'Travels'
    }
  } as any
};
const routeComponentPropsMock2 = {
  history: {} as any,
  location: {
    pathname: '/DomainExplorer/Travels',
    state: {}
  } as any,
  match: {
    params: {
      domainName: 'Travels'
    }
  } as any
};
const props2 = {
  domains: ['Travels', 'VisaApplications'],
  location: {
    pathname: '/DomainExplorer/Travels',
    state: {}
  },
  match: {
    params: {
      domainName: 'Travels'
    }
  },
  loadingState: false
};

describe('DomainExplorerPage component', () => {
  it('Snapshot with default props', () => {
    const wrapper = getWrapper(
      <MockedProvider mocks={[]} addTypename={false}>
        <BrowserRouter>
          <DomainExplorerPage {...props} {...routeComponentPropsMock} />
        </BrowserRouter>
      </MockedProvider>,
      'DomainExplorerPage'
    );

    wrapper.update();
    expect(wrapper).toMatchSnapshot();
  });
  it('Check error response for getQueryFields query', async () => {
    const wrapper = getWrapper(
      <BrowserRouter>
        <MockedProvider mocks={[]} addTypename={false}>
          <DomainExplorerPage {...props} {...routeComponentPropsMock} />
        </MockedProvider>
      </BrowserRouter>,
      'DomainExplorerPage'
    );
    wrapper.update();
    expect(wrapper).toMatchSnapshot();
  });
  it('Mock query testing', async () => {
    const wrapper = getWrapper(
      <BrowserRouter>
        <MockedProvider mocks={[]} addTypename={false}>
          <DomainExplorerPage {...props} {...routeComponentPropsMock} />
        </MockedProvider>
      </BrowserRouter>,
      'DomainExplorerPage'
    );
    wrapper.update();
    expect(wrapper).toMatchSnapshot();
  });
  it('Check error response for getPicker query', () => {
    const wrapper = shallow(
      <BrowserRouter>
        <DomainExplorerPage {...props} {...routeComponentPropsMock} />
      </BrowserRouter>
    );
    wrapper.update();
    wrapper.setProps({});
    expect(wrapper).toMatchSnapshot();
  });
  it('Check error response for getQueryTypes', () => {
    const wrapper = shallow(
      <BrowserRouter>
        <DomainExplorerPage {...props} {...routeComponentPropsMock} />
      </BrowserRouter>
    );
    wrapper.update();
    wrapper.setProps({});
    expect(wrapper).toMatchSnapshot();
  });
  it('check assertions on rememberedParams', () => {
    const wrapper = getWrapper(
      <BrowserRouter>
        <MockedProvider mocks={[]} addTypename={false}>
          <DomainExplorerPage {...props2} {...routeComponentPropsMock2} />
        </MockedProvider>
      </BrowserRouter>,
      'DomainExplorerPage'
    );
    wrapper.update();
    expect(wrapper).toMatchSnapshot();
  });
});
