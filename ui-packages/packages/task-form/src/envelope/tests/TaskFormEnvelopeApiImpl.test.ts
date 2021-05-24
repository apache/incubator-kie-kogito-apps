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

import { EnvelopeApiFactoryArgs } from '@kogito-tooling/envelope';
import {
  MockedEnvelopeBusController,
  MockedTaskFormEnvelopeViewApi,
  testUserTask
} from './mocks/Mocks';
import { TaskFormChannelApi, TaskFormEnvelopeApi } from '../../api';
import { TaskFormEnvelopeViewApi } from '../TaskFormEnvelopeView';
import { TaskFormEnvelopeContext } from '../TaskFormEnvelopeContext';
import { TaskFormEnvelopeApiImpl } from '../TaskFormEnvelopeApiImpl';

describe('TaskFormEnvelopeApiImpl tests', () => {
  it('initialize', () => {
    const envelopeBusController = new MockedEnvelopeBusController();
    const view = new MockedTaskFormEnvelopeViewApi();
    const args: EnvelopeApiFactoryArgs<
      TaskFormEnvelopeApi,
      TaskFormChannelApi,
      TaskFormEnvelopeViewApi,
      TaskFormEnvelopeContext
    > = {
      envelopeBusController,
      envelopeContext: {},
      view: () => view
    };

    const envelopeApi = new TaskFormEnvelopeApiImpl(args);

    envelopeApi.taskForm__init(
      {
        envelopeServerId: 'envelopeServerId',
        origin: 'origin'
      },
      {
        userTask: testUserTask
      }
    );

    expect(envelopeBusController.associate).toHaveBeenCalledWith(
      'origin',
      'envelopeServerId'
    );

    expect(view.initialize).toHaveBeenCalledWith(testUserTask);
  });
});
