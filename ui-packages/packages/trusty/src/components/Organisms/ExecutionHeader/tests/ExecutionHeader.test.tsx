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
import ExecutionHeader from '../ExecutionHeader';
import { shallow } from 'enzyme';
import { Execution, RemoteData } from '../../../../types';

describe('ExecutionHeader', () => {
  test('renders a loading animation while fetching data', () => {
    const execution = {
      status: 'LOADING'
    } as RemoteData<Error, Execution>;

    const wrapper = shallow(<ExecutionHeader execution={execution} />);

    expect(wrapper).toMatchSnapshot();
    expect(wrapper.find('SkeletonStripe')).toHaveLength(1);
  });

  test('renders the execution info', () => {
    const execution = {
      status: 'SUCCESS',
      data: {
        executionId: 'b2b0ed8d-c1e2-46b5-3ac54ff4beae-1000',
        executionDate: '2020-08-12T12:54:53.933Z',
        executionType: 'DECISION',
        executedModelName: 'fraud-score',
        executionSucceeded: true,
        executorName: 'Technical User'
      }
    } as RemoteData<Error, Execution>;
    const wrapper = shallow(<ExecutionHeader execution={execution} />);

    expect(wrapper).toMatchSnapshot();
    expect(wrapper.find('.execution-header__uuid').text()).toMatch(
      'ID# b2b0ed8d-c1e2-46b5-3ac54ff4beae-1000'
    );
    expect(wrapper.find('Tooltip')).toHaveLength(1);
    expect(wrapper.find('ExecutionStatus')).toHaveLength(1);
    expect(wrapper.find('ExecutionStatus').props().result).toMatch('success');
  });
});
