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

import uuidv4 from 'uuid';
import {
  ActionGroup,
  Button,
  Form,
  Grid,
  GridItem
} from '@patternfly/react-core';
import * as Babel from '@babel/standalone';
import ReactDOM from 'react-dom';
import * as Patternfly from '@patternfly/react-core';
import { FormArgs } from '../../../api';

interface ReactFormRendererProps {
  content: FormArgs;
}

const ReactFormRenderer: React.FC<ReactFormRendererProps> = ({ content }) => {
  // @ts-ignore
  const [formName, setFormName] = useState<string>();
  // const [source, setSource] = useState<string>();

  const renderform = () => {
    if (content && content.source) {
      const source = content.source['source-content'];
      window.React = React;
      window.ReactDOM = ReactDOM;

      // @ts-ignore
      window.PatternFlyReact = Patternfly;

      const container: HTMLElement = document.getElementById('formContainer');
      container.innerHTML = '';
      const id = uuidv4();
      const formContainer: HTMLElement = document.createElement('div');
      formContainer.id = id;

      container.appendChild(formContainer);

      try {
        const react = Babel.transform(source, {
          presets: [
            'react',
            [
              'typescript',
              {
                allExtensions: true,
                isTSX: true
              }
            ]
          ]
        }).code;

        const compiledReact = react;

        const scriptElement: HTMLScriptElement = document.createElement(
          'script'
        );

        // @ts-ignore
        window.PatternFly = window.PatternFlyReact;

        scriptElement.type = 'module';

        const content = `
        const { useState } = React;
        const { Form, Checkbox, Card, CardBody, FormGroup, TextInput } = PatternFlyReact;
       
        
        ${compiledReact}
        const target = document.getElementById("${id}");
        const element = window.React.createElement(${formName}, {});
        window.ReactDOM.render(element, target);
        `;
        scriptElement.text = content;

        container.appendChild(scriptElement);
      } catch (e) {
        console.error(e);
      }
    }
  };

  return (
    <Grid hasGutter>
      <GridItem span={6}>
        <Form>
          {/* <FormGroup fieldId={'formName'} label={'Form Name'}>
            <TextInput onChange={setFormName} value={formName} />
          </FormGroup>
          <FormGroup fieldId={'source'} label={'Source Code'}>
            <TextArea
              onChange={setSource}
              rows={20}
              style={{
                fontFamily: 'monospace'
              }}
            >
              {source}
            </TextArea>
          </FormGroup> */}
          <ActionGroup>
            <Button variant="primary" onClick={renderform}>
              Render Form
            </Button>
          </ActionGroup>
        </Form>
      </GridItem>
      <GridItem span={6}>
        <div
          style={{
            height: '100%'
          }}
          id={'formContainer'}
        >
          {}
        </div>
      </GridItem>
    </Grid>
  );
};

export default ReactFormRenderer;
