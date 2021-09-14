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

import React, { useState } from 'react';
import { OUIAProps, componentOuiaProps } from '@kogito-apps/ouia-tools';
import {
  CodeEditor,
  CodeEditorControl,
  Language
} from '@patternfly/react-code-editor';
import { SyncAltIcon } from '@patternfly/react-icons';
import { Form } from '../../../api';
import { useFormDetailsContext } from '../contexts/FormDetailsContext';
export interface FormViewProps {
  formType?: string;
  isSource?: boolean;
  isConfig?: boolean;
  formContent: Form;
  code: string;
  setFormContent: any;
}

const FormView: React.FC<FormViewProps & OUIAProps> = ({
  code,
  formType,
  formContent,
  setFormContent,
  isSource = false,
  isConfig = false,
  ouiaId,
  ouiaSafe
}) => {
  const appContext = useFormDetailsContext();
  const [contentChange, setContentChange] = useState<Form>(null);

  const getFormLanguage = (): Language => {
    if (isSource && formType) {
      switch (formType.toLowerCase()) {
        case 'tsx':
          return Language.typescript;
        case 'html':
          return Language.html;
      }
      /* istanbul ignore else */
    } else if (isConfig) {
      return Language.json;
    }
  };

  const editorDidMount = (editor, monaco): void => {
    console.log('mount', editor, monaco);
    if (isSource && formType.toLowerCase() === 'tsx') {
      monaco.languages.typescript.typescriptDefaults.setCompilerOptions({
        jsx: 'react'
      });

      monaco.languages.typescript.typescriptDefaults.setDiagnosticsOptions({
        noSemanticValidation: false,
        noSyntaxValidation: false
      });
    }
  };

  const handleChange = (value): void => {
    console.log('view', formContent);
    if (Object.keys(formContent)[0].length > 0 && isSource) {
      const temp: Form = formContent;
      temp.source['source-content'] = value;
      setContentChange({ ...formContent, ...temp });
    } else {
      setContentChange((formContent.formConfiguration['resources'] = value));
    }
  };

  const onRefreshCode = (): void => {
    appContext.updateContent(contentChange);
    setFormContent(contentChange);
  };

  const customControl = (
    <CodeEditorControl
      icon={<SyncAltIcon />}
      aria-label="Refresh code"
      toolTipText="Refresh code"
      onClick={onRefreshCode}
      isVisible={contentChange && Object.keys(contentChange)[0].length > 0}
    />
  );
  CodeEditorControl;

  return (
    <div {...componentOuiaProps(ouiaId, 'form-view', ouiaSafe)}>
      <CodeEditor
        isCopyEnabled
        isDarkTheme={false}
        isLineNumbersVisible={true}
        isReadOnly={false}
        isMinimapVisible={false}
        isLanguageLabelVisible
        customControls={customControl}
        code={code}
        language={getFormLanguage()}
        height="800px"
        onEditorDidMount={editorDidMount}
        onChange={handleChange}
      />
    </div>
  );
};

export default FormView;
