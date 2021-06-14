/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import * as React from 'react';
import { RuntimeToolsDevUIEnvelopeViewApi } from './RuntimeToolsDevUIEnvelopeViewApi';
import { useImperativeHandle, useState } from 'react';
import { UserContext } from '../../../consoles-common';

export interface Props {
  userContext: UserContext;
}

export const RuntimeToolsDevUIEnvelopeViewRef: React.ForwardRefRenderFunction<
  RuntimeToolsDevUIEnvelopeViewApi,
  Props
> = (props: Props, forwardingRef) => {
  const [dataIndexUrl, setDataIndexUrl] = useState<string>('defaultUrl');

  useImperativeHandle(
    forwardingRef,
    () => {
      return {
        setDataIndexUrl: dataIndexUrl => setDataIndexUrl(dataIndexUrl)
      };
    },
    []
  );

  return (
    <>
      <div>Data Index URL: {dataIndexUrl}</div>
    </>
  );
};

export const RuntimeToolsDevUIEnvelopeView = React.forwardRef(
  RuntimeToolsDevUIEnvelopeViewRef
);
