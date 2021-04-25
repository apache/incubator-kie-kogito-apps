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
import { BrowserRouter, Route, Switch } from 'react-router-dom';
import { ApolloProvider } from 'react-apollo';
import { ApolloClient } from 'apollo-client';

import {
  KogitoAppContextProvider,
  UserContext,
  PageLayout
} from '@kogito-apps/consoles-common';
import ManagementConsoleNav from '../ManagementConsoleNav/ManagementConsoleNav';
import managementConsoleLogo from '../../../static/managementConsoleLogo.svg';
import JobsManagementContextProvider from '../../../channel/JobsManagement/JobsManagementContextProvider';

interface IOwnProps {
  apolloClient: ApolloClient<any>;
  userContext: UserContext;
  children: React.ReactElement;
}

const ManagementConsole: React.FC<IOwnProps> = ({
  apolloClient,
  userContext,
  children
}) => {
  const renderPage = routeProps => {
    return (
      <PageLayout
        BrandSrc={managementConsoleLogo}
        pageNavOpen={true}
        BrandAltText={'Management Console Logo'}
        BrandClick={() => routeProps.history.push('/')}
        PageNav={
          <ManagementConsoleNav pathname={routeProps.location.pathname} />
        }
      >
        {children}
      </PageLayout>
    );
  };

  return (
    <ApolloProvider client={apolloClient}>
      <KogitoAppContextProvider userContext={userContext}>
        <JobsManagementContextProvider apolloClient={apolloClient}>
          <BrowserRouter>
            <Switch>
              <Route path="/" render={renderPage} />
            </Switch>
          </BrowserRouter>
        </JobsManagementContextProvider>
      </KogitoAppContextProvider>
    </ApolloProvider>
  );
};

export default ManagementConsole;
