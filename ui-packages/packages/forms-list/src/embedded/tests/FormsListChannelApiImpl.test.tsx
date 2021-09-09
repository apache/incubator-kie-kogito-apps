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
import { FormInfo, FormsListDriver } from '../../api';
import { FormsListChannelApiImpl } from '../FormsListChannelApiImpl';
import { MockedFormsListDriver } from './utils/Mocks';

let driver: FormsListDriver;
let api: FormsListChannelApiImpl;

describe('FormsListChannelApiImpl tests', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    driver = new MockedFormsListDriver();
    api = new FormsListChannelApiImpl(driver);
  });

  it('FormsList__getFormFilter', () => {
    api.formsList__getFormFilter();
    expect(driver.getFormFilter).toHaveBeenCalled();
  });

  it('FormsList__applyFilter', () => {
    const formFilter = {
      formNames: ['form1']
    };
    api.formsList__applyFilter(formFilter);
    expect(driver.applyFilter).toHaveBeenCalledWith(formFilter);
  });

  it('FormsList__getFormsQuery', () => {
    api.formsList__getFormsQuery();
    expect(driver.getFormsQuery).toHaveBeenCalled();
  });

  it('FormsList__openForm', () => {
    const formsData: FormInfo = {
      name: 'form1',
      type: 'tsx',
      lastModified: new Date(2020, 6, 2)
    };
    api.formsList__openForm(formsData);
    expect(driver.openForm).toHaveBeenCalledWith(formsData);
  });
});
