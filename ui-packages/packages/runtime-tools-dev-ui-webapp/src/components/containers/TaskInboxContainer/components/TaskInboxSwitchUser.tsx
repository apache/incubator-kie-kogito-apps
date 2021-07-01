import React, { useState } from 'react';
import {
  Dropdown,
  DropdownToggle,
  DropdownItem,
  Tooltip
} from '@patternfly/react-core';
import { ThIcon } from '@patternfly/react-icons';
import { useDevUIAppContext } from '../../../contexts/DevUIAppContext';

const TaskInboxSwitchUser = () => {
  const [isOpen, setIsOpen] = useState(false);
  const appContext = useDevUIAppContext();
  const allUsers = appContext.getAllUsers();

  const onSelect = (event): void => {
    const selectedUser = event.target.innerHTML;
    appContext.switchUser(selectedUser);

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
          <Tooltip position="top" content={<div>Switch user</div>}>
            <ThIcon />
          </Tooltip>
        </DropdownToggle>
      }
      isOpen={isOpen}
      isPlain
      dropdownItems={dropdownItems()}
    />
  );
};

export default TaskInboxSwitchUser;
