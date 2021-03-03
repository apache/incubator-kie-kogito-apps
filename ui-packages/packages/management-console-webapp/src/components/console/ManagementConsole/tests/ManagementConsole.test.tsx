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
import ManagementConsole from '../ManagementConsole';
import ManagementConsoleRoutes from '../../ManagementConsoleRoutes/ManagementConsoleRoutes';
import { ApolloClient } from 'apollo-client';

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

describe('ManagementConsole tests', () => {
  it('Snapshot test with default props', () => {
    // @ts-ignore
    const client = new ApolloClientMock();
    const props = {
      apolloClient: client,
      userContext: { getCurrentUser: jest.fn() }
    };
    const wrapper = getWrapper(
      <ManagementConsole {...props}>
        <ManagementConsoleRoutes />
      </ManagementConsole>,
      'ManagementConsole'
    );

    expect(wrapper).toMatchSnapshot();
  });
});
