import React, { useState } from 'react';
import {
  BrowserRouter,
  NavLink,
  Redirect,
  Route,
  Switch,
  useLocation
} from 'react-router-dom';
import {
  Avatar,
  Brand,
  Nav,
  NavItem,
  NavList,
  Page,
  PageHeader,
  PageHeaderTools,
  PageSidebar
} from '@patternfly/react-core';
import AuditOverview from '../AuditOverview/AuditOverview';
import kogitoLogo from '../../../../static/images/kogitoLogo.svg';
import AuditDetail from '../AuditDetail/AuditDetail';
import imgAvatar from '../../../../static/images/user.svg';
import Breadcrumbs from '../../Organisms/Breadcrumbs/Breadcrumbs';
import NotFound from '../NotFound/NotFound';
import ApplicationError from '../ApplicationError/ApplicationError';
import { TrustyContextValue } from '../../../types';
import { datePickerSetup } from '../../Molecules/DatePicker/DatePicker';
import './TrustyApp.scss';

datePickerSetup();

type TrustyAppProps = {
  /** Enable counterfactual analysis feature */
  counterfactualEnabled: boolean;
  /** Enable explainability information inside decisions */
  explanationEnabled: boolean;
  /** Include the page layout wrapper with sidebar navigation and breadcrumbs */
  pageWrapper?: boolean;
  /** Use an optional base path for internal routes */
  basePath?: string;
  /**
   * Do not include a rect router (BrowserRouter) within TrustyApp.
   * The host application will have to include one in order to make
   * Trusty internal Routes work. */
  excludeReactRouter?: boolean;
  /**
   * Use traditional anchor links with href attributes. When set to false
   * all links will be handled with a onClick handler pushing url
   * to the browser history */
  useHrefLinks?: boolean;
};

const TrustyApp: React.FC<TrustyAppProps> = props => {
  const {
    counterfactualEnabled,
    explanationEnabled,
    pageWrapper = true,
    basePath = '',
    excludeReactRouter = false,
    useHrefLinks = true
  } = props;
  const location = useLocation();
  const [isMobileView, setIsMobileView] = useState(false);
  const [isNavOpenDesktop, setIsNavOpenDesktop] = useState(true);
  const [isNavOpenMobile, setIsNavOpenMobile] = useState(false);

  const onNavToggleDesktop = () => {
    setIsNavOpenDesktop(!isNavOpenDesktop);
  };

  const onNavToggleMobile = () => {
    setIsNavOpenMobile(!isNavOpenMobile);
  };
  console.log(`the location is`, location);
  const handlePageResize = (props: {
    windowSize: number;
    mobileView: boolean;
  }) => {
    // closing sidebar menu when resolution is < 1200
    if (props.windowSize < 1200) {
      if (!isMobileView) setIsMobileView(true);
    } else {
      if (isMobileView) setIsMobileView(false);
    }
  };

  const PageNav = (
    <Nav aria-label="Nav" theme="dark">
      <NavList>
        <NavItem
          isActive={location.pathname.startsWith('/audit')}
          ouiaId="audit-item"
        >
          <NavLink to="/audit">Audit investigation</NavLink>
        </NavItem>
      </NavList>
    </Nav>
  );

  const Sidebar = (
    <PageSidebar
      nav={PageNav}
      isNavOpen={isMobileView ? isNavOpenMobile : isNavOpenDesktop}
      theme="dark"
    />
  );

  const Header = (
    <PageHeader
      logo={
        <Brand src={kogitoLogo} alt="Kogito TrustyAI" className="trusty-logo" />
      }
      logoProps={{ href: '#/' }}
      headerTools={
        <PageHeaderTools>
          <Avatar src={imgAvatar} alt="Avatar image" />
        </PageHeaderTools>
      }
      showNavToggle={isMobileView}
      onNavToggle={isMobileView ? onNavToggleMobile : onNavToggleDesktop}
      isNavOpen={isMobileView ? isNavOpenMobile : isNavOpenDesktop}
    />
  );

  const Routes = (
    <Switch>
      <Route exact path={`${basePath}/`}>
        <Redirect to={`${basePath}/audit`} />
      </Route>
      <Route exact path={`${basePath}/audit`}>
        <AuditOverview />
      </Route>
      <Route path={`${basePath}/audit/:executionType/:executionId`}>
        <AuditDetail />
      </Route>
      <Route exact path="/error">
        <ApplicationError />
      </Route>
      <Route path="/not-found" component={NotFound} />
      <Redirect to="/not-found" />
    </Switch>
  );

  const Routing = (
    <>
      {excludeReactRouter ? (
        <>{Routes}</>
      ) : (
        <BrowserRouter>{Routes}</BrowserRouter>
      )}
    </>
  );

  return (
    <TrustyContext.Provider
      value={{
        config: {
          counterfactualEnabled,
          explanationEnabled,
          basePath: basePath,
          useHrefLinks
        }
      }}
    >
      {pageWrapper ? (
        <Page
          header={Header}
          sidebar={Sidebar}
          breadcrumb={<Breadcrumbs />}
          onPageResize={handlePageResize}
        >
          {Routing}
        </Page>
      ) : (
        <>{Routing}</>
      )}
    </TrustyContext.Provider>
  );
};

export default TrustyApp;

export const TrustyContext = React.createContext<TrustyContextValue>(null);
