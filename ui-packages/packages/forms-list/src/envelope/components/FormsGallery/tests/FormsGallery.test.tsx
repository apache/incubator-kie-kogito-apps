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
import FormsGallery from '../FormsGallery';
import { mount } from 'enzyme';
import {
  formList,
  MockedFormsListDriver
} from '../../../tests/mocks/MockedFormsListDriver';
import {
  KogitoEmptyState,
  KogitoSpinner
} from '@kogito-apps/components-common';

const MockedComponent = (): React.ReactElement => {
  return <></>;
};

jest.mock('@kogito-apps/components-common', () =>
  Object.assign({}, jest.requireActual('@kogito-apps/components-common'), {
    KogitoSpinner: () => {
      return <MockedComponent />;
    },
    KogitoEmptyState: () => {
      return <MockedComponent />;
    }
  })
);

describe('forms gallery tests', () => {
  Date.now = jest.fn(() => 1487076708000);
  const driver = new MockedFormsListDriver();
  it('renders gallery of forms', () => {
    const wrapper = mount(
      <FormsGallery driver={driver} isLoading={false} formsData={formList} />
    );
    expect(wrapper).toMatchSnapshot();
  });

  it('renders loading component', () => {
    const wrapper = mount(
      <FormsGallery driver={driver} isLoading={true} formsData={formList} />
    );
    expect(wrapper).toMatchSnapshot();
    const loadingComponent = wrapper.find(KogitoSpinner);
    expect(loadingComponent.exists()).toBeTruthy();
  });

  it('renders empty state component', () => {
    const wrapper = mount(
      <FormsGallery driver={driver} isLoading={false} formsData={[]} />
    );
    expect(wrapper).toMatchSnapshot();
    const emptyStateComponent = wrapper.find(KogitoEmptyState);
    expect(emptyStateComponent.exists()).toBeTruthy();
  });
});
