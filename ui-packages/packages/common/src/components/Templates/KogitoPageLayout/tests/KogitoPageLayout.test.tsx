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
import KogitoPageLayout from '../KogitoPageLayout';
import { getWrapper } from '../../../../utils/OuiaUtils';
import { act } from 'react-dom/test-utils';

const props = {
  children: <React.Fragment>children rendered</React.Fragment>,
  BrandSrc: '../../../../static/kogito.png',
  PageNav: <React.Fragment>page Navigation elements</React.Fragment>,
  BrandAltText: 'Kogito logo',
  BrandClick: jest.fn()
};

describe('KogitoPageLayout component tests', () => {
  it('snapshot tests', () => {
    const wrapper = getWrapper(
      <KogitoPageLayout {...props} />,
      'KogitoPageLayout'
    );
    expect(wrapper).toMatchSnapshot();
  });
  it('check isNavOpen boolean', () => {
    const wrapper = getWrapper(
      <KogitoPageLayout {...props} />,
      'KogitoPageLayout'
    );
    const event = {
      target: {}
    } as React.MouseEvent<HTMLInputElement>;
    act(() => {
      wrapper.find('Button').prop('onClick')(event);
      wrapper.update();
    });
    expect(wrapper.find('PageSidebar').prop('isNavOpen')).toBeTruthy();
  });
});
