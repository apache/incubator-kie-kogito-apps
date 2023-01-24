/*
 * Copyright 2022 Red Hat, Inc. and/or its affiliates.
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
import { componentOuiaProps, OUIAProps } from '@kogito-apps/ouia-tools';
import {
  ActionType,
  FormRendererApi,
  FormAction,
  FormRenderer
} from '@kogito-apps/components-common';
import { WorkflowFormDriver } from '../../../api/WorkflowFormDriver';
import { WorkflowDefinition } from '../../../api';

export interface CustomWorkflowFormProps {
  customFormSchema: Record<string, any>;
  driver: WorkflowFormDriver;
  workflowDefinition: WorkflowDefinition;
}
const CustomWorkflowForm: React.FC<CustomWorkflowFormProps & OUIAProps> = ({
  workflowDefinition,
  customFormSchema,
  driver,
  ouiaId,
  ouiaSafe
}) => {
  const formRendererApi = React.useRef<FormRendererApi>();

  const formAction: FormAction[] = [
    {
      name: 'Start'
    },
    {
      name: 'Reset',
      execute: () => {
        formRendererApi?.current?.doReset();
      },
      actionType: ActionType.RESET
    }
  ];

  const startWorkflow = (data: Record<string, any>): void => {
    driver.startWorkflow(workflowDefinition.endpoint, data).then(() => {
      formRendererApi?.current?.doReset();
    });
  };

  return (
    <div {...componentOuiaProps(ouiaId, 'custom-workflow-form', ouiaSafe)}>
      <FormRenderer
        formSchema={customFormSchema}
        readOnly={false}
        onSubmit={startWorkflow}
        formActions={formAction}
        ref={formRendererApi}
      />
    </div>
  );
};

export default CustomWorkflowForm;
