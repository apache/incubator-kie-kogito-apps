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

import React, { useEffect, useRef, useState } from 'react';
import uuidv4 from 'uuid';
import cloneDeep from 'lodash/cloneDeep';
import unset from 'lodash/unset';
import { componentOuiaProps, OUIAProps } from '@kogito-apps/ouia-tools';
import {
  EmbeddedFormDisplayer,
  FormDisplayerApi,
  FormOpenedState,
  FormSubmitResponseType
} from '@kogito-apps/form-displayer';
import { UserTaskInstance } from '@kogito-apps/task-console-shared';
import { CustomForm, TaskFormSchema } from '../../../types';
import { generateFormData } from '../utils/TaskFormDataUtils';
import FormFooter from '../FormFooter/FormFooter';
import { FormAction } from '../utils';
import { TaskFormDriver, User } from '../../../api';
import { FormOpened } from '@kogito-apps/form-displayer/src';
import { Bullseye, Flex, FlexItem } from '@patternfly/react-core';
import { KogitoSpinner } from '@kogito-apps/components-common';

export interface CustomTaskFormDisplayerProps {
  userTask: UserTaskInstance;
  schema: TaskFormSchema;
  customForm: CustomForm;
  user: User;
  driver: TaskFormDriver;
}

const CustomTaskFormDisplayer: React.FC<CustomTaskFormDisplayerProps &
  OUIAProps> = ({
  userTask,
  customForm,
  schema,
  user,
  driver,
  ouiaId,
  ouiaSafe
}) => {
  const formDisplayerApiRef = useRef<FormDisplayerApi>();
  const [formUUID] = useState<string>(uuidv4());
  const [formData] = useState(generateFormData(userTask));
  const [formActions, setFormActions] = useState<FormAction[]>([]);
  const [formOpened, setFormOpened] = useState<FormOpened>();
  const [submitted, setSubmitted] = useState<boolean>(false);

  const doSubmit = async (phase: string, payload: any) => {
    const formDisplayerApi = formDisplayerApiRef.current;

    try {
      const response = await driver.doSubmit(phase, payload);
      formDisplayerApi.notifySubmitResult({
        type: FormSubmitResponseType.SUCCESS,
        info: response
      });
    } catch (error) {
      formDisplayerApi.notifySubmitResult({
        type: FormSubmitResponseType.FAILURE,
        info: error
      });
    } finally {
      setSubmitted(true);
    }
  };

  useEffect(() => {
    if (schema.phases) {
      const actions = schema.phases.map(phase => {
        return {
          name: phase,
          execute: () => {
            const formDisplayerApi = formDisplayerApiRef.current;
            formDisplayerApi
              .startSubmit({
                params: {
                  phase: phase
                }
              })
              .then(formOutput => doSubmit(phase, formOutput))
              .catch(error =>
                console.log(`Couldn't submit form due to: ${error}`)
              );
          }
        };
      });
      setFormActions(actions);
    }
  }, []);

  useEffect(() => {
    if (formOpened) {
      document.getElementById(`${formUUID}-form`).style.visibility = 'visible';
      document.getElementById(`${formUUID}-form`).style.height =
        formOpened.size.height + 'px';
    }
  }, [formOpened]);

  const buildTaskFormContext = (): Record<string, any> => {
    const ctxSchema = cloneDeep(schema);

    const ctxPhases = ctxSchema.phases;

    unset(ctxSchema, 'phases');

    const ctxTask = cloneDeep(userTask);

    unset(ctxTask, 'actualOwner');
    unset(ctxTask, 'adminGroups');
    unset(ctxTask, 'adminUsers');
    unset(ctxTask, 'excludedUsers');
    unset(ctxTask, 'potentialGroups');
    unset(ctxTask, 'potentialUsers');
    unset(ctxTask, 'inputs');
    unset(ctxTask, 'outputs');
    unset(ctxTask, 'endpoint');

    return {
      user: user,
      task: ctxTask,
      schema: ctxSchema,
      phases: ctxPhases
    };
  };
  return (
    <div {...componentOuiaProps(ouiaId, 'custom-form-displayer', ouiaSafe)}>
      {!formOpened && (
        <Bullseye
          {...componentOuiaProps(
            (ouiaId ? ouiaId : 'task-form-envelope-view') + '-loading-spinner',
            'task-form',
            true
          )}
        >
          <KogitoSpinner spinnerText={`Loading task form...`} />
        </Bullseye>
      )}
      <div id={`${formUUID}-form`} style={{ visibility: 'hidden' }}>
        <EmbeddedFormDisplayer
          targetOrigin={window.location.origin}
          envelopePath={'resources/form-displayer.html'}
          formContent={customForm}
          data={formData}
          context={buildTaskFormContext()}
          onOpenForm={opened => setFormOpened(opened)}
          ref={formDisplayerApiRef}
        />
      </div>
      {formOpened && formOpened.state === FormOpenedState.OPENED && (
        <Flex>
          <FlexItem>
            <FormFooter actions={formActions} enabled={!submitted} />
          </FlexItem>
        </Flex>
      )}
    </div>
  );
};

export default CustomTaskFormDisplayer;
