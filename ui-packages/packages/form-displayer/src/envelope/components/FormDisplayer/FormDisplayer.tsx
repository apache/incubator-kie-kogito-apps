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
import { FormArgs, FormInfo } from '../../../api';
import ReactFormRenderer from '../ReactFormRenderer/ReactFormRenderer';
import HtmlFormRenderer from '../HtmlFormRenderer/HtmlFormRenderer';

interface FormDisplayerProps {
  content: FormArgs;
  config: FormInfo;
}

const FormDisplayer: React.FC<FormDisplayerProps> = ({ content, config }) => {
  return (
    <>
      {config && config.type === 'TSX' ? (
        <ReactFormRenderer content={content} />
      ) : (
        <HtmlFormRenderer content={content} config={config} />
      )}
    </>
  );
};

export default FormDisplayer;
