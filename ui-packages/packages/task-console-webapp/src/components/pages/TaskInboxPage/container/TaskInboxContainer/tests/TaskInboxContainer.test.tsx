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
import { getWrapper } from '@kogito-apps/components-common';
import { DefaultUser } from '@kogito-apps/consoles-common';
import TaskInboxContainer from '../TaskInboxContainer';
import * as TaskInboxContext from '../../../../../../channel/inbox/TaskInboxContext';
import { TaskInboxQueries } from '../../../../../../channel/inbox/TaskInboxQueries';
import { TaskInboxGatewayApiImpl } from '../../../../../../channel/inbox/TaskInboxGatewayApi';

const MockQueries = jest.fn<TaskInboxQueries, []>(() => ({
  getUserTasks: jest.fn()
}));

jest
  .spyOn(TaskInboxContext, 'useTaskInboxGatewayApi')
  .mockImplementation(
    () =>
      new TaskInboxGatewayApiImpl(
        new DefaultUser('jon', []),
        new MockQueries(),
        jest.fn()
      )
  );

describe('TaskInboxContainer tests', () => {
  it('Snapshot', () => {
    const wrapper = getWrapper(<TaskInboxContainer />, 'TaskInboxContainer');

    expect(wrapper).toMatchSnapshot();

    const forwardRef = wrapper.childAt(0);
    expect(forwardRef.props().activeTaskStates).toStrictEqual([
      'Ready',
      'Reserved'
    ]);
    expect(forwardRef.props().allTaskStates).toStrictEqual([
      'Ready',
      'Reserved',
      'Completed',
      'Aborted',
      'Skipped'
    ]);
    expect(forwardRef.props().driver).not.toBeNull();
    expect(forwardRef.props().envelopePath).toBe('/envelope/task-inbox.html');
    expect(forwardRef.props().targetOrigin).toBe('http://localhost');
  });
});
