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
import { shallow } from 'enzyme';
import PageToolbar from '../PageToolbar';
import * as Keycloak from '../../../../utils/KeycloakClient';

describe('PageToolbar component tests', () => {
  const getUserName = jest.spyOn(Keycloak, 'getUserName');
  const currentEnv = process.env;

  getUserName.mockReturnValue('Ajay');
  afterEach(() => {
    process.env = currentEnv;
  });

  it('snapshot testing with kogito_auth_enabled param', () => {
    process.env = { KOGITO_AUTH_ENABLED: 'true' };
    const wrapper = shallow(<PageToolbar />);
    expect(wrapper).toMatchSnapshot();
  });

  it('snapshot testing with kogito-auth_enabled as null', () => {
    process.env = { KOGITO_AUTH_ENABLED: null };
    const wrapper = shallow(<PageToolbar />);
    expect(wrapper).toMatchSnapshot();
  });

  it('onDropdownSelect test', () => {
    process.env = { KOGITO_AUTH_ENABLED: 'true' };
    const wrapper = shallow(<PageToolbar />);
    const event = {
      target: {}
    } as React.ChangeEvent<HTMLInputElement>;
    wrapper.find('Dropdown').prop('onSelect')(event);
  });
  /* tslint:disable */

  it('isDropDownToggle test', () => {
    process.env = { KOGITO_AUTH_ENABLED: 'true' };
    const wrapper = shallow(<PageToolbar />);
    wrapper
      .find('Dropdown')
      .prop('toggle')
      ['props']['onToggle']();
  });

  it('handleModalToggleProp test', () => {
    process.env = { KOGITO_AUTH_ENABLED: 'true' };
    const wrapper = shallow(<PageToolbar />);
    expect(wrapper.find('AboutModalBox').props()['isOpenProp']).toBeFalsy();
    wrapper
      .find('AboutModalBox')
      .props()
      ['handleModalToggleProp']();
    expect(wrapper.find('AboutModalBox').props()['isOpenProp']).toBeTruthy();
  });
});
