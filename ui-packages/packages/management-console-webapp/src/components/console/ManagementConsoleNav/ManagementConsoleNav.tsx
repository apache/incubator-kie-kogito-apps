import React from 'react';
import {
  Nav,
  NavItem,
  NavList
} from '@patternfly/react-core/dist/js/components/Nav';
import { Link } from 'react-router-dom';
import { ouiaAttribute } from '@kogito-apps/ouia-tools/dist/utils/OuiaUtils';

interface IOwnProps {
  pathname: string;
}

const ManagementConsoleNav: React.FC<IOwnProps> = ({ pathname }) => {
  return (
    <Nav aria-label="Nav" theme="dark" ouiaId="navigation-list">
      <NavList>
        <NavItem
          key={'process-instances-nav'}
          isActive={pathname === '/ProcessInstances'}
          ouiaId="process-instances"
        >
          <Link
            to="/ProcessInstances"
            {...ouiaAttribute('data-ouia-navigation-name', 'process-instances')}
          >
            Process Instances
          </Link>
        </NavItem>
        <NavItem
          key={'jobs-management-nav'}
          isActive={pathname === '/JobsManagement'}
          ouiaId="jobs-management"
        >
          <Link
            to="/JobsManagement"
            {...ouiaAttribute('data-ouia-navigation-name', 'jobs-management')}
          >
            Jobs Management
          </Link>
        </NavItem>
      </NavList>
    </Nav>
  );
};

export default ManagementConsoleNav;
