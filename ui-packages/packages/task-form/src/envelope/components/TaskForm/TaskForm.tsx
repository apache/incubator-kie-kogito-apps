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

import React, { useEffect, useState } from 'react';
import _ from 'lodash';
import EmptyTaskForm from '../EmptyTaskForm/EmptyTaskForm';
import {
  componentOuiaProps,
  KogitoEmptyState,
  KogitoEmptyStateType,
  KogitoSpinner,
  OUIAProps
} from '@kogito-apps/components-common';
import { UserTaskInstance } from '@kogito-apps/task-inbox';
import { TaskFormDriver } from '../../../api';
import { TaskFormSchema } from '../../../types';
import { Bullseye } from '@patternfly/react-core';
import TaskFormRenderer from '../TaskFormRenderer/TaskFormRenderer';
import {
  readSchemaAssignments,
  TaskDataAssignments
} from '../utils/TaskFormDataUtils';

export interface TaskFormProps {
  userTask: UserTaskInstance;
  driver: TaskFormDriver;
  isEnvelopeConnectedToChannel: boolean;
}

enum State {
  READY,
  SUBMITTING,
  SUBMITTED
}

const TaskForm: React.FC<TaskFormProps & OUIAProps> = ({
  userTask,
  driver,
  isEnvelopeConnectedToChannel,
  ouiaId,
  ouiaSafe
}) => {
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [formData, setFormData] = useState<any>(null);
  const [taskFormSchema, setTaskFormSchema] = useState<TaskFormSchema>(null);
  const [taskFormAssignments, setTaskFormAssignments] = useState<
    TaskDataAssignments
  >();
  const [formState, setFormState] = useState<State>(State.READY);

  useEffect(() => {
    if (isEnvelopeConnectedToChannel) {
      loadForm();
    }
  }, [isEnvelopeConnectedToChannel]);

  const loadForm = async () => {
    try {
      const schema = await driver.getTaskFormSchema();
      setTaskFormAssignments(readSchemaAssignments(schema));
      setTaskFormSchema(schema);
      setIsLoading(false);
    } catch (err) {
      setIsLoading(false);
    }
  };

  if (isLoading) {
    return (
      <Bullseye
        {...componentOuiaProps(
          (ouiaId ? ouiaId : 'task-form') + '-loading-spinner',
          'task-form',
          ouiaSafe
        )}
      >
        <KogitoSpinner
          spinnerText={`Loading task form...`}
          ouiaId={(ouiaId ? ouiaId : 'task-form') + '-spinner-loading'}
          ouiaSafe={ouiaSafe}
        />
      </Bullseye>
    );
  }

  if (taskFormSchema) {
    if (formState == State.SUBMITTING) {
      return (
        <Bullseye
          {...componentOuiaProps(
            (ouiaId ? ouiaId : 'task-form') + '-submit-spinner',
            'task-form',
            ouiaSafe
          )}
        >
          <KogitoSpinner
            spinnerText={`Submitting for task ${
              userTask.referenceName
            } (${userTask.id.substring(0, 5)})`}
            ouiaId={(ouiaId ? ouiaId : 'task-form') + '-spinner-submitting'}
            ouiaSafe={ouiaSafe}
          />
        </Bullseye>
      );
    }

    const doSubmit = async (phase: string, data: any) => {
      try {
        setFormState(State.SUBMITTING);
        setFormData(data);

        const payload = {};

        taskFormAssignments.outputs.forEach(output => {
          if (_.has(data, output)) {
            _.set(payload, output, _.get(data, output));
          }
        });

        await driver.doSubmit(phase, payload);
      } finally {
        setFormState(State.SUBMITTED);
      }
    };

    if (_.isEmpty(taskFormSchema.properties)) {
      return (
        <EmptyTaskForm
          {...componentOuiaProps(
            (ouiaId ? ouiaId : 'task-form') + '-empty-form',
            'task-form',
            ouiaSafe
          )}
          userTask={userTask}
          enabled={formState == State.READY}
          formSchema={taskFormSchema}
          submit={phase => doSubmit(phase, {})}
        />
      );
    }

    return (
      <TaskFormRenderer
        {...componentOuiaProps(
          (ouiaId ? ouiaId : 'task-form') + '-form-renderer',
          'task-form',
          ouiaSafe
        )}
        userTask={userTask}
        formSchema={taskFormSchema}
        formData={formData}
        enabled={formState == State.READY}
        submit={doSubmit}
      />
    );
  }

  return (
    <KogitoEmptyState
      type={KogitoEmptyStateType.Info}
      title="No form to show"
      body={`Cannot find form for task  ${
        userTask.referenceName
      } (${userTask.id.substring(0, 5)})`}
      {...componentOuiaProps(
        (ouiaId ? ouiaId : 'task-form') + '-no-form',
        'empty-task-form',
        ouiaSafe
      )}
    />
  );
};

export default TaskForm;
