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
import { BrowserRouter } from 'react-router-dom';
import { withKnobs, text } from '@storybook/addon-knobs';
import ServerErrors from './ServerErrors';

export default {
  title: 'Server errors',
  decorators: [withKnobs]
};

export const defaultView = () => {
  const error = text(
    'Error message',
    'Network error: Response not successful: Received status code 400'
  );
  return (
    <BrowserRouter>
      <ServerErrors error={error} variant="large" />
    </BrowserRouter>
  );
};
