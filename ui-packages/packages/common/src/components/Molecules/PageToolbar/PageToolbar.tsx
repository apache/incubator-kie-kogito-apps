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
import React, { useState } from 'react';
import {
  Dropdown,
  DropdownToggle,
  Toolbar,
  ToolbarGroup,
  ToolbarItem,
  DropdownItem,
  DropdownSeparator
} from '@patternfly/react-core';
import accessibleStyles from '@patternfly/react-styles/css/utilities/Accessibility/accessibility';
import { css } from '@patternfly/react-styles';
import AboutModalBox from '../AboutModalBox/AboutModalBox';
import {
  getUserName,
  handleLogout,
  isAuthEnabled
} from '../../../utils/KeycloakClient';
import { componentOuiaProps, OUIAProps } from '../../../utils/OuiaUtils';

const PageToolbar: React.FunctionComponent<OUIAProps> = ({
  ouiaId,
  ouiaSafe
}) => {
  const [isDropdownOpen, setDropdownOpen] = useState(false);
  const [modalToggle, setmodalToggle] = useState(false);

  const handleModalToggle = () => {
    setmodalToggle(modalToggle ? false : true);
  };

  const onDropdownToggle = _isDropdownOpen => {
    setDropdownOpen(_isDropdownOpen);
  };

  const onDropdownSelect = () => {
    setDropdownOpen(!isDropdownOpen);
  };
  const userDropdownItems = [
    <DropdownItem key={1} onClick={handleModalToggle}>
      About
    </DropdownItem>
  ];

  if (isAuthEnabled()) {
    userDropdownItems.push(
      <DropdownSeparator key={2} />,
      <DropdownItem component="button" key={3} onClick={handleLogout}>
        Log out
      </DropdownItem>
    );
  }

  return (
    <React.Fragment>
      <AboutModalBox
        isOpenProp={modalToggle}
        handleModalToggleProp={handleModalToggle}
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
                <DropdownToggle onToggle={onDropdownToggle}>
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
