import React from 'react';
//import { withKnobs, text } from '@storybook/addon-knobs';
import NoData from './NoData';

export default {
  title: 'No data',
  component: NoData
};

export const defaultView = args => {
  // const props = {
  //   location: {
  //     state: {
  //       prev: '/DomainExplorer',
  //       title: text('Title', 'Domain not found'),
  //       description: text(
  //         'Description',
  //         'Domain with the name Travels not found'
  //       ),
  //       buttonText: text('Button Text', 'Go to domain explorer')
  //     }
  //   },
  //   defaultPath: '/DomainExplorer',
  //   defaultButton: text('Default Button', 'Go to domain explorer')
  // };

  return <NoData {...args} />;
};
defaultView.args = {
  location: {
    state: {
      prev: '/DomainExplorer',
      title: 'Domain not found',
      description: 'Domain with the name Travels not found',
      buttonText: 'Go to domain explorer'
    }
  },
  defaultPath: '/DomainExplorer',
  defaultButton: 'Go to domain explorer'
};
