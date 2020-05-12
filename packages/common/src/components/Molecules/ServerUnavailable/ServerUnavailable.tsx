import React, { useState } from 'react';
import {
  PageSection,
  Bullseye,
  EmptyState,
  EmptyStateIcon,
  EmptyStateVariant,
  Button,
  EmptyStateBody,
  Title,
  Page,
  PageSidebar,
  PageHeader,
  Avatar,
  Brand,
  InjectedOuiaProps,
  withOuiaContext
} from '@patternfly/react-core';
import { ExclamationCircleIcon } from '@patternfly/react-icons';
import { BrowserRouter as Router } from 'react-router-dom';
import { aboutLogoContext } from '../../contexts';
import PageToolbar from '../PageToolbar/PageToolbar';
import userImage from '../../../static/avatar.svg';
import './ServerUnavailable.css';
import { componentOuiaProps } from '../../../utils/OuiaUtils';

interface IOwnProps {
  src: any,
  PageNav: any
}

const ServerUnavailable: React.FC<IOwnProps & InjectedOuiaProps> = ({
  ouiaContext,
  ouiaId,
  ...props
}) => {
  const [isNavOpen, setIsNavOpen] = useState(true);
  const onNavToggle = () => {
    setIsNavOpen(!isNavOpen);
  };

  const Header = (
    <PageHeader
      logo={<Brand src={props.src} alt="Management Console Logo" />}
      toolbar={
        <aboutLogoContext.Provider value={props.src}>
          <PageToolbar />
        </aboutLogoContext.Provider>
      }
      avatar={<Avatar src={userImage} alt="Kogito Logo" />}
      showNavToggle
      isNavOpen={isNavOpen}
      onNavToggle={onNavToggle}
    />
  );

  const Sidebar = (
    <PageSidebar nav={props.PageNav} isNavOpen={isNavOpen} theme="dark" />
  );

  const pageId = 'main-content-page-layout-default-nav';

  return (
    <>
      <Router>
        <Page
          header={Header}
          mainContainerId={pageId}
          sidebar={Sidebar}
          isManagedSidebar
          className="kogito-common--PageLayout"
        >
          <PageSection variant="light"
            {...componentOuiaProps(ouiaContext, ouiaId, 'ServerUnavailable', true)}>
            <Bullseye>
              <EmptyState variant={EmptyStateVariant.full}>
                <EmptyStateIcon
                  icon={ExclamationCircleIcon}
                  size="md"
                  color="var(--pf-global--danger-color--100)"
                />
                <Title headingLevel="h1" size="4xl">
                  Error connecting server
                </Title>
                <EmptyStateBody>
                  {`The ${process.env.KOGITO_APP_NAME} could not access the server to display content.`}
                </EmptyStateBody>
                <EmptyStateBody>
                  Try reloading the page, or contact your administrator for more
                  information.
                </EmptyStateBody>
                <Button
                  variant="primary"
                  onClick={() => window.location.reload()}
                >
                  Refresh
                </Button>
              </EmptyState>
            </Bullseye>
          </PageSection>
        </Page>
      </Router>
    </>
  );
};

export default withOuiaContext(ServerUnavailable);
