import React from 'react';
import { Avatar, withOuiaContext, InjectedOuiaProps } from '@patternfly/react-core';
import userImage from '../../../static/avatar.svg';
import { componentOuiaProps } from '@kogito-apps/common'

const AvatarComponent: React.FC<InjectedOuiaProps> = ({
  ouiaContext,
  ouiaId
}) => {
  return <Avatar
    src={userImage}
    alt="Kogito Logo"
    {...componentOuiaProps(ouiaContext, ouiaId, 'Avatar', true)}
  />;
};

const AvatarComponentWithContext = withOuiaContext(AvatarComponent);
export default AvatarComponentWithContext;
