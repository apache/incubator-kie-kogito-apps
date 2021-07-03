import React, { useState } from 'react';
import {
  Dropdown,
  DropdownToggle,
  DropdownItem,
  Text,
  TextVariants
} from '@patternfly/react-core';
import { UserIcon } from '@patternfly/react-icons';
import { useDevUIAppContext } from '../../../contexts/DevUIAppContext';

interface IOwnProps {
  user: string;
}

const TaskInboxSwitchUser: React.FC<IOwnProps> = ({ user }) => {
  const appContext = useDevUIAppContext();
  const [isOpen, setIsOpen] = useState(false);
  const [currentUser, setCurrentUser] = useState(user);
  const allUsers = appContext.getAllUsers();

  const onSelect = (event): void => {
    const selectedUser = event.target.innerHTML;
    appContext.switchUser(selectedUser);
    setCurrentUser(selectedUser);
    setIsOpen(!isOpen);
  };

  const dropdownItems = (): JSX.Element[] => {
    const userIds = [];
    allUsers.map(user => {
      userIds.push(<DropdownItem key={user.id}>{user.id}</DropdownItem>);
    });
    return userIds;
  };

  const onToggle = (isOpen): void => {
    setIsOpen(isOpen);
  };

  return (
    <Dropdown
      onSelect={onSelect}
      toggle={
        <DropdownToggle
          toggleIndicator={null}
          onToggle={onToggle}
          aria-label="Applications"
          id="toggle-id-7"
        >
          <UserIcon />
          <Text component={TextVariants.p}>{currentUser}</Text>
        </DropdownToggle>
      }
      isOpen={isOpen}
      isPlain
      dropdownItems={dropdownItems()}
    />
  );
};

export default TaskInboxSwitchUser;
