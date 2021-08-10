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
  formContent: any;
  isSource?: boolean;
  isConfig?: boolean;
  setFormContent: any;
  code: string;
}

const FormView: React.FC<FormViewProps & OUIAProps> = ({
  code,
  formType,
  formContent,
  isSource,
  isConfig,
  setFormContent,
  ouiaId,
  ouiaSafe
}) => {
  // const getFormLanguage = (): Language => {
  //   if (isSource && formType) {
  //     switch (formType) {
  //       case 'tsx':
  //         return Language.typescript;
  //       case 'html':
  //         return Language.html;
  //     }
  //   } else if (isConfig) {
  //     return Language.json;
  //   }
  // };
  //  function formatJSON(val) {
  //     try {
  //       const res = JSON.parse(JSON.stringify(val));
  //       return JSON.stringify(res, null, 2)
  //     } catch {
  //       const errorJson = {
  //         "error": `parsing json`
  //       }
  //       return JSON.stringify(errorJson, null, 2)
  //     }
  //   }
  const editorDidMount = (editor, monaco) => {
    setTimeout(() => {
      console.log(editor);
      monaco.languages.registerDocumentFormattingEditProvider('json', {
        provideDocumentFormattingEdits: function(model, options, token) {
          return [
            {
              range: {
                startLineNumber: 1,
                startColumn: 1,
                endLineNumber: 1,
                endColumn: 1
              },
              text: 'a'
            }
          ];
        }
      });
      editor.trigger('anyString', 'editor.action.formatDocument');
    }, 4000);
  };
  // console.log(code)
  // const onCodeChange = (value) => {
  //   if (isSource) {
  //     const newFormContent = JSON.parse(JSON.stringify(formContent));
  //     newFormContent['source']['sourceContent'] = value;
  //     setFormContent(newFormContent)
  //   }else if(isConfig){
  //     const newFormContent = JSON.parse(JSON.stringify(formContent));
  //     newFormContent['formConfiguration'] = value;
  //     setFormContent(newFormContent)
  //   }
  // }
  return (
    <div
      {...componentOuiaProps(ouiaId, 'form-view', ouiaSafe)}
      id="code-editor-test"
    >
      <CodeEditor
        id="code-editor-test"
        isCopyEnabled
        isDarkTheme={false}
        isLineNumbersVisible={true}
        isReadOnly={false}
        isMinimapVisible={false}
        isLanguageLabelVisible
        code={JSON.stringify(code)}
        language={Language.json}
        height="600px"
        onEditorDidMount={editorDidMount}
        // onChange={onCodeChange}
      />
      {/* <MonacoEditor
        width="800"
        height="600"
        language="javascript"
        theme="vs-dark"
        value={ [
          '"use strict";',
          'function Person(age) {',
          '	if (age) {',
          '		this.age = age;',
          '	}',
          '}',
          'Person.prototype.getAge = function () {',
          '	return this.age;',
          '};'
        ].join('\n')}
        options={{selectOnLineNumbers: true}}
       // onChange={onChange}
        editorDidMount={editorDidMount}
      /> */}
      <div id="render"></div>
    </div>
  );
};

export default FormView;
