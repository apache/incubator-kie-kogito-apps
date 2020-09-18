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
import InputDataBrowser from '../InputDataBrowser';
import { shallow, mount } from 'enzyme';
import { ItemObject, RemoteData } from '../../../../types';

describe('InputDataBrowser', () => {
  test('renders a loading animation while fetching data', () => {
    const inputData = { status: 'LOADING' } as RemoteData<Error, ItemObject[]>;
    const wrapper = shallow(<InputDataBrowser inputData={inputData} />);

    expect(wrapper).toMatchSnapshot();
  });

  test('renders a list of inputs', () => {
    const inputData = {
      status: 'SUCCESS',
      data: [
        { name: 'Asset Score', typeRef: 'number', value: 738, components: [] },
        {
          name: 'Asset Amount',
          typeRef: 'number',
          value: 700,
          components: []
        },
        {
          name: 'Property',
          typeRef: 'tProperty',
          value: null,
          components: [
            {
              name: 'Purchase Price',
              typeRef: 'number',
              value: 34000,
              components: []
            }
          ]
        }
      ]
    } as RemoteData<Error, ItemObject[]>;
    const wrapper = mount(<InputDataBrowser inputData={inputData} />);
    const dataList = wrapper.find('DataList.input-browser__data-list');

    expect(wrapper.find('.input-browser__section-list')).toHaveLength(1);
    expect(wrapper.find('.input-browser__section-list button')).toHaveLength(2);
    expect(
      wrapper
        .find('.input-browser__section-list button')
        .at(0)
        .text()
    ).toMatch('Root');
    expect(
      wrapper
        .find('.input-browser__section-list button')
        .at(1)
        .text()
    ).toMatch('Property');
    expect(dataList).toHaveLength(1);
    expect(dataList.find('DataListItem.input-browser__header')).toHaveLength(1);
    expect(dataList.find('CategoryLine')).toHaveLength(1);
    expect(dataList.find('CategoryLine').prop('categoryLabel')).toMatch('Root');
    expect(dataList.find('InputValue')).toHaveLength(2);
    expect(
      dataList
        .find('InputValue')
        .at(0)
        .prop('inputValue')
    ).toBe(738);
    expect(
      dataList
        .find('InputValue')
        .at(1)
        .prop('inputValue')
    ).toBe(700);

    wrapper
      .find('.input-browser__section-list button')
      .at(1)
      .simulate('click');
    const propertyDataList = wrapper.find('DataList.input-browser__data-list');

    expect(
      propertyDataList.find('DataListItem.input-browser__header')
    ).toHaveLength(1);
    expect(propertyDataList.find('CategoryLine')).toHaveLength(1);
    expect(propertyDataList.find('CategoryLine').prop('categoryLabel')).toMatch(
      'Property'
    );

    expect(propertyDataList.find('InputValue')).toHaveLength(1);
    expect(propertyDataList.find('InputValue').prop('inputValue')).toBe(34000);
  });
});
