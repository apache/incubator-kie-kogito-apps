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

import { JobStatus } from '@kogito-apps/management-console-shared';
import { JobsIconCreator } from '../utils';
const children = 'children';

describe('process details package utils', () => {
  it('Jobs icon creator tests', () => {
    const jobsErrorResult = JobsIconCreator(JobStatus.Error);
    const jobsCanceledResult = JobsIconCreator(JobStatus.Canceled);
    const jobsScheduledResult = JobsIconCreator(JobStatus.Scheduled);
    const jobsExecutedResult = JobsIconCreator(JobStatus.Executed);
    const jobsRetryResult = JobsIconCreator(JobStatus.Retry);

    expect(jobsErrorResult.props[children][1]).toEqual('Error');
    expect(jobsCanceledResult.props[children][1]).toEqual('Canceled');
    expect(jobsScheduledResult.props[children][1]).toEqual('Scheduled');
    expect(jobsRetryResult.props[children][1]).toEqual('Retry');
    expect(jobsExecutedResult.props[children][1]).toEqual('Executed');
  });
});
