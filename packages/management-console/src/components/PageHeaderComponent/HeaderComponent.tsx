import React from 'react';
import { PageHeader } from '@patternfly/react-core';
import Avatar from '../AvatarComponent/AvatarComponent';
import PageToolbarComponent from '../PageToolbarComponent/PageToolbarComponent';
import BrandComponent from '../BrandComponent/BrandComponent';

interface IOwnProps {}

const HeaderComponent: React.FC<IOwnProps> = () => {
  return <PageHeader logo={<BrandComponent />} toolbar={<PageToolbarComponent />} avatar={<Avatar />} showNavToggle />;
};

export default HeaderComponent;
