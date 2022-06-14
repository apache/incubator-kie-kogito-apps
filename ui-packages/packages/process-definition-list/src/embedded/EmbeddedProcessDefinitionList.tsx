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
import React, { useCallback, useMemo, Ref } from 'react';
import { EnvelopeServer } from '@kogito-tooling/envelope-bus/dist/channel';
import { EmbeddedEnvelopeFactory } from '@kogito-tooling/envelope/dist/embedded';
import {
  ProcessDefinitionListApi,
  ProcessDefinitionListChannelApi,
  ProcessDefinitionListEnvelopeApi,
  ProcessDefinitionListDriver
} from '../api';
import { ProcessDefinitionListChannelApiImpl } from './ProcessDefinitionListChannelApiImpl';
import { ContainerType } from '@kogito-tooling/envelope/dist/api';
import { init } from '../envelope';

export interface Props {
  targetOrigin: string;
  driver: ProcessDefinitionListDriver;
  singularProcessLabel: string;
}

export const EmbeddedProcessDefinitionList = React.forwardRef<
  ProcessDefinitionListApi,
  Props
>((props, forwardedRef: Ref<ProcessDefinitionListApi>) => {
  const pollInit = useCallback(
    (
      envelopeServer: EnvelopeServer<
        ProcessDefinitionListChannelApi,
        ProcessDefinitionListEnvelopeApi
      >,
      container: () => HTMLDivElement
    ) => {
      init({
        config: {
          containerType: ContainerType.DIV,
          envelopeId: envelopeServer.id
        },
        container: container(),
        bus: {
          postMessage(message, targetOrigin, transfer) {
            window.postMessage(message, '*', transfer);
          }
        }
      });
      return envelopeServer.envelopeApi.requests.processDefinitionList__init(
        {
          origin: envelopeServer.origin,
          envelopeServerId: envelopeServer.id
        },
        {
          singularProcessLabel: props.singularProcessLabel
        }
      );
    },
    []
  );

  const refDelegate = useCallback(
    (
      envelopeServer: EnvelopeServer<
        ProcessDefinitionListChannelApi,
        ProcessDefinitionListEnvelopeApi
      >
    ): ProcessDefinitionListApi => ({}),
    []
  );

  const EmbeddedEnvelope = useMemo(() => {
    return EmbeddedEnvelopeFactory({
      api: new ProcessDefinitionListChannelApiImpl(props.driver),
      origin: props.targetOrigin,
      refDelegate,
      pollInit,
      config: { containerType: ContainerType.DIV }
    });
  }, []);

  return <EmbeddedEnvelope ref={forwardedRef} />;
});
