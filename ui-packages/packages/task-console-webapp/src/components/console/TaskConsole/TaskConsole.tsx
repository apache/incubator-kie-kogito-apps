import React from 'react';
import { BrowserRouter, Route, Switch } from 'react-router-dom';
import { ApolloClient } from 'apollo-client';
import { PageLayout } from '@kogito-apps/consoles-common/dist/components/layout/PageLayout';
import { KogitoAppContextProvider } from '@kogito-apps/consoles-common/dist/environment/context';
import { UserContext } from '@kogito-apps/consoles-common/dist/environment/auth';

import TaskConsoleContextsProvider from '../../../context/TaskConsoleContext/TaskConsoleContextsProvider';
import taskConsoleLogo from '../../../static/taskConsoleLogo.svg';
import TaskConsoleNav from '../TaskConsoleNav/TaskConsoleNav';

interface Props {
  apolloClient: ApolloClient<any>;
  userContext: UserContext;
  children: React.ReactElement;
}

const TaskConsole: React.FC<Props> = ({
  apolloClient,
  userContext,
  children
}) => {
  const renderPage = (routeProps) => {
    return (
      <PageLayout
        BrandSrc={taskConsoleLogo}
        pageNavOpen={false}
        BrandAltText={'Task Console Logo'}
        BrandClick={() => routeProps.history.push('/')}
        withHeader={true}
        PageNav={<TaskConsoleNav pathname={routeProps.location.pathname} />}
      >
        {children}
      </PageLayout>
    );
  };

  return (
    <KogitoAppContextProvider userContext={userContext}>
      <TaskConsoleContextsProvider client={apolloClient}>
        <BrowserRouter>
          <Switch>
            <Route path="/" render={renderPage} />
          </Switch>
        </BrowserRouter>
      </TaskConsoleContextsProvider>
    </KogitoAppContextProvider>
  );
};

export default TaskConsole;
