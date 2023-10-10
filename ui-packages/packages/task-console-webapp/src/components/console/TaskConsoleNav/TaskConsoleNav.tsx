import React from 'react';
import {
  Nav,
  NavItem,
  NavList
} from '@patternfly/react-core/dist/js/components/Nav';
import { Link } from 'react-router-dom';
import { ouiaAttribute } from '@kogito-apps/ouia-tools';

interface Props {
  pathname?: string;
}

const TaskConsoleNav: React.FC<Props> = ({ pathname }) => {
  return (
    <Nav aria-label="Nav" theme="dark">
      <NavList>
        <NavItem key={'task-inbox-nav'} isActive={pathname === '/TaskInbox'}>
          <Link
            to="/TaskInbox"
            {...ouiaAttribute('data-ouia-navigation-name', 'task-inbox')}
          >
            Task Inbox
          </Link>
        </NavItem>
      </NavList>
    </Nav>
  );
};

export default TaskConsoleNav;
