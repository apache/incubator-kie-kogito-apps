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
import Breadcrumbs from '../Breadcrumbs';
import { mount } from 'enzyme';
import { MemoryRouter } from 'react-router-dom';

describe('Breadcrumbs', () => {
  test('renders correctly', () => {
    const wrapper = mount(
      <MemoryRouter initialEntries={['/audit']}>
        <Breadcrumbs />
      </MemoryRouter>
    );
    const breadcrumbs = wrapper.find(Breadcrumbs);

    expect(breadcrumbs).toMatchSnapshot();
    expect(breadcrumbs.find('li.breadcrumb-item')).toHaveLength(0);
  });

  test('renders outcome details breadcrumbs links', () => {
    const executionId = 'b2b0ed8d-c1e2-46b5-3ac54ff4beae-1000';
    const wrapper = mount(
      <MemoryRouter
        initialEntries={[
          {
            pathname: `/audit/decision/${executionId}/outcomes`,
            key: 'execution'
          }
        ]}
      >
        <Breadcrumbs />
      </MemoryRouter>
    );
    const breadcrumbs = wrapper.find(Breadcrumbs);

    expect(breadcrumbs).toMatchSnapshot();
    expect(breadcrumbs.find('li.breadcrumb-item')).toHaveLength(3);
    expect(
      breadcrumbs
        .find('li.breadcrumb-item')
        .at(0)
        .text()
    ).toMatch('Audit Investigation');
    expect(
      breadcrumbs
        .find('li.breadcrumb-item')
        .at(1)
        .text()
    ).toMatch(`ID #${executionId}`);
    expect(
      breadcrumbs
        .find('li.breadcrumb-item')
        .at(2)
        .text()
    ).toMatch('Outcomes');
    expect(
      breadcrumbs
        .find('li.breadcrumb-item')
        .find('.pf-c-breadcrumb__link.pf-m-current')
    ).toHaveLength(1);
  });
});
