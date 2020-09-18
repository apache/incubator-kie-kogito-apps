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
import ServerUnavailable from '../ServerUnavailable';
import { Button } from '@patternfly/react-core';

const props = {
  src: '.../../../../static/logo.png',
  alt: 'Logo alt text',
  pageNav: (
    <React.Component>
      <Button>something</Button>
    </React.Component>
  )
};

describe('ServerUnavailable component tests', () => {
  const location: Location = window.location;
  beforeEach(() => {
    delete window.location;
    window.location = {
      ...location,
      reload: jest.fn()
    };
  });
  afterEach(() => {
    jest.restoreAllMocks();
    window.location = location;
  });
  it('snapshot testing ', () => {
    const wrapper = shallow(<ServerUnavailable {...props} />);
    expect(wrapper).toMatchSnapshot();
  });
  /* tslint:disable */
  it('reload button click ', () => {
    const wrapper = shallow(<ServerUnavailable {...props} />);
    wrapper.find(Button).simulate('click');
    expect(window.location.reload).toHaveBeenCalledTimes(1);
  });
  it('onNav toggle test', () => {
    const wrapper = shallow(<ServerUnavailable {...props} />);
    wrapper
      .find('Page')
      .props()
      ['header']['props']['onNavToggle']();
    expect(
      wrapper.find('Page').props()['header']['props']['isNavOpen']
    ).toBeFalsy();
  });
});
