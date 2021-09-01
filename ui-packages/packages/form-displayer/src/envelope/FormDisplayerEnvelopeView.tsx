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

import * as React from 'react';
import { useImperativeHandle, useState } from 'react';

import { FormDisplayerChannelApi } from '../api';
import { MessageBusClientApi } from '@kogito-tooling/envelope-bus/dist/api';
import { UserTaskInstance } from '@kogito-apps/task-console-shared';
import ReactFormRenderer from './components/ReactFormRenderer/ReactFormRenderer';

import '@patternfly/patternfly/patternfly.css';

export interface FormDisplayerEnvelopeViewApi {
  setTask(task: UserTaskInstance): void;
}

interface Props {
  channelApi: MessageBusClientApi<FormDisplayerChannelApi>;
}

export const FormDisplayerEnvelopeView = React.forwardRef<
  FormDisplayerEnvelopeViewApi,
  Props
>((props, forwardedRef) => {
  // @ts-ignore
  const [task, setTask] = useState<UserTaskInstance>();

  useImperativeHandle(
    forwardedRef,
    () => {
      return {
        setTask: (formContent: any) => {
          setTask(formContent);
        }
      };
    },
    []
  );
  console.log('task', task);
  return <ReactFormRenderer />;
});