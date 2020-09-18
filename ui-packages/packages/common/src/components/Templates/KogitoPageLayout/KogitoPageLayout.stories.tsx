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
import React from 'react';
import KogitoPageLayout from './KogitoPageLayout';
import { action } from '@storybook/addon-actions';
import { PageSection, Nav, NavList, NavItem } from '@patternfly/react-core';
import managementConsoleLogo from '../../../examples/managementConsoleLogo.svg';

export default {
  title: 'Page layout'
};

export const defaultView = () => {
  const PageNav = (
    <Nav aria-label="Nav" theme="dark">
      <NavList>
        <NavItem onClick={action('button-click')}>Process Instances</NavItem>
        <NavItem onClick={action('button-click')}>Domain Explorer</NavItem>
      </NavList>
    </Nav>
  );
  return (
    <div style={{ height: '100vh' }}>
      <KogitoPageLayout
        PageNav={PageNav}
        BrandSrc={managementConsoleLogo}
        BrandAltText="Management Console Logo"
        BrandClick={action('button-click')}
      >
        <PageSection variant="light" />
      </KogitoPageLayout>
    </div>
  );
};
