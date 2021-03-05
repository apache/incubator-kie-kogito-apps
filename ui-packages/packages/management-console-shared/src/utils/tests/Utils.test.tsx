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

import {
  constructObject,
  formatForBulkListJob,
  handleJobReschedule,
  setTitle
} from '../Utils';
import axios from 'axios';
jest.mock('axios');
const mockedAxios = axios as jest.Mocked<typeof axios>;
const children = 'children';

export enum JobStatus {
  Error = 'ERROR',
  Executed = 'EXECUTED',
  Scheduled = 'SCHEDULED',
  Retry = 'RETRY',
  Canceled = 'CANCELED'
}
describe('Management-console-shared utils tests', () => {
  it('set title tests', () => {
    const successResult = setTitle('success', 'Abort operation');
    const failureResult = setTitle('failure', 'Skip operation');
    expect(successResult.props[children][1].props.children).toEqual(
      'Abort operation'
    );
    expect(failureResult.props[children][1].props.children).toEqual(
      'Skip operation'
    );
  });

  it('test reschedule function', async () => {
    mockedAxios.patch.mockResolvedValue({
      status: 200,
      statusText: 'OK',
      data: {
        callbackEndpoint:
          'http://localhost:8080/management/jobs/travels/instances/9865268c-64d7-3a44-8972-7325b295f7cc/timers/58180644-2fdf-4261-83f2-f4e783d308a3_0',
        executionCounter: 0,
        executionResponse: null,
        expirationTime: '2020-10-16T10:17:22.879Z',
        id: '58180644-2fdf-4261-83f2-f4e783d308a3_0',
        lastUpdate: '2020-10-07T07:41:31.467Z',
        priority: 0,
        processId: 'travels',
        processInstanceId: '9865268c-64d7-3a44-8972-7325b295f7cc',
        repeatInterval: null,
        repeatLimit: null,
        retries: 0,
        rootProcessId: null,
        rootProcessInstanceId: null,
        scheduledId: null,
        status: 'SCHEDULED'
      }
    });
    const job = {
      id: '6e74a570-31c8-4020-bd70-19be2cb625f3_0',
      processId: 'travels',
      processInstanceId: '5c56eeff-4cbf-3313-a325-4c895e0afced',
      rootProcessId: '5c56eeff-4cbf-3313-a325-4c895e0afced',
      status: JobStatus.Executed,
      priority: 0,
      callbackEndpoint:
        'http://localhost:8080/management/jobs/travels/instances/5c56eeff-4cbf-3313-a325-4c895e0afced/timers/6e74a570-31c8-4020-bd70-19be2cb625f3_0',
      repeatInterval: 1,
      repeatLimit: 3,
      scheduledId: '0',
      retries: 0,
      lastUpdate: '2020-08-27T03:35:50.147Z',
      expirationTime: '2020-08-27T03:35:50.147Z'
    };
    const repeatInterval = 2;
    const repeatLimit = 1;
    const rescheduleClicked = false;
    const setRescheduleClicked = jest.fn();
    const scheduleDate = new Date('2020-08-27T03:35:50.147Z');
    const refetch = jest.fn();
    const setErrorMessage = jest.fn();
    await handleJobReschedule(
      job,
      repeatInterval,
      repeatLimit,
      rescheduleClicked,
      setErrorMessage,
      setRescheduleClicked,
      scheduleDate,
      refetch
    );
    expect(setRescheduleClicked).toHaveBeenCalled();
  });
  it('test error response for reschedule function', async () => {
    mockedAxios.patch.mockRejectedValue({ message: '403 error' });
    const job = {
      id: '6e74a570-31c8-4020-bd70-19be2cb625f3_0',
      processId: 'travels',
      processInstanceId: '5c56eeff-4cbf-3313-a325-4c895e0afced',
      rootProcessId: '5c56eeff-4cbf-3313-a325-4c895e0afced',
      status: JobStatus.Executed,
      priority: 0,
      callbackEndpoint:
        'http://localhost:8080/management/jobs/travels/instances/5c56eeff-4cbf-3313-a325-4c895e0afced/timers/6e74a570-31c8-4020-bd70-19be2cb625f3_0',
      repeatInterval: 1,
      repeatLimit: 3,
      scheduledId: '0',
      retries: 0,
      lastUpdate: '2020-08-27T03:35:50.147Z',
      expirationTime: '2020-08-27T03:35:50.147Z'
    };
    const repeatInterval = null;
    const repeatLimit = null;
    const rescheduleClicked = false;
    const setRescheduleClicked = jest.fn();
    const scheduleDate = new Date('2020-08-27T03:35:50.147Z');
    const refetch = jest.fn();
    const setErrorMessage = jest.fn();
    await handleJobReschedule(
      job,
      repeatInterval,
      repeatLimit,
      rescheduleClicked,
      setErrorMessage,
      setRescheduleClicked,
      scheduleDate,
      refetch
    );
    expect(setRescheduleClicked).toHaveBeenCalled();
  });
  it('Test constructObject function', () => {
    const obj = {};
    const keys = 'trip,country,equal';
    const value = 'India';
    constructObject(obj, keys, value);
    expect(obj).toEqual({ trip: { country: { equal: 'India' } } });
  });

  it('test format job for bulklist function', () => {
    const testJob = [
      {
        id: 'dad3aa88-5c1e-4858-a919-uey23c675a0fa_0',
        processId: 'travels',
        processInstanceId: 'e4448857-fa0c-403b-ad69-f0a353458b9d',
        rootProcessId: '',
        status: JobStatus.Scheduled,
        priority: 0,
        callbackEndpoint:
          'http://localhost:8080/management/jobs/travels/instances/5c56eeff-4cbf-3313-a325-4c895e0afced/timers/dad3aa88-5c1e-4858-a919-6123c675a0fa_0',
        repeatInterval: null,
        repeatLimit: null,
        scheduledId: null,
        retries: 5,
        lastUpdate: new Date('2020-08-27T03:35:54.635Z'),
        expirationTime: new Date('2020-08-27T04:35:54.631Z'),
        endpoint: 'http://localhost:4000/jobs',
        nodeInstanceId: '08c153e8-2766-4675-81f7-29943efdf411',
        executionCounter: 1,
        errorMessage: '403 error'
      }
    ];
    const testResultWithError = formatForBulkListJob(testJob);
    expect(testResultWithError).toEqual([
      {
        id: testJob[0].id,
        name: testJob[0].processId,
        description: testJob[0].id,
        errorMessage: testJob[0].errorMessage
      }
    ]);
    const testResultWithoutError = formatForBulkListJob([
      { ...testJob[0], errorMessage: null }
    ]);
    expect(testResultWithoutError).toEqual([
      {
        id: testJob[0].id,
        name: testJob[0].processId,
        description: testJob[0].id,
        errorMessage: null
      }
    ]);
  });
});
