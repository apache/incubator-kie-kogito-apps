/**
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import React from 'react';
import { withKnobs, text } from '@storybook/addon-knobs';
import NoData from './NoData';

export default {
  title: 'No data',
  component: NoData,
  decorators: [withKnobs]
};

export const defaultView = () => {
  const props = {
    location: {
      state: {
        prev: '/DomainExplorer',
        title: text('Title', 'Domain not found'),
        description: text(
          'Description',
          'Domain with the name Travels not found'
        ),
        buttonText: text('Button Text', 'Go to domain explorer')
      }
    },
    defaultPath: '/DomainExplorer',
    defaultButton: text('Default Button', 'Go to domain explorer')
  };

  return <NoData {...props} />;
};
