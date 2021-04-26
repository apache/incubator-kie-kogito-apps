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

import React, { useCallback, useMemo } from 'react';
import { EnvelopeServer } from '@kogito-tooling/envelope-bus/dist/channel';
import { EmbeddedEnvelopeFactory } from '@kogito-tooling/envelope/dist/embedded';
import { ContainerType } from '@kogito-tooling/envelope/dist/api';
import { EnvelopeBusMessage } from '@kogito-tooling/envelope-bus/dist/api';
import { init } from '../envelope';
import {
  JobsManagementApi,
  JobsManagementChannelApi,
  JobsManagementEnvelopeApi,
  JobsManagementDriver
} from '../api';
import { JobsManagementChannelApiImpl } from './JobsManagementChannelApiImpl';

export interface Props {
  targetOrigin: string;
  driver: JobsManagementDriver;
}

export const EmbeddedJobsManagement = React.forwardRef<
  JobsManagementApi,
  Props
>((props, forwardedRef) => {
  const pollInit = useCallback(
    (
      envelopeServer: EnvelopeServer<
        JobsManagementChannelApi,
        JobsManagementEnvelopeApi
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
          postMessage<D, Type>(
            message: EnvelopeBusMessage<D, Type>,
            targetOrigin?: string,
            transfer?: any
          ) {
            window.parent.postMessage(message, '*', transfer);
          }
        }
      });

      return envelopeServer.envelopeApi.requests.jobsManagement__init({
        origin: envelopeServer.origin,
        envelopeServerId: envelopeServer.id
      });
    },
    []
  );

  const refDelegate = useCallback(
    (
      envelopeServer: EnvelopeServer<
        JobsManagementChannelApi,
        JobsManagementEnvelopeApi
      >
    ): JobsManagementApi => ({}),
    []
  );

  const EmbeddedEnvelope = useMemo(() => {
    return EmbeddedEnvelopeFactory({
      api: new JobsManagementChannelApiImpl(props.driver),
      origin: props.targetOrigin,
      refDelegate,
      pollInit,
      config: { containerType: ContainerType.DIV }
    });
  }, []);

  return <EmbeddedEnvelope ref={forwardedRef} />;
});
