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
import { mount } from 'enzyme';
import ExecutionDetail from '../ExecutionDetail';
import { Outcome, RemoteData } from '../../../../types';
import { MemoryRouter } from 'react-router';

const mockHistoryPush = jest.fn();
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useHistory: () => ({
    push: mockHistoryPush
  })
}));

describe('ExecutionDetail', () => {
  test('renders a loading animation while fetching data', () => {
    const outcome = {
      status: 'LOADING'
    } as RemoteData<Error, Outcome[]>;
    const wrapper = mount(<ExecutionDetail outcomes={outcome} />);

    expect(wrapper.find('Title').text()).toMatch('Outcomes');
    expect(wrapper.find('SkeletonCards')).toHaveLength(1);
    expect(wrapper.find('Outcomes')).toHaveLength(0);
  });

  test('renders a list of outcomes', () => {
    const wrapper = mount(<ExecutionDetail outcomes={outcomes} />);

    expect(
      wrapper
        .find('StackItem')
        .at(0)
        .find('Title')
        .text()
    ).toMatch('Outcomes');
    expect(wrapper.find('SkeletonCards')).toHaveLength(0);
    expect(wrapper.find('Outcomes')).toHaveLength(1);
    expect(wrapper.find('Outcomes').prop('outcomes')).toStrictEqual(
      outcomes.status === 'SUCCESS' && outcomes.data
    );
  });

  test('handles navigation to the outcome-details page', () => {
    const wrapper = mount(
      <MemoryRouter>
        <ExecutionDetail outcomes={outcomes} />
      </MemoryRouter>
    );

    wrapper
      .find('button.outcome-cards__card__explanation-link')
      .at(0)
      .simulate('click');

    expect(mockHistoryPush).toHaveBeenCalledWith({
      pathname: 'outcomes-details',
      search: '?outcomeId=_12268B68-94A1-4960-B4C8-0B6071AFDE58'
    });
  });
});

const outcomes = {
  status: 'SUCCESS',
  data: [
    {
      outcomeId: '_12268B68-94A1-4960-B4C8-0B6071AFDE58',
      outcomeName: 'Mortgage Approval',
      evaluationStatus: 'SUCCEEDED',
      outcomeResult: {
        name: 'Mortgage Approval',
        typeRef: 'boolean',
        value: true,
        components: []
      },
      messages: [],
      hasErrors: false
    },
    {
      outcomeId: '_9CFF8C35-4EB3-451E-874C-DB27A5A424C0',
      outcomeName: 'Risk Score',
      evaluationStatus: 'SUCCEEDED',
      outcomeResult: {
        name: 'Risk Score',
        typeRef: 'number',
        value: 21.7031851958099,
        components: []
      },
      messages: [],
      hasErrors: false
    }
  ]
} as RemoteData<Error, Outcome[]>;
