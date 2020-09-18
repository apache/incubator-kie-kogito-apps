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
import PageLayout from '../PageLayout';
import { MockedProvider } from '@apollo/react-testing';
import { getWrapper, GraphQL } from '@kogito-apps/common';
import { MemoryRouter as Router } from 'react-router-dom';
import useGetQueryFieldsQuery = GraphQL.useGetQueryFieldsQuery;

const props: any = {
  location: {
    pathname: '/ProcessInstances'
  },
  history: []
};

const mocks = [];

jest.mock('../../ProcessListPage/ProcessListPage.tsx');

jest.mock('@kogito-apps/common/src/graphql/types');
const MockedComponent = (): React.ReactElement => {
  return <></>;
};
jest.mock('@kogito-apps/common', () => ({
  ...jest.requireActual('@kogito-apps/common'),
  KogitoPageLayout: () => {
    return <MockedComponent />;
  },
  GraphQL: {
    useGetQueryFieldsQuery: jest.fn()
  }
}));
describe('PageLayout tests', () => {
  // @ts-ignore
  useGetQueryFieldsQuery.mockReturnValue({
    loading: false,
    data: {
      __type: {
        fields: [
          {
            name: 'Travels'
          },
          {
            name: 'visaApplication'
          },
          {
            name: 'Jobs'
          }
        ]
      }
    }
  });
  it('snapshot testing', () => {
    const wrapper = getWrapper(
      // keyLength set to zero to have stable snapshots
      <Router keyLength={0}>
        <MockedProvider mocks={mocks}>
          <PageLayout {...props} />
        </MockedProvider>
      </Router>,
      'PageLayout'
    );
    wrapper
      .find('KogitoPageLayout')
      .props()
      [
        // tslint:disable-next-line
        'BrandClick'
      ]();
    expect(wrapper).toMatchSnapshot();
  });
});
