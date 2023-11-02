/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
import {
  MockedEnvelopeClient,
  MockedRuntimeToolsDevUIEnvelopeViewApi
} from './mocks/Mocks';
import { EnvelopeApiFactoryArgs } from '@kie-tools-core/envelope';
import {
  RuntimeToolsDevUIChannelApi,
  RuntimeToolsDevUIEnvelopeApi
} from '../../api';
import { RuntimeToolsDevUIEnvelopeApiImpl } from '../RuntimeToolsDevUIEnvelopeApiImpl';
import { RuntimeToolsDevUIEnvelopeContextType } from '../RuntimeToolsDevUIEnvelopeContext';
import { RuntimeToolsDevUIEnvelopeViewApi } from '../RuntimeToolsDevUIEnvelopeViewApi';

describe('JobsManagementEnvelopeApiImpl tests', () => {
  it('initialize', () => {
    const envelopeClient = new MockedEnvelopeClient();
    const view = new MockedRuntimeToolsDevUIEnvelopeViewApi();
    const args: EnvelopeApiFactoryArgs<
      RuntimeToolsDevUIEnvelopeApi,
      RuntimeToolsDevUIChannelApi,
      RuntimeToolsDevUIEnvelopeViewApi,
      RuntimeToolsDevUIEnvelopeContextType
    > = {
      envelopeClient,
      envelopeContext: {} as any,
      viewDelegate: () => Promise.resolve(() => view)
    };

    const envelopeApi = new RuntimeToolsDevUIEnvelopeApiImpl(args);

    envelopeApi.runtimeToolsDevUI_initRequest(
      {
        envelopeServerId: 'envelopeServerId',
        origin: 'origin'
      },
      {
        users: [],
        dataIndexUrl: '',
        trustyServiceUrl: '',
        page: '',
        devUIUrl: '',
        openApiPath: '',
        isProcessEnabled: true,
        isTracingEnabled: true
      }
    );

    expect(envelopeClient.associate).toHaveBeenCalledWith(
      'origin',
      'envelopeServerId'
    );
  });
});
