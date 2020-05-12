import React from 'react';
import { PageHeader, InjectedOuiaProps, withOuiaContext } from '@patternfly/react-core';
import Avatar from '../../Atoms/AvatarComponent/AvatarComponent';
import PageToolbarComponent from '../PageToolbarComponent/PageToolbarComponent';
import BrandComponent from '../../Atoms/BrandComponent/BrandComponent';
import { componentOuiaProps } from '@kogito-apps/common';

const HeaderComponent: React.FC<InjectedOuiaProps> = ({
  ouiaContext,
  ouiaId
}) => {
  return (
    <PageHeader
      {...componentOuiaProps(ouiaContext, ouiaId, 'PageHeader', true)}
      logo={<BrandComponent />}
      toolbar={<PageToolbarComponent />}
      avatar={<Avatar />}
    />
  );
};

const HeaderComponentWithContext = withOuiaContext(HeaderComponent);
export default HeaderComponentWithContext;
