import React, { useEffect, useState } from 'react';
import get from 'lodash/get';
import has from 'lodash/has';
import isEmpty from 'lodash/isEmpty';
import set from 'lodash/set';
import { Bullseye } from '@patternfly/react-core/dist/js/layouts/Bullseye';
import { KogitoSpinner } from '@kogito-apps/components-common/dist/components/KogitoSpinner';
import {
  componentOuiaProps,
  OUIAProps
} from '@kogito-apps/ouia-tools/dist/utils/OuiaUtils';
import { UserTaskInstance } from '@kogito-apps/task-console-shared';
import { TaskFormDriver } from '../../../api';
import EmptyTaskForm from '../EmptyTaskForm/EmptyTaskForm';
import TaskFormRenderer from '../TaskFormRenderer/TaskFormRenderer';
import {
  parseTaskSchema,
  TaskDataAssignments
} from '../utils/TaskFormDataUtils';

export interface TaskFormProps {
  userTask: UserTaskInstance;
  schema: Record<string, any>;
  driver: TaskFormDriver;
}

enum State {
  READY,
  SUBMITTING,
  SUBMITTED
}

const TaskForm: React.FC<TaskFormProps & OUIAProps> = ({
  userTask,
  schema,
  driver,
  ouiaId,
  ouiaSafe
}) => {
  const [formData, setFormData] = useState<any>(null);
  const [formState, setFormState] = useState<State>(State.READY);
  const [taskFormSchema, setTaskFormSchema] = useState<Record<string, any>>();
  const [taskFormAssignments, setTaskFormAssignments] =
    useState<TaskDataAssignments>();

  useEffect(() => {
    const parsedSchema = parseTaskSchema(schema);
    setTaskFormSchema(parsedSchema.schema);
    setTaskFormAssignments(parsedSchema.assignments);
  }, []);

  if (formState === State.SUBMITTING) {
    return (
      <Bullseye
        {...componentOuiaProps(
          (ouiaId ? ouiaId : 'task-form') + '-submit-spinner',
          'task-form',
          true
        )}
      >
        <KogitoSpinner
          spinnerText={`Submitting for task ${
            userTask.referenceName
          } (${userTask.id.substring(0, 5)})`}
        />
      </Bullseye>
    );
  }

  if (formState === State.READY || formState === State.SUBMITTED) {
    const doSubmit = async (
      phase: string,
      data: any,
      onSuccess?: (response: any) => void,
      onFailure?: (response: any) => void
    ) => {
      try {
        setFormState(State.SUBMITTING);
        setFormData(data);

        const payload = {};

        taskFormAssignments.outputs.forEach((output) => {
          if (has(data, output)) {
            set(payload, output, get(data, output));
          }
        });

        const result = await driver.doSubmit(phase, payload);
        if (onSuccess) {
          onSuccess(result);
        }
      } catch (err) {
        if (onFailure) {
          onFailure(err);
        }
      } finally {
        setFormState(State.SUBMITTED);
      }
    };

    if (!taskFormSchema) {
      return (
        <Bullseye
          {...componentOuiaProps(
            (ouiaId ? ouiaId : 'task-form-') + '-loading-spinner',
            'task-form',
            true
          )}
        >
          <KogitoSpinner spinnerText={`Loading task form...`} />
        </Bullseye>
      );
    }

    if (isEmpty(taskFormSchema.properties)) {
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
          submit={(phase) => doSubmit(phase, {})}
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
};

export default TaskForm;
