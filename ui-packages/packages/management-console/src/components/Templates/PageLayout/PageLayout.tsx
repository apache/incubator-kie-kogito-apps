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
import { Nav, NavList, NavItem } from '@patternfly/react-core';
import React from 'react';
import { Redirect, Route, Link, Switch } from 'react-router-dom';
import {
  KogitoPageLayout,
  PageNotFound,
  NoData,
  ouiaAttribute,
  GraphQL,
  OUIAProps
} from '@kogito-apps/common';
import ProcessListPage from '../ProcessListPage/ProcessListPage';
import ProcessDetailsPage from '../ProcessDetailsPage/ProcessDetailsPage';
import DomainExplorerPage from '../DomainExplorerPage/DomainExplorerPage';
import DomainExplorerLandingPage from '../DomainExplorerLandingPage/DomainExplorerLandingPage';
import './PageLayout.css';
import managementConsoleLogo from '../../../static/managementConsoleLogo.svg';
import { History, Location } from 'history';

interface IOwnProps {
  location: Location;
  history: History;
}

const PageLayout: React.FC<IOwnProps & OUIAProps> = ({ ouiaId, ...props }) => {
  const { pathname } = props.location;

  const PageNav = (
    <Nav aria-label="Nav" theme="dark">
      <NavList>
        <NavItem isActive={pathname === '/ProcessInstances'}>
          <Link
            to="/ProcessInstances"
            {...ouiaAttribute('data-ouia-navigation-name', 'process-instances')}
          >
            Process Instances
          </Link>
        </NavItem>
        <NavItem isActive={pathname === '/DomainExplorer'}>
          <Link
            to="/DomainExplorer"
            {...ouiaAttribute('data-ouia-navigation-name', 'domain-explorer')}
          >
            Domain Explorer
          </Link>
        </NavItem>
      </NavList>
    </Nav>
  );

  const BrandClick = () => {
    props.history.push('/ProcessInstances');
  };
  const getQuery = GraphQL.useGetQueryFieldsQuery();
  const availableDomains =
    !getQuery.loading && getQuery.data && getQuery.data.__type.fields.slice(2);
  const domains = [];
  availableDomains && availableDomains.map(item => domains.push(item.name));
  return (
    <React.Fragment>
      <KogitoPageLayout
        PageNav={PageNav}
        BrandSrc={managementConsoleLogo}
        BrandAltText="Management Console Logo"
        BrandClick={BrandClick}
      >
        <Switch>
          <Route
            exact
            path="/"
            render={() => <Redirect to="/ProcessInstances" />}
          />
          <Route exact path="/ProcessInstances" component={ProcessListPage} />
          <Route
            exact
            path="/Process/:instanceID"
            component={ProcessDetailsPage}
          />
          <Route
            exact
            path="/DomainExplorer"
            component={DomainExplorerLandingPage}
          />
          <Route
            exact
            path="/DomainExplorer/:domainName"
            render={_props => (
              <DomainExplorerPage
                {..._props}
                domains={domains}
                loadingState={getQuery.loading}
              />
            )}
          />
          <Route
            path="/NoData"
            render={_props => (
              <NoData
                {..._props}
                defaultPath="/ProcessInstances"
                defaultButton="Go to process instances"
              />
            )}
          />
          <Route
            path="*"
            render={_props => (
              <PageNotFound
                {..._props}
                defaultPath="/ProcessInstances"
                defaultButton="Go to process instances"
              />
            )}
          />
        </Switch>
      </KogitoPageLayout>
    </React.Fragment>
  );
};

export default PageLayout;
