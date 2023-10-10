import React from 'react';
import { BrowserRouter, Route, Switch } from 'react-router-dom';
import { ApolloProvider } from 'react-apollo';
import { ApolloClient } from 'apollo-client';
import { PageLayout } from '@kogito-apps/consoles-common/dist/components/layout/PageLayout';
import { KogitoAppContextProvider } from '@kogito-apps/consoles-common/dist/environment/context';
import { UserContext } from '@kogito-apps/consoles-common/dist/environment/auth';
import ManagementConsoleNav from '../ManagementConsoleNav/ManagementConsoleNav';
import managementConsoleLogo from '../../../static/managementConsoleLogo.svg';
import JobsManagementContextProvider from '../../../channel/JobsManagement/JobsManagementContextProvider';
import ProcessDetailsContextProvider from '../../../channel/ProcessDetails/ProcessDetailsContextProvider';
import ProcessListContextProvider from '../../../channel/ProcessList/ProcessListContextProvider';

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
  const renderPage = (routeProps) => {
    return (
      <PageLayout
        BrandSrc={managementConsoleLogo}
        pageNavOpen={true}
        BrandAltText={'Management Console Logo'}
        BrandClick={() => routeProps.history.push('/')}
        withHeader={true}
        PageNav={
          <ManagementConsoleNav pathname={routeProps.location.pathname} />
        }
        ouiaId="management-console"
      >
        {children}
      </PageLayout>
    );
  };

  return (
    <ApolloProvider client={apolloClient}>
      <KogitoAppContextProvider userContext={userContext}>
        <ProcessListContextProvider apolloClient={apolloClient}>
          <ProcessDetailsContextProvider apolloClient={apolloClient}>
            <JobsManagementContextProvider apolloClient={apolloClient}>
              <BrowserRouter>
                <Switch>
                  <Route path="/" render={renderPage} />
                </Switch>
              </BrowserRouter>
            </JobsManagementContextProvider>
          </ProcessDetailsContextProvider>
        </ProcessListContextProvider>
      </KogitoAppContextProvider>
    </ApolloProvider>
  );
};

export default ManagementConsole;
