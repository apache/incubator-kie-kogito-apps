import React from 'react';
import { Brand } from '@patternfly/react-core';
import { withRouter } from 'react-router-dom';
import { RouteComponentProps } from 'react-router';
import kogitoLogoRGB from '../../../static/kogitoLogoRGB.png';
type combinedProps = RouteComponentProps & IOwnProps;
interface IOwnProps {}

const BrandComponent: React.FC<combinedProps> = ({ history }) => {
  const onLogoClick = () => {
    history.push('/ProcessInstances');
  };
  return <Brand src={kogitoLogoRGB} alt="Kogito Logo" onClick={onLogoClick} />;
};

export default withRouter(BrandComponent);
