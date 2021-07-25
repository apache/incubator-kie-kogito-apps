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
import TaskInboxPage from '../TaskInboxPage';
import TaskInboxContainer from '../../../containers/TaskInboxContainer/TaskInboxContainer';
import DevUIAppContextProvider from '../../../contexts/DevUIAppContextProvider';

jest.mock('../../../containers/TaskInboxContainer/TaskInboxContainer');

describe('TaskInboxPage tests', () => {
  it('Snapshot', () => {
    const wrapper = mount(
      <DevUIAppContextProvider users={[{ id: 'John snow', groups: ['admin'] }]}>
        <TaskInboxPage />
      </DevUIAppContextProvider>
    );

    expect(wrapper).toMatchSnapshot();
    expect(wrapper.find(TaskInboxContainer).exists()).toBeTruthy();
  });
});
