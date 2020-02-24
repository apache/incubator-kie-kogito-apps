import React from 'react';
import { Avatar } from '@patternfly/react-core';
import userImg from '../../../static/user.png';

const AvatarComponent: React.FC = () => {
  return <Avatar src={userImg} alt="Kogito Logo" />;
};

export default AvatarComponent;

