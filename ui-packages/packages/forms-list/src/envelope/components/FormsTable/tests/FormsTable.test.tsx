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
import { mount } from 'enzyme';
import FormsTable from '../FormsTable';
import {
  formList,
  MockedFormsListDriver
} from '../../../tests/mocks/MockedFormsListDriver';
import { DataTable } from '@kogito-apps/components-common';

Date.now = jest.fn(() => 1487076708000); //14.02.2017

const MockedComponent = (): React.ReactElement => {
  return <></>;
};

jest.mock('@kogito-apps/components-common', () => ({
  ...jest.requireActual('@kogito-apps/components-common'),
  DataTable: () => {
    return <MockedComponent />;
  }
}));

describe('forms table test', () => {
  const driver = new MockedFormsListDriver();
  it('renders table', () => {
    const wrapper = mount(
      <FormsTable
        driver={driver}
        isLoading={false}
        formsData={formList}
        setFormsData={jest.fn()}
      />
    );
    const dataTable = wrapper.find(DataTable);
    expect(dataTable.exists()).toBeTruthy();
  });
});
