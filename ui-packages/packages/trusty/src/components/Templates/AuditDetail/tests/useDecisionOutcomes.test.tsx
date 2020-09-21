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
import { renderHook } from '@testing-library/react-hooks';
import { act } from 'react-test-renderer';
import * as api from '../../../../utils/api/httpClient';
import useDecisionOutcomes from '../useDecisionOutcomes';
import { Execution, Outcome } from '../../../../types';

const flushPromises = () => new Promise(setImmediate);
const apiMock = jest.spyOn(api, 'httpClient');

beforeEach(() => {
  apiMock.mockClear();
});

describe('useDecisionOutcome', () => {
  test('retrieves the outcome(s) of a decision', async () => {
    const outcomes = {
      data: {
        header: {
          executionId: 'b2b0ed8d-c1e2-46b5-3ac54ff4beae-1000',
          executionDate: '2020-08-12T12:54:53.933Z',
          executionType: 'DECISION',
          executionSucceeded: true,
          executorName: 'Technical User'
        } as Execution,
        outcomes: [
          {
            outcomeId: '_12268B68-94A1-4960-B4C8-0B6071AFDE58',
            outcomeName: 'Mortgage Approval',
            evaluationStatus: 'SUCCEEDED',
            outcomeResult: {
              name: 'Mortgage Approval',
              typeRef: 'boolean',
              value: true,
              components: null
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
              components: null
            },
            messages: [],
            hasErrors: false
          }
        ] as Outcome[]
      }
    };

    // @ts-ignore
    apiMock.mockImplementation(() => Promise.resolve(outcomes));

    const { result } = renderHook(() => {
      // tslint:disable-next-line:react-hooks-nesting
      return useDecisionOutcomes('b2b0ed8d-c1e2-46b5-3ac54ff4beae-1000');
    });

    expect(result.current).toStrictEqual({ status: 'LOADING' });

    await act(async () => {
      await flushPromises();
    });

    expect(result.current).toStrictEqual(
      Object.assign({ status: 'SUCCESS' }, { data: outcomes.data.outcomes })
    );
    expect(apiMock).toHaveBeenCalledTimes(1);
  });
});
