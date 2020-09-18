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
import {
  Page,
  PageSidebar,
  PageHeader,
  Avatar,
  Brand,
  PageHeaderTools,
  OUIAProps
} from '@patternfly/react-core';
import React, { useState, useEffect } from 'react';
import PageToolbar from '../../Molecules/PageToolbar/PageToolbar';
import { aboutLogoContext } from '../../contexts';
import '../../styles.css';

import userImage from '../../../static/avatar.svg';
import { ouiaAttribute } from '../../../utils/OuiaUtils';

interface IOwnProps {
  children: React.ReactNode;
  BrandSrc: string;
  PageNav: React.ReactNode;
  BrandAltText: string;
  BrandClick: () => void;
}

const KogitoPageLayout: React.FC<IOwnProps & OUIAProps> = ({
  ouiaId,
  ...props
}) => {
  const pageId = 'main-content-page-layout-default-nav';
  const [isNavOpen, setIsNavOpen] = useState(true);
  const onNavToggle = () => {
    setIsNavOpen(!isNavOpen);
  };

  useEffect(() => {
    if (document.getElementById(pageId)) {
      document.getElementById(pageId).setAttribute('data-ouia-main', 'true');
    }
  });

  const Header = (
    <PageHeader
      logo={
        <Brand
          src={props.BrandSrc}
          alt={props.BrandAltText}
          onClick={props.BrandClick}
        />
      }
      headerTools={
        <PageHeaderTools>
          <aboutLogoContext.Provider value={props.BrandSrc}>
            <PageToolbar />
          </aboutLogoContext.Provider>
          <Avatar src={userImage} alt="Kogito Logo" />
        </PageHeaderTools>
      }
      showNavToggle
      isNavOpen={isNavOpen}
      onNavToggle={onNavToggle}
      {...ouiaAttribute('data-ouia-header', 'true')}
    />
  );

  const Sidebar = (
    <PageSidebar
      nav={props.PageNav}
      isNavOpen={isNavOpen}
      theme="dark"
      {...ouiaAttribute('data-ouia-navigation', 'true')}
    />
  );

  return (
    <React.Fragment>
      <Page
        header={Header}
        mainContainerId={pageId}
        sidebar={Sidebar}
        className="kogito-common--PageLayout"
      >
        {props.children}
      </Page>
    </React.Fragment>
  );
};

export default KogitoPageLayout;
