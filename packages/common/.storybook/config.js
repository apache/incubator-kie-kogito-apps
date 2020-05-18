import { configure, addDecorator } from '@storybook/react';
import '@patternfly/patternfly/patternfly.css';

import StoryRouter from 'storybook-react-router';
 
addDecorator(StoryRouter());
module = {
  addons: ['@storybook/addon-actions/register',
    '@storybook/addon-storysource',
    '@storybook/addon-knobs/register',
    '@storybook/addon-links/register',
    '@storybook/addon-viewport/register',
    '@storybook/addon-console',
    '@storybook/addon-options/register']
}
;
configure(require.context('../src/components', true, /\.stories\.tsx$/), module);