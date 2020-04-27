import {
  Nav,
  NavList,
  NavItem,
} from '@patternfly/react-core';
import React from 'react';
import { Redirect, Route, Link, Switch } from 'react-router-dom';
import { PageLayout } from '@kogito-apps/common/src/components';
import DataListContainer from '../DataListContainer/DataListContainer';
import ProcessDetailsPage from '../ProcessDetailsPage/ProcessDetailsPage';
import DomainExplorerDashboard from '../DomainExplorerDashboard/DomainExplorerDashboard';
import DomainExplorerLandingPage from '../DomainExplorerLandingPage/DomainExplorerLandingPage';
import ErrorComponent from '../../Molecules/ErrorComponent/ErrorComponent';
import NoDataComponent from '../../Molecules/NoDataComponent/NoDataComponent';
import './Dashboard.css';
import managementConsoleLogo from '../../../static/managementConsoleLogo.svg';

import { useGetQueryFieldsQuery } from '../../../graphql/types';

const Dashboard: React.FC<{}> = (props: any) => {
  const { pathname } = props.location;


  const PageNav = (
    <Nav aria-label="Nav" theme="dark">
      <NavList>
        <NavItem isActive={pathname === '/ProcessInstances'}>
          <Link to="/ProcessInstances">Process Instances</Link>
        </NavItem>
        <NavItem isActive={pathname === '/DomainExplorer'}>
          <Link to="/DomainExplorer">Domain Explorer</Link>
        </NavItem>
      </NavList>
    </Nav>
  );

  const BrandClick = () => {
    props.history.push('/ProcessInstances');
  };

  const getQuery = useGetQueryFieldsQuery();
  const availableDomains =
    !getQuery.loading && getQuery.data.__type.fields.slice(2);
  const domains = [];
  availableDomains && availableDomains.map(item => domains.push(item.name));
  return (

    <PageLayout PageNav={PageNav}
      BrandSrc={managementConsoleLogo}
      BrandAltText="Task Consol Logo"
      BrandClick={BrandClick}
    >
      <Switch>
        <Route
          exact
          path="/"
          render={() => <Redirect to="/ProcessInstances" />}
        />
        <Route exact path="/ProcessInstances" component={DataListContainer} />
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
            <DomainExplorerDashboard {..._props} domains={domains} />
          )}
        />
        <Route path="/NoData" component={NoDataComponent} />
        <Route path="*" component={ErrorComponent} />
      </Switch> {' '}
    </PageLayout>
  );
};

export default Dashboard;
