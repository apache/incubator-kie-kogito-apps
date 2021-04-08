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
import { TaskInboxEnvelopeViewApi } from './TaskInboxEnvelopeView';
import {
  Association,
  TaskInboxChannelApi,
  TaskInboxEnvelopeApi,
  TaskInboxInitArgs
} from '../api';
import { TaskInboxEnvelopeContext } from './TaskInboxEnvelopeContext';

/**
 * Implementation of the TaskInboxEnvelopeApi
 */
export class TaskInboxEnvelopeApiImpl implements TaskInboxEnvelopeApi {
  constructor(
    private readonly args: EnvelopeApiFactoryArgs<
      TaskInboxEnvelopeApi,
      TaskInboxChannelApi,
      TaskInboxEnvelopeViewApi,
      TaskInboxEnvelopeContext
    >
  ) {}

  taskInbox__init = (
    association: Association,
    initArgs: TaskInboxInitArgs
  ): Promise<void> => {
    this.args.envelopeBusController.associate(
      association.origin,
      association.envelopeServerId
    );
    this.args
      .view()
      .initialize(
        initArgs.initialState,
        initArgs.allTaskStates,
        initArgs.activeTaskStates
      );
    return Promise.resolve();
  };
}
