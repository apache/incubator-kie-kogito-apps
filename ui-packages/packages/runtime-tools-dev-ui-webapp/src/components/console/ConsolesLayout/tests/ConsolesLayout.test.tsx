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
import { mount } from 'enzyme';
import ConsolesLayout from '../ConsolesLayout';
import ConsolesRoutes from '../../ConsolesRoutes/ConsolesRoutes';
import { ApolloClient } from 'apollo-client';
import { MemoryRouter } from 'react-router-dom'

const MockedComponent = (): React.ReactElement => {
  return <></>;
};

jest.mock('@kogito-apps/consoles-common', () => ({
  ...jest.requireActual('@kogito-apps/consoles-common'),
  PageLayout: () => {
    return <MockedComponent />;
  }
}));

jest.mock('apollo-client');
const ApolloClientMock = ApolloClient as jest.MockedClass<typeof ApolloClient>;

describe('ConsolesLayout tests', () => {
  it('Snapshot test with default props', () => {
    // @ts-ignore
    const client = new ApolloClientMock();
    const props = {
      apolloClient: client,
      userContext: { getCurrentUser: jest.fn() }
    };
    const wrapper = mount(
      <MemoryRouter initialEntries={['/']} keyLength={0}>
      <ConsolesLayout {...props}>
        <ConsolesRoutes />
      </ConsolesLayout>
      </MemoryRouter>
    );
    expect(wrapper.find('ConsolesLayout')).toMatchSnapshot();
  });
});
