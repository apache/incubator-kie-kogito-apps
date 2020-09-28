/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
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

import React, { useState } from 'react';
import {
  Avatar,
  Dropdown,
  DropdownItem,
  DropdownSeparator,
  DropdownToggle,
  Toolbar,
  ToolbarGroup,
  ToolbarItem
} from '@patternfly/react-core';
import accessibleStyles from '@patternfly/react-styles/css/utilities/Accessibility/accessibility';
import { css } from '@patternfly/react-styles';
import AboutModalBox from '../AboutModalBox/AboutModalBox';
import { componentOuiaProps, OUIAProps } from '../../../utils/OuiaUtils';
import userImage from '../../../static/avatar.svg';
import {
  AppContext,
  isContextInTestMode,
  useKogitoAppContext
} from '../../../environment/context/KogitoAppContext';
import PageToolbarUsersDropdownGroup from '../PageToolbarUsersDropdownGroup/PageToolbarUsersDropdownGroup';
import AddTestUser from '../../Atoms/AddTestUser/AddTestUser';

const PageToolbar: React.FunctionComponent<OUIAProps> = ({
  ouiaId,
  ouiaSafe
}) => {
  const [isDropdownOpen, setDropdownOpen] = useState<boolean>(false);
  const [modalToggle, setmodalToggle] = useState<boolean>(false);
  const [addUserToggle, setAddUserToggle] = useState<boolean>(false);

  const context: AppContext = useKogitoAppContext();

  const testMode = isContextInTestMode(context);

  const handleAboutModalToggle = () => {
    setmodalToggle(!modalToggle);
  };

  const handleAddUserModalToggle = () => {
    setAddUserToggle(!addUserToggle);
  };

  const onDropdownToggle = _isDropdownOpen => {
    setDropdownOpen(_isDropdownOpen);
  };

  const onDropdownSelect = () => {
    setDropdownOpen(!isDropdownOpen);
  };

  const getUserName = () => {
    return context && context.getCurrentUser()
      ? context.getCurrentUser().id
      : 'Anonymous';
  };

  const handleLogout = () => {
    if (context.userContext) {
      context.userContext.logout();
    }
  };

  const userDropdownItems: React.ReactElement[] = [];

  userDropdownItems.push(
    <DropdownItem
      key={userDropdownItems.length}
      onClick={handleAboutModalToggle}
    >
      About
    </DropdownItem>
  );

  if (testMode) {
    userDropdownItems.push(
      <DropdownSeparator key={userDropdownItems.length} />
    );
    userDropdownItems.push(
      <PageToolbarUsersDropdownGroup
        key={userDropdownItems.length}
        toggleAddUsersModal={() => setAddUserToggle(true)}
      />
    );
  }

  userDropdownItems.push(<DropdownSeparator key={userDropdownItems.length} />);
  userDropdownItems.push(
    <DropdownItem key={userDropdownItems.length} onClick={handleLogout}>
      Log out
    </DropdownItem>
  );

  return (
    <React.Fragment>
      <AboutModalBox
        isOpenProp={modalToggle}
        handleModalToggleProp={handleAboutModalToggle}
      />
      <AddTestUser
        isOpen={testMode && addUserToggle}
        toggleModal={handleAddUserModalToggle}
      />
      <Toolbar {...componentOuiaProps(ouiaId, 'page-toolbar', ouiaSafe)}>
        <ToolbarGroup>
          <ToolbarItem
            className={css(
              accessibleStyles.screenReader,
              accessibleStyles.visibleOnMd
            )}
          >
            <Dropdown
              isPlain
              position="right"
              onSelect={onDropdownSelect}
              isOpen={isDropdownOpen}
              toggle={
                <DropdownToggle
                  onToggle={onDropdownToggle}
                  icon={<Avatar src={userImage} alt="User Avatar" />}
                >
                  {getUserName()}
                </DropdownToggle>
              }
              dropdownItems={userDropdownItems}
            />
          </ToolbarItem>
        </ToolbarGroup>
      </Toolbar>
    </React.Fragment>
  );
};

export default PageToolbar;
