import React, { useState } from 'react';
import { Dropdown, DropdownToggle, DropdownItem } from '@patternfly/react-core';
import { UserIcon, CaretDownIcon } from '@patternfly/react-icons';
import { useDevUIAppContext } from '../../../contexts/DevUIAppContext';
import '../../../styles.css';

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
          onToggle={onToggle}
          aria-label="Applications"
          id="toggle-id-7"
          toggleIndicator={CaretDownIcon}
          icon={<UserIcon />}
        >
          {currentUser}
        </DropdownToggle>
      }
      isOpen={isOpen}
      isPlain
      dropdownItems={dropdownItems()}
      className="DevUI-switchUser-dropdown-styling"
    />
  );
};

export default TaskInboxSwitchUser;
