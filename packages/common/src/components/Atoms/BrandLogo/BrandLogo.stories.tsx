import React from 'react';
import { storiesOf } from '@storybook/react';
import BrandLogo from './BrandLogo';
import managementConsoleLogo from '../../../static/managementConsoleLogo.svg';
import taskConsoleLogo from '../../../static/taskConsoleLogo.svg';

storiesOf('Brand logo', module)
  .add('Management Console', () => (
    <div style={{ backgroundColor: 'black' }}>
      <BrandLogo
        src={managementConsoleLogo}
        alt="Management Console"
        brandClick={() => null}
      />
    </div>
  ))
  .add('Task Console', () => (
    <div style={{ backgroundColor: 'black' }}>
      <BrandLogo
        src={taskConsoleLogo}
        alt="Task Console"
        brandClick={() => null}
      />
    </div>
  ));