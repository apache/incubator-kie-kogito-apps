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
import { EmbeddedJobsManagement } from '../EmbeddedJobsManagement';
import { MockedJobsManagementDriver } from './mocks/Mocks';
import { mount } from 'enzyme';

describe('EmbeddedJobsManagement tests', () => {
  it('Snapshot', () => {
    const props = {
      targetOrigin: 'origin',
      driver: new MockedJobsManagementDriver()
    };

    const wrapper = mount(<EmbeddedJobsManagement {...props} />);

    expect(wrapper).toMatchSnapshot();
    expect(wrapper.props().driver).toStrictEqual(props.driver);
    expect(wrapper.props().targetOrigin).toStrictEqual(props.targetOrigin);
    const div = wrapper.find('div');

    expect(div.exists()).toBeTruthy();
  });
});
