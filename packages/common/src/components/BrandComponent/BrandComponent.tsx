import React from 'react';
import { Brand } from '@patternfly/react-core';
// import { withRouter } from 'react-router-dom';
// import { RouteComponentProps } from 'react-router';
// import Management_Console_60px from '../../../static/Management_Console_60px.svg';

interface IOwnProps {
  src: string;
  alt: string;
  brandClick: any;
}

const BrandComponent: React.FC<IOwnProps> = (props) => {
  return (
    <Brand
      src={props.src}
      alt={props.alt}
      onClick={props.brandClick}
    />
  );
};

export default BrandComponent;
