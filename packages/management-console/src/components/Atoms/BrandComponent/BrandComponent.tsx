import React from 'react';
import { Brand, OuiaContext, InjectedOuiaProps, withOuiaContext } from '@patternfly/react-core';
import { withRouter } from 'react-router-dom';
import { RouteComponentProps } from 'react-router';
import managementConsoleLogo from '../../../static/managementConsoleLogo.svg';
import { componentOuiaProps } from '@kogito-apps/common';

const BrandComponent: React.FC<RouteComponentProps & InjectedOuiaProps> = ({
  history,
  ouiaContext,
  ouiaId
}) => {
  const onLogoClick = () => {
    history.push('/ProcessInstances');
  };
  return (
    <Brand
      src={managementConsoleLogo}
      alt="Management Console Logo"
      onClick={onLogoClick}
      {...componentOuiaProps(OuiaContext, ouiaId, 'Brand', true)}
    />
  );
};

export default withRouter(withOuiaContext(BrandComponent));
