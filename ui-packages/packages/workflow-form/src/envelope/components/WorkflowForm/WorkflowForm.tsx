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

import React, { useCallback, useState } from 'react';
import { componentOuiaProps, OUIAProps } from '@kogito-apps/ouia-tools';
import { WorkflowDefinition, WorkflowFormDriver } from '../../../api';
import {
  Form,
  FormGroup,
  TextInput,
  ActionGroup,
  Button
} from '@patternfly/react-core';
import { CodeEditor, Language } from '@patternfly/react-code-editor';

export interface WorkflowFormProps {
  workflowDefinition: WorkflowDefinition;
  driver: WorkflowFormDriver;
}

const WorkflowForm: React.FC<WorkflowFormProps & OUIAProps> = ({
  workflowDefinition,
  driver,
  ouiaId,
  ouiaSafe
}) => {
  const [type, setType] = useState<string>('');
  const [data, setData] = useState<string>('');

  const onSubmit = useCallback(() => {
    driver.startWorkflow({
      type,
      data
    });
  }, [driver, type, data]);

  const resetForm = useCallback(() => {
    setType('');
    setData('');
  }, []);

  return (
    <div {...componentOuiaProps(ouiaId, 'workflow-form', ouiaSafe)}>
      <Form isHorizontal>
        <FormGroup label="Type" isRequired fieldId="formType">
          <TextInput
            value={type}
            isRequired
            type="text"
            id="formType"
            name="formType"
            onChange={setType}
          />
        </FormGroup>
        <FormGroup label="Data" isRequired fieldId="formData">
          <CodeEditor
            isDarkTheme={false}
            isLineNumbersVisible={true}
            isReadOnly={false}
            isCopyEnabled={false}
            isMinimapVisible={true}
            isLanguageLabelVisible={false}
            code={data}
            language={Language.json}
            height="400px"
            onChange={setData}
          />
        </FormGroup>
        <ActionGroup>
          <Button variant="primary" onClick={onSubmit}>
            Send
          </Button>
          <Button variant="secondary" onClick={resetForm}>
            Reset
          </Button>
        </ActionGroup>
      </Form>
    </div>
  );
};

export default WorkflowForm;
