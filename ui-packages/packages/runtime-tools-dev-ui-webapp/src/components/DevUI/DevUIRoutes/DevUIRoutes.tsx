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

import React, { useMemo } from 'react';
import { Redirect, Route, Switch } from 'react-router-dom';
import { JobsManagementPage, ProcessesPage } from '../../pages';
import { NoData, PageNotFound } from '@kogito-apps/consoles-common';
import ProcessDetailsPage from '../../pages/ProcessDetailsPage/ProcessDetailsPage';
import TaskInboxPage from '../../pages/TaskInboxPage/TaskInboxPage';
import TaskDetailsPage from '../../pages/TaskDetailsPage/TaskDetailsPage';
import FormsListPage from '../../pages/FormsListPage/FormsListPage';
import FormDetailPage from '../../pages/FormDetailsPage/FormDetailsPage';
import { TrustyApp } from '@kogito-apps/trusty';
import ProcessFormPage from '../../pages/ProcessFormPage/ProcessFormPage';
import { useDevUIAppContext } from '../../contexts/DevUIAppContext';

interface IOwnProps {
  trustyServiceUrl: string;
  navigate: string;
}

const DevUIRoutes: React.FC<IOwnProps> = ({ trustyServiceUrl, navigate }) => {
  const { isProcessEnabled, isTracingEnabled } = useDevUIAppContext();

  const defaultPath = useMemo(() => {
    if (isProcessEnabled) {
      return '/JobsManagement';
    }
    if (isTracingEnabled) {
      return '/Audit';
    }
  }, [isProcessEnabled, isTracingEnabled]);

  const defaultButton = useMemo(() => {
    if (isProcessEnabled) {
      return 'Go to jobs management';
    }
    if (isTracingEnabled) {
      return 'Go to audit';
    }
  }, [isProcessEnabled, isTracingEnabled]);

  return (
    <Switch>
      <Route exact path="/" render={() => <Redirect to={`/${navigate}`} />} />
      {isProcessEnabled && (
        <Route exact path="/Processes" component={ProcessesPage} />
      )}
      {isProcessEnabled && (
        <Route
          exact
          path="/Process/:instanceID"
          component={ProcessDetailsPage}
        />
      )}
      {isProcessEnabled && (
        <Route exact path="/JobsManagement" component={JobsManagementPage} />
      )}
      {isProcessEnabled && (
        <Route exact path="/TaskInbox" component={TaskInboxPage} />
      )}
      {isProcessEnabled && (
        <Route exact path="/Forms" component={FormsListPage} />
      )}
      {isProcessEnabled && (
        <Route exact path="/Forms/:formName" component={FormDetailPage} />
      )}
      {isProcessEnabled && (
        <Route
          exact
          path="/ProcessDefinition/Form/:processName"
          component={ProcessFormPage}
        />
      )}
      {isProcessEnabled && (
        <Route
          exact
          path="/TaskDetails/:taskId"
          render={routeProps => <TaskDetailsPage {...routeProps} />}
        />
      )}
      {isTracingEnabled && (
        <Route path="/Audit">
          <TrustyApp
            counterfactualEnabled={false}
            explanationEnabled={false}
            containerConfiguration={{
              pageWrapper: false,
              serverRoot: trustyServiceUrl,
              basePath: '/Audit',
              excludeReactRouter: true,
              useHrefLinks: false
            }}
          />
        </Route>
      )}
      <Route
        path="/NoData"
        render={_props => (
          <NoData
            {..._props}
            defaultPath={defaultPath}
            defaultButton={defaultButton}
          />
        )}
      />
      <Route
        path="*"
        render={_props => (
          <PageNotFound
            {..._props}
            defaultPath={defaultPath}
            defaultButton={defaultButton}
          />
        )}
      />
    </Switch>
  );
};

export default DevUIRoutes;
