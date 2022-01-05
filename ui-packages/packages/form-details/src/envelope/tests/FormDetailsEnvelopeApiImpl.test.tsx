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

import {
  MockedEnvelopeBusController,
  MockedFormDetailsEnvelopeViewApi
} from './mocks/Mocks';
import { FormType } from '@kogito-apps/forms-list';
import { EnvelopeApiFactoryArgs } from '@kogito-tooling/envelope';
import { FormDetailsChannelApi, FormDetailsEnvelopeApi } from '../../api';
import { FormDetailsEnvelopeApiImpl } from '../FormDetailsEnvelopeApiImpl';
import { FormDetailsEnvelopeViewApi } from '../FormDetailsEnvelopeView';
import { FormDetailsEnvelopeContext } from '../FormDetailsEnvelopeContext';

describe('FormDetailsEnvelopeApiImpl tests', () => {
  it('initialize', () => {
    const envelopeBusController = MockedEnvelopeBusController;
    const view = new MockedFormDetailsEnvelopeViewApi();
    const args: EnvelopeApiFactoryArgs<
      FormDetailsEnvelopeApi,
      FormDetailsChannelApi,
      FormDetailsEnvelopeViewApi,
      FormDetailsEnvelopeContext
    > = {
      envelopeBusController,
      envelopeContext: {},
      view: () => view
    };

    const envelopeApi = new FormDetailsEnvelopeApiImpl(args);

    envelopeApi.formDetails__init(
      {
        envelopeServerId: 'envelopeServerId',
        origin: 'origin'
      },
      {
        name: 'form1',
        type: FormType.HTML,
        lastModified: new Date('2020-07-11T18:30:00.000Z')
      }
    );

    expect(envelopeBusController.associate).toHaveBeenCalledWith(
      'origin',
      'envelopeServerId'
    );
    expect(view.initialize).toHaveBeenCalled();
  });
});
