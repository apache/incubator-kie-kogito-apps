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

import React from 'react';
import { OUIAProps, componentOuiaProps } from '@kogito-apps/ouia-tools';
import { CodeEditor, Language } from '@patternfly/react-code-editor';
export interface FormViewProps {
  formType?: string;
  isSource?: boolean;
  isConfig?: boolean;
  code: string;
}

const FormView: React.FC<FormViewProps & OUIAProps> = ({
  code,
  formType,
  isSource = false,
  isConfig = false,
  ouiaId,
  ouiaSafe
}) => {
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
    if (formType.toLowerCase() === 'tsx') {
      monaco.languages.typescript.typescriptDefaults.setCompilerOptions({
        jsx: 'react'
      });

      monaco.languages.typescript.typescriptDefaults.setDiagnosticsOptions({
        noSemanticValidation: false,
        noSyntaxValidation: false
      });
    }
    setTimeout(() => {
      editor.trigger('anyString', 'editor.action.formatDocument');
    }, 500);
  };

  return (
    <div {...componentOuiaProps(ouiaId, 'form-view', ouiaSafe)}>
      <CodeEditor
        isCopyEnabled
        isDarkTheme={false}
        isLineNumbersVisible={true}
        isReadOnly={false}
        isMinimapVisible={false}
        isLanguageLabelVisible
        code={code}
        language={getFormLanguage()}
        height="800px"
        onEditorDidMount={editorDidMount}
      />
    </div>
  );
};

export default FormView;
