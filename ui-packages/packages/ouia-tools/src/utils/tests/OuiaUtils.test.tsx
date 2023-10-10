/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
import React, { useEffect } from 'react';
import { render } from '@testing-library/react';
import {
  ouiaAttribute,
  attributeOuiaId,
  componentOuiaProps,
  ouiaPageTypeAndObjectId
} from '../OuiaUtils';

describe('test function ouiaAttribute', () => {
  it('no value', () => {
    const attribute = ouiaAttribute('attribute', undefined);
    expect(attribute).not.toHaveProperty('attribute');
  });
  it('int value', () => {
    const attribute = ouiaAttribute('attribute', 3);
    expect(attribute).toHaveProperty('attribute', 3);
  });
});
describe('test function attributeOuiaId', () => {
  it('no value', () => {
    const ouiaId = attributeOuiaId(undefined);
    expect(ouiaId).not.toHaveProperty('data-ouia-component-id');
  });
  it('string value', () => {
    const ouiaId = attributeOuiaId('value');
    expect(ouiaId).toHaveProperty('data-ouia-component-id', 'value');
  });
  it('int value', () => {
    const ouiaId = attributeOuiaId(5);
    expect(ouiaId).toHaveProperty('data-ouia-component-id', 5);
  });
});
describe('test function componentOuiaProps', () => {
  it('only component type', () => {
    const componentProps = componentOuiaProps(
      undefined,
      'test-type',
      undefined
    );
    expect(componentProps).toHaveProperty(
      'data-ouia-component-type',
      'test-type'
    );
    expect(componentProps).not.toHaveProperty('data-ouia-component-id');
    expect(componentProps).toHaveProperty('data-ouia-safe', true);
  });
  it('all non-default', () => {
    const componentProps = componentOuiaProps('ouia-id', 'test-type', false);
    expect(componentProps).toHaveProperty(
      'data-ouia-component-type',
      'test-type'
    );
    expect(componentProps).toHaveProperty('data-ouia-component-id', 'ouia-id');
    expect(componentProps).toHaveProperty('data-ouia-safe', false);
  });
  it('spread operator using variable', () => {
    const componentProps = componentOuiaProps('ouia-id', 'test-type', false);
    const spread = { ...componentProps };
    expect(spread).toHaveProperty('data-ouia-component-type', 'test-type');
    expect(spread).toHaveProperty('data-ouia-component-id', 'ouia-id');
    expect(spread).toHaveProperty('data-ouia-safe', false);
  });
  it('spread operator direct', () => {
    const spread = { ...componentOuiaProps('ouia-id', 'test-type', false) };
    expect(spread).toHaveProperty('data-ouia-component-type', 'test-type');
    expect(spread).toHaveProperty('data-ouia-component-id', 'ouia-id');
    expect(spread).toHaveProperty('data-ouia-safe', false);
  });
});
const TestComponentSettingPageType = (): React.ReactElement => {
  useEffect(() => {
    return ouiaPageTypeAndObjectId('test-page-type');
  });
  return <div />;
};
const TestComponentSettingPageTypeAndId = (): React.ReactElement => {
  useEffect(() => {
    return ouiaPageTypeAndObjectId('test-page-type', 'test-object-id');
  });
  return <div />;
};
describe('test ouiaPageTypeAndObjectId', () => {
  document.body.setAttribute = jest.fn();
  document.body.removeAttribute = jest.fn();
  it('page type only', () => {
    render(<TestComponentSettingPageType />);
    expect(document.body.setAttribute).toBeCalledWith(
      'data-ouia-page-type',
      'test-page-type'
    );
  });
  it('page type and id', () => {
    render(<TestComponentSettingPageTypeAndId />);
    expect(document.body.setAttribute).toBeCalledWith(
      'data-ouia-page-type',
      'test-page-type'
    );
    expect(document.body.setAttribute).toBeCalledWith(
      'data-ouia-page-object-id',
      'test-object-id'
    );

    expect(document.body.removeAttribute).toBeCalledWith('data-ouia-page-type');
  });
});
