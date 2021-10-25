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
  MockedFormDisplayerEnvelopeViewApi
} from './mocks/Mocks';
import { EnvelopeApiFactoryArgs } from '@kogito-tooling/envelope';
import {
  FormDisplayerChannelApi,
  FormDisplayerEnvelopeApi,
  FormType
} from '../../api';
import { FormDisplayerEnvelopeApiImpl } from '../FormDisplayerEnvelopeApiImpl';
import { FormDisplayerEnvelopeViewApi } from '../FormDisplayerEnvelopeView';
import { FormDisplayerEnvelopeContext } from '../FormDisplayerEnvelopeContext';

describe('FormDisplayerEnvelopeApiImpl tests', () => {
  it('initialize', () => {
    const envelopeBusController = new MockedEnvelopeBusController();
    const view = new MockedFormDisplayerEnvelopeViewApi();
    const args: EnvelopeApiFactoryArgs<
      FormDisplayerEnvelopeApi,
      FormDisplayerChannelApi,
      FormDisplayerEnvelopeViewApi,
      FormDisplayerEnvelopeContext
    > = {
      envelopeBusController,
      envelopeContext: {},
      view: () => view
    };

    const envelopeApi = new FormDisplayerEnvelopeApiImpl(args);

    envelopeApi.formDisplayer__init(
      {
        envelopeServerId: 'envelopeServerId',
        origin: 'origin'
      },
      {
        form: {
          formInfo: {
            lastModified: new Date('2021-08-23T13:26:02.130Z'),
            name: 'react_hiring_HRInterview',
            type: FormType.TSX
          },
          configuration: {
            resources: {
              scripts: {},
              styles: {}
            },
            schema: 'json schema'
          },
          source: 'react source code'
        }
      }
    );

    expect(envelopeBusController.associate).toHaveBeenCalledWith(
      'origin',
      'envelopeServerId'
    );
    expect(view.initForm).toHaveBeenCalled();
  });
});
