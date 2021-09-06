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

import uuidv4 from 'uuid';
import * as Babel from '@babel/standalone';
import ReactDOM from 'react-dom';
import * as Patternfly from '@patternfly/react-core';
import { FormArgs } from '../../../api';

interface ReactFormRendererProps {
  content: FormArgs;
}

const ReactFormRenderer: React.FC<ReactFormRendererProps> = ({ content }) => {
  const [source, setSource] = useState<string>();

  useEffect(() => {
    if (content && content.source) {
      setSource(content.source['source-content']);
      renderform();
    }
  }, [content]);

  const renderform = () => {
    if (source) {
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

      const reactReg = /import React, {[^}]*}.*(?='react').*/gim;
      const patternflyReg = /import {[^}]*}.*(?='@patternfly\/react-core').*/gim;
      const regexvalueReact = new RegExp(reactReg);
      const reactImport = regexvalueReact.exec(source);
      const reg = /\{([^)]+)\}/;
      const reactElements = reg.exec(reactImport[0])[1];
      console.log('react', reactElements);
      const regexvaluePat = new RegExp(patternflyReg);
      const patternflyImport = regexvaluePat.exec(source);
      const patternflyElements = reg.exec(patternflyImport[0])[1];
      console.log('pat', patternflyElements);
      const trimmedSource = source
        .split(reactReg)
        .join('')
        .trim()
        .split(patternflyReg)
        .join('')
        .trim();
      const tempSource = trimmedSource;
      const formName = tempSource.split(':')[0].split('const ')[1];
      try {
        const react = Babel.transform(trimmedSource, {
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
        const {${reactElements}} = React;
        const {${patternflyElements}} = PatternFlyReact;
       
        
        ${compiledReact}
        const target = document.getElementById("${id}");
        const element = window.React.createElement(${formName}, {});
        window.ReactDOM.render(element, target);
        `;
        console.log('cone', content);
        scriptElement.text = content;

        container.appendChild(scriptElement);
      } catch (e) {
        console.error(e);
      }
    }
  };

  return (
    <div
      style={{
        height: '100%'
      }}
      id={'formContainer'}
    >
      {}
    </div>
  );
};

export default ReactFormRenderer;
