/*
 * Copyright 2023 Red Hat, Inc. and/or its affiliates.
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
import { mount } from 'enzyme';
import { createMemoryHistory } from 'history';
import CloudEventFormPage, {
  CloudEventPageSource
} from '../CloudEventFormPage';
import CloudEventFormContainer from '../../../containers/CloudEventFormContainer/CloudEventFormContainer';
import { MemoryRouter } from 'react-router';

jest.mock(
  '../../../containers/CloudEventFormContainer/CloudEventFormContainer'
);

let historyMock;

jest.mock('react-router-dom', () =>
  Object.assign({
    ...jest.requireActual('react-router-dom'),
    useHistory: () => historyMock
  })
);

describe('CloudEventFormPage - tests', () => {
  it('Snapshot - definitions', () => {
    historyMock = createMemoryHistory();
    const state = {
      source: CloudEventPageSource.DEFINITIONS
    };
    historyMock.push('/', state);

    const wrapper = mount(
      <MemoryRouter keyLength={0}>
        <CloudEventFormPage />
      </MemoryRouter>
    );

    expect(wrapper).toMatchSnapshot();

    const container = wrapper.find(CloudEventFormContainer);
    expect(container.exists()).toBeTruthy();
    expect(container.props().isTriggerNewInstance).toBeTruthy();
  });

  it('Snapshot - instances', () => {
    historyMock = createMemoryHistory();
    const state = {
      source: CloudEventPageSource.INSTANCES
    };
    historyMock.push('/', state);

    const wrapper = mount(
      <MemoryRouter keyLength={0}>
        <CloudEventFormPage />
      </MemoryRouter>
    );

    expect(wrapper).toMatchSnapshot();

    const container = wrapper.find(CloudEventFormContainer);
    expect(container.exists()).toBeTruthy();
    expect(container.props().isTriggerNewInstance).toBeFalsy();
  });
});
