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

import _ from 'lodash';
import { UserTaskInstance } from '@kogito-apps/task-console-shared';
import { TaskFormSchema } from '../../../types';

export interface TaskDataAssignments {
  inputs: string[];
  outputs: string[];
}

export function readSchemaAssignments(
  formSchema: TaskFormSchema
): TaskDataAssignments {
  const assignments: TaskDataAssignments = {
    inputs: [],
    outputs: []
  };

  if (!formSchema.properties) {
    return assignments;
  }

  for (const key of Object.keys(formSchema.properties)) {
    const property = formSchema.properties[key];
    if (_.get(property, 'input', false)) {
      assignments.inputs.push(key);
      _.unset(property, 'input');
    }
    if (_.get(property, 'output', false)) {
      assignments.outputs.push(key);
      _.unset(property, 'output');
    }

    if (!assignments.outputs.includes(key)) {
      const uniforms = _.get(property, 'uniforms', {});
      uniforms.disabled = true;
      _.set(property, 'uniforms', uniforms);
    }
  }

  return assignments;
}

function toJSON(value: string) {
  if (value) {
    try {
      return JSON.parse(value);
    } catch (e) {
      // do nothing
    }
  }
  return {};
}

export function generateFormData(userTask: UserTaskInstance): any {
  const taskInputs = toJSON(userTask.inputs);

  if (!userTask.outputs) {
    return taskInputs;
  }

  const taskOutputs = toJSON(userTask.outputs);

  return _.merge(taskInputs, taskOutputs);
}
