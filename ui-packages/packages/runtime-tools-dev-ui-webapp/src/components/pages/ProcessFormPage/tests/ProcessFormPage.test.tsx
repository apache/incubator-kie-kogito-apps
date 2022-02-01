/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import React from 'react';
import { mount } from 'enzyme';
import ProcessFormPage from '../ProcessFormPage';
import { BrowserRouter } from 'react-router-dom';

jest.mock('../components/InlineEdit/InlineEdit');
jest.mock('../../../containers/ProcessFormContainer/ProcessFormContainer');

jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useHistory: () => ({
    location: {
      state: {
        processDefinition: {
          processName: 'process1',
          endpoint: 'http://localhost:4000'
        }
      }
    }
  })
}));

describe('ProcessFormPage tests', () => {
  it('Snapshot', () => {
    const wrapper = mount(
      <BrowserRouter>
        <ProcessFormPage />
      </BrowserRouter>
    );

    expect(wrapper.find('ProcessFormPage')).toMatchSnapshot();
    expect(wrapper.find('MockedProcessFormContainer').exists()).toBeTruthy();
  });
});
