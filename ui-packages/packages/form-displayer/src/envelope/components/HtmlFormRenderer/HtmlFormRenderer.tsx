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

import React, { useEffect, useCallback } from 'react';
import { FormResources } from '../../../api';

interface HtmlFormRendererProps {
  source: any;
  resources: FormResources;
}

const HtmlFormRenderer: React.FC<HtmlFormRendererProps> = ({
  source,
  resources
}) => {
  useEffect(() => {
    if (source) {
      renderResources();
    }
  }, [resources]);

  const renderTags = (container): void => {
    for (const key in resources.scripts) {
      const script: HTMLScriptElement = document.createElement('script');

      script.src = resources.scripts[key];
      container.appendChild(script);
    }

    for (const key in resources.styles) {
      const link: HTMLLinkElement = document.createElement('link');
      link.rel = 'stylesheet';
      link.href = resources.styles[key];
      container.appendChild(link);
    }
  };

  const renderResources = useCallback((): void => {
    const container: HTMLElement = document.getElementById('script-container');
    const scripts: HTMLCollectionOf<HTMLScriptElement> = container.getElementsByTagName(
      'script'
    );
    const styles: HTMLCollectionOf<HTMLLinkElement> = container.getElementsByTagName(
      'link'
    );
    if (scripts.length > 0 || styles.length > 0) {
      let scriptIndex: number = scripts.length;
      let styleIndex: number = styles.length;
      while (scriptIndex--) {
        container.removeChild(scripts[scriptIndex]);
      }
      while (styleIndex--) {
        container.removeChild(styles[styleIndex]);
      }
      renderTags(container);
    } else {
      renderTags(container);
    }
  }, [resources]);

  return (
    <div id="script-container">
      <div dangerouslySetInnerHTML={{ __html: source }} />
    </div>
  );
};

export default HtmlFormRenderer;
