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
import { getWrapper } from '@kogito-apps/components-common';
import ConsolesNav from '../ConsolesNav';
import { MemoryRouter } from 'react-router-dom';

describe('ConsolesNav tests', () => {
  it('Snapshot testing with process list props', () => {
    const wrapper = getWrapper(
      <MemoryRouter>
        <ConsolesNav pathname={'/ProcessInstances'} />
      </MemoryRouter>,
      'ConsolesNav'
    );

    expect(wrapper).toMatchSnapshot();

    const ConsolesNavWrapper = wrapper.findWhere(
      nested => nested.key() === 'process-instances-nav'
    );

    expect(ConsolesNavWrapper.exists()).toBeTruthy();
    expect(ConsolesNavWrapper.props().isActive).toBeTruthy();
  });

  it('Snapshot testing with jobs management props', () => {
    const wrapper = getWrapper(
      <MemoryRouter>
        <ConsolesNav pathname={'/JobsManagement'} />
      </MemoryRouter>,
      'ConsolesNav'
    );

    expect(wrapper).toMatchSnapshot();

    const ConsolesNavWrapper = wrapper.findWhere(
      nested => nested.key() === 'jobs-management-nav'
    );

    expect(ConsolesNavWrapper.exists()).toBeTruthy();
    expect(ConsolesNavWrapper.props().isActive).toBeTruthy();
  });
});
