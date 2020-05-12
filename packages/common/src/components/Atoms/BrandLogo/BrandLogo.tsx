import React from 'react';
import { Brand, InjectedOuiaProps, withOuiaContext } from '@patternfly/react-core';
import { componentOuiaProps } from '../../../utils/OuiaUtils';

interface IOwnProps {
  src: string;
  alt: string;
  brandClick: any;
}

const BrandLogo: React.FC<IOwnProps & InjectedOuiaProps> = ({
  ouiaContext,
  ouiaId,
  ...props
}) => {
  return <Brand src={props.src} alt={props.alt} onClick={props.brandClick} {...componentOuiaProps(ouiaContext, ouiaId, 'BrandLogo', true)}/>;
};

export default withOuiaContext(BrandLogo);
