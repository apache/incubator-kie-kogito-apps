import React from 'react';
import { Brand } from '@patternfly/react-core';
import { withRouter } from 'react-router-dom';
import { RouteComponentProps } from 'react-router';
// import taskConsoleLogo from '../../../../../task-console/src/static/managementConsoleLogo.svg';
import taskConsoleLogo from '../../../static/kogitoLogo.png';

const BrandComponent: React.FC<RouteComponentProps> = ({ history }) => {
  const onLogoClick = () => {
    history.push('/UserTasks');
  };
  return (
    <Brand
      src={taskConsoleLogo}
      alt="Task Console Logo"
      onClick={onLogoClick}
    />
  );
};

export default withRouter(BrandComponent);