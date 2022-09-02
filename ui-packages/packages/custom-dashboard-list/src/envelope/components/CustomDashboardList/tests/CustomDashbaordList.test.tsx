/*
 * Copyright 2022 Red Hat, Inc. and/or its affiliates.
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
import CustomDashboardList from '../CustomDashboardList';
import { MockedCustomDashboardListDriver } from '../../../tests/mocks/MockedCustomDashboardsListDriver';
import { ToggleGroupItem } from '@patternfly/react-core';
import { act } from 'react-dom/test-utils';
import wait from 'waait';

describe('customDashboard list tests', () => {
  jest.mock('../../CustomDashboardsGallery/CustomDashboardsGallery');
  jest.mock('../../CustomDashboardListToolbar/CustomDashboardListToolbar');
  jest.mock('../../CustomDashboardsTable/CustomDashboardsTable');
  Date.now = jest.fn(() => 1487076708000);
  const driver = new MockedCustomDashboardListDriver();

  it('envelope not connected to channel', async () => {
    const props = {
      isEnvelopeConnectedToChannel: false,
      driver: null
    };
    let wrapper;
    await act(async () => {
      wrapper = mount(<CustomDashboardList {...props} />);
    });
    expect(
      wrapper.find(CustomDashboardList).props()['isEnvelopeConnectedToChannel']
    ).toBeFalsy();
  });

  it('render customDashboard list - table', async () => {
    const props = {
      isEnvelopeConnectedToChannel: true,
      driver: driver
    };
    let wrapper;
    await act(async () => {
      wrapper = mount(<CustomDashboardList {...props} />);
    });
    expect(wrapper).toMatchSnapshot();
  });

  it('render CustomDashboard list - gallery', async () => {
    const props = {
      isEnvelopeConnectedToChannel: true,
      driver: driver
    };
    let wrapper;
    // switches to gallery view
    await act(async () => {
      wrapper = mount(<CustomDashboardList {...props} />);
      wrapper
        .find(ToggleGroupItem)
        .at(1)
        .find('button')
        .simulate('click');
    });
    await wait(0);
    await act(async () => {
      wrapper = wrapper.update();
    });
    expect(wrapper).toMatchSnapshot();

    // switches to table view
    await act(async () => {
      wrapper
        .find(ToggleGroupItem)
        .at(0)
        .find('button')
        .simulate('click');
    });
    await wait(0);
    await act(async () => {
      wrapper = wrapper.update();
    });
    expect(wrapper.find('CustomDashboardList').exists()).toBeTruthy();
  });
});
