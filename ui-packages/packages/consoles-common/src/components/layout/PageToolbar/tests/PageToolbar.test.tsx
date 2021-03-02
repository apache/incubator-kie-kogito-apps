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
import { act } from 'react-dom/test-utils';
import { shallow } from 'enzyme';
import { Dropdown } from '@patternfly/react-core';
import { getWrapper } from '@kogito-apps/components-common';
import PageToolbar from '../PageToolbar';
import {
  resetTestKogitoAppContext,
  testHandleLogoutMock,
  testIsTestUserSystemEnabledMock
} from '../../../../environment/auth/tests/utils/KogitoAppContextTestingUtils';

jest.mock('../../AboutModalBox/AboutModalBox');
jest.mock('../../PageToolbarUsersDropdownGroup/PageToolbarUsersDropdownGroup');
jest.mock('../../AddTestUser/AddTestUser');

describe('PageToolbar component tests', () => {
  beforeEach(() => {
    testIsTestUserSystemEnabledMock.mockReturnValue(false);
    testHandleLogoutMock.mockClear();
    resetTestKogitoAppContext(false);
  });

  it('Snapshot testing - auth disabled', () => {
    const wrapper = getWrapper(<PageToolbar />, 'PageToolbar');

    expect(wrapper).toMatchSnapshot();
  });

  it('Snapshot testing - auth enabled', () => {
    resetTestKogitoAppContext(true);
    const wrapper = getWrapper(<PageToolbar />, 'PageToolbar');

    expect(wrapper).toMatchSnapshot();
  });

  it('Testing dropdown items - auth enabled', () => {
    resetTestKogitoAppContext(true);

    const wrapper = shallow(<PageToolbar />);

    expect(wrapper).toMatchSnapshot();

    const dropdown = wrapper.find(Dropdown);

    const dropdownItems = dropdown.prop('dropdownItems');

    expect(dropdownItems.length).toStrictEqual(3);
  });

  it('Testing logout - auth enabled', () => {
    resetTestKogitoAppContext(true);

    const wrapper = shallow(<PageToolbar />);

    const dropdown = wrapper.find(Dropdown);

    const dropdownItems = dropdown.prop('dropdownItems');

    expect(dropdownItems.length).toStrictEqual(3);

    const logout = dropdownItems[2];

    act(() => {
      logout.props.onClick();
    });

    expect(testHandleLogoutMock).toBeCalled();
  });

  it('Testing dropdown items - auth disabled', () => {
    const wrapper = shallow(<PageToolbar />);

    expect(wrapper).toMatchSnapshot();

    const dropdown = wrapper.find(Dropdown);

    const dropdownItems = dropdown.prop('dropdownItems');

    expect(dropdownItems.length).toStrictEqual(1);
  });

  it('Testing dropdown items - auth disabled TestUserSystem enabled', () => {
    testIsTestUserSystemEnabledMock.mockReturnValue(true);

    const wrapper = shallow(<PageToolbar />);

    expect(wrapper).toMatchSnapshot();

    const dropdown = wrapper.find(Dropdown);

    const dropdownItems = dropdown.prop('dropdownItems');

    expect(dropdownItems.length).toStrictEqual(3);
  });

  it('Testing select dropdown test', () => {
    let wrapper = shallow(<PageToolbar />);

    let dropdown = wrapper.find(Dropdown);

    expect(dropdown.prop('isOpen')).toBeFalsy();

    act(() => {
      dropdown.prop('onSelect')();
    });

    wrapper = wrapper.update();

    dropdown = wrapper.find(Dropdown);

    expect(dropdown.prop('isOpen')).toBeTruthy();
  });

  it('Testing toggle dropdown test', () => {
    let wrapper = shallow(<PageToolbar />);

    let dropdown = wrapper.find(Dropdown);

    expect(dropdown.prop('isOpen')).toBeFalsy();

    act(() => {
      dropdown.prop('toggle').props.onToggle(true);
    });

    wrapper = wrapper.update();

    dropdown = wrapper.find(Dropdown);

    expect(dropdown.prop('isOpen')).toBeTruthy();
  });

  it('handleAboutModalToggle test', () => {
    const wrapper = getWrapper(<PageToolbar />, 'PageToolbar');

    let aboutModalBox = wrapper.find('MockedAboutModalBox');

    expect(aboutModalBox.exists()).toBeTruthy();

    expect(aboutModalBox.prop('isOpenProp')).toBeFalsy();

    act(() => {
      // tslint:disable:no-string-literal
      aboutModalBox.props()['handleModalToggleProp']();
    });

    aboutModalBox = wrapper.update().find('MockedAboutModalBox');

    expect(aboutModalBox.prop('isOpenProp')).toBeTruthy();
  });

  it('Testing handleaddUserModalToggle - TestUserSystem enabled', () => {
    testIsTestUserSystemEnabledMock.mockReturnValue(true);

    const wrapper = getWrapper(<PageToolbar />, 'PageToolbar');

    let addUserModal = wrapper.find('MockedAddTestUser');

    expect(addUserModal.exists()).toBeTruthy();

    expect(addUserModal.prop('isOpen')).toBeFalsy();

    act(() => {
      addUserModal.props()['toggleModal']();
    });

    addUserModal = wrapper.update().find('MockedAddTestUser');

    expect(addUserModal.prop('isOpen')).toBeTruthy();
  });

  it('Testing handleaddUserModalToggle test - TestUserSystem disabled', () => {
    testIsTestUserSystemEnabledMock.mockReturnValue(false);

    const wrapper = getWrapper(<PageToolbar />, 'PageToolbar');

    let addUserModal = wrapper.find('MockedAddTestUser');

    expect(addUserModal.exists()).toBeTruthy();

    expect(addUserModal.prop('isOpen')).toBeFalsy();

    act(() => {
      addUserModal.props()['toggleModal']();
    });

    addUserModal = wrapper.update().find('MockedAddTestUser');

    expect(addUserModal.prop('isOpen')).toBeFalsy();
  });
});
