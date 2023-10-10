import React from 'react';
import { Redirect, Route, Switch } from 'react-router-dom';
import {
  JobsManagementPage,
  ProcessListPage,
  ProcessDetailsPage
} from '../../pages';
import { PageNotFound } from '@kogito-apps/consoles-common/dist/components/pages/PageNotFound';
import { NoData } from '@kogito-apps/consoles-common/dist/components/pages/NoData';

const ManagementConsoleRoutes: React.FC = () => {
  return (
    <Switch>
      <Route
        exact
        path="/"
        render={() => <Redirect to="/ProcessInstances" />}
      />
      <Route exact path="/ProcessInstances" component={ProcessListPage} />
      <Route exact path="/JobsManagement" component={JobsManagementPage} />
      <Route exact path="/Process/:instanceID" component={ProcessDetailsPage} />
      <Route
        path="/NoData"
        render={(_props) => (
          <NoData
            {..._props}
            defaultPath="/JobsManagement"
            defaultButton="Go to jobs management"
          />
        )}
      />
      <Route
        path="*"
        render={(_props) => (
          <PageNotFound
            {..._props}
            defaultPath="/JobsManagement"
            defaultButton="Go to jobs management"
          />
        )}
      />
    </Switch>
  );
};

export default ManagementConsoleRoutes;
