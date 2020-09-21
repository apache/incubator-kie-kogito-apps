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
import useInputData from '../useInputData';
import * as api from '../../../../utils/api/httpClient';
import { ItemObject } from '../../../../types';

const flushPromises = () => new Promise(setImmediate);
const apiMock = jest.spyOn(api, 'httpClient');

beforeEach(() => {
  apiMock.mockClear();
});

describe('useInputData', () => {
  test('returns the inputs of a specific decision', async () => {
    const inputData = {
      data: {
        inputs: [
          {
            name: 'Asset Score',
            typeRef: 'number',
            value: 738,
            components: []
          },
          {
            name: 'Asset Amount',
            typeRef: 'number',
            value: 70000,
            components: []
          }
        ] as ItemObject[]
      }
    };

    // @ts-ignore
    apiMock.mockImplementation(() => Promise.resolve(inputData));

    const { result } = renderHook(() => {
      // tslint:disable-next-line:react-hooks-nesting
      return useInputData('b2b0ed8d-c1e2-46b5-3ac54ff4beae-1000');
    });

    expect(result.current).toStrictEqual({ status: 'LOADING' });

    await act(async () => {
      await flushPromises();
    });

    expect(result.current).toStrictEqual(
      Object.assign({ status: 'SUCCESS' }, { data: inputData.data.inputs })
    );
    expect(apiMock).toHaveBeenCalledTimes(1);
  });
});
