import React from 'react';
import { Brand } from '@patternfly/react-core';
import { withRouter } from 'react-router-dom';
import { RouteComponentProps } from 'react-router';
import Management_Console_60px from '../../../static/Management_Console_60px.svg';

const BrandComponent: React.FC<RouteComponentProps> = ({ history }) => {
  const onLogoClick = () => {
    history.push('/');
  };
  return (
    <Brand
      src={Management_Console_60px}
      alt="Task Console Logo"
      onClick={onLogoClick}
    />
  );
};

export default withRouter(BrandComponent);
