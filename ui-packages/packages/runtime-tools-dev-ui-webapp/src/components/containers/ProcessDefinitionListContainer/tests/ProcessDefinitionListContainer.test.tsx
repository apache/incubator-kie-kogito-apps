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
import ProcessDefinitionListContainer from '../ProcessDefinitionListContainer';
import * as ProcessDefinitionListContext from '../../../../channel/ProcessDefinitionList/ProcessDefinitionListContext';
import { ProcessDefinitionListGatewayApiImpl } from '../../../../channel/ProcessDefinitionList/ProcessDefinitionListGatewayApi';
import { ProcessDefinitionListQueries } from '../../../../channel/ProcessDefinitionList/ProcessDefinitionListQueries';
import * as RuntimeToolsDevUIAppContext from '../../../contexts/DevUIAppContext';

const MockQueries = jest.fn<ProcessDefinitionListQueries>(() => ({
  getProcessDefinitions: jest.fn()
}));

jest
  .spyOn(ProcessDefinitionListContext, 'useProcessDefinitionListGatewayApi')
  .mockImplementation(
    () => new ProcessDefinitionListGatewayApiImpl(new MockQueries())
  );

jest
  .spyOn(RuntimeToolsDevUIAppContext, 'useDevUIAppContext')
  .mockImplementation(() => {
    return {
      isWorkflow: () => {
        return false;
      }
    };
  });

describe('ProcessDefinitionListContainer tests', () => {
  it('Snapshot', () => {
    const wrapper = mount(<ProcessDefinitionListContainer />);

    expect(wrapper).toMatchSnapshot();

    const forwardRef = wrapper.childAt(0);

    expect(forwardRef.props().driver).not.toBeNull();
    expect(forwardRef.props().targetOrigin).toBe('*');
  });
});
