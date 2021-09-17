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
import { BallBeat } from 'react-pure-loaders';
import ReactFormRenderer from '../ReactFormRenderer/ReactFormRenderer';
import HtmlFormRenderer from '../HtmlFormRenderer/HtmlFormRenderer';
import { Bullseye } from '@patternfly/react-core';
import '../styles.css';

interface FormDisplayerProps {
  isEnvelopeConnectedToChannel: boolean;
  content: FormArgs;
  config: FormInfo;
}

const FormDisplayer: React.FC<FormDisplayerProps> = ({
  isEnvelopeConnectedToChannel,
  content,
  config
}) => {
  const [source, setSource] = useState<string>();
  const [resources, setResources] = useState<any>();
  const [isExecuting, seIsExecuting] = useState<boolean>(false);

  useEffect(() => {
    if (isEnvelopeConnectedToChannel) {
      setSource(content.source['source-content']);
      setResources(content.formConfiguration['resources']);
    }
  }, [isEnvelopeConnectedToChannel, content]);
  return (
    <>
      {isEnvelopeConnectedToChannel && !isExecuting ? (
        <>
          {config && config.type === 'TSX' ? (
            <ReactFormRenderer
              source={source}
              resources={resources}
              seIsExecuting={seIsExecuting}
            />
          ) : (
            <HtmlFormRenderer source={source} resources={resources} />
          )}
        </>
      ) : (
        <Bullseye className="kogito-form-displayer__ball-beats">
          <BallBeat color={'#000000'} loading={!isEnvelopeConnectedToChannel} />
        </Bullseye>
      )}
    </>
  );
};

export default FormDisplayer;
