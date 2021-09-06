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

import React, { useState, useEffect } from 'react';
import { FormArgs, FormInfo } from '../../../api';
// import uuidv4 from 'uuid';

interface HtmlFormRendererProps {
  content: FormArgs;
  config: FormInfo;
}

const HtmlFormRenderer: React.FC<HtmlFormRendererProps> = ({ content }) => {
  const [source, setSource] = useState<string>();
  let resources = {} as any;

  useEffect(() => {
    if (content && content.source) {
      setSource(content.source['source-content']);
      resources = { ...content.formConfiguration.resources };
      renderResources();
    }
  }, [content]);

  const renderResources = () => {
    const container: HTMLElement = document.getElementById('script-container');
    for (const key in resources.scripts) {
      const script = document.createElement('script');

      script.src = resources.scripts[key];
      container.appendChild(script);
    }

    for (const key in resources.styles) {
      const link = document.createElement('link');
      link.rel = 'stylesheet';
      link.href = resources.styles[key];
      container.appendChild(link);
    }
  };

  return (
    <div id="script-container">
      <div dangerouslySetInnerHTML={{ __html: source }} />
    </div>
  );
};

export default HtmlFormRenderer;
