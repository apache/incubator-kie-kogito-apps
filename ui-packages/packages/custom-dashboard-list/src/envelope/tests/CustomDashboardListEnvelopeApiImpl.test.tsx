/*
 * Copyright 2022 Red Hat, Inc. and/or its affiliates.
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
  MockedCustomDashboardListEnvelopeViewApi
} from './mocks/Mocks';
import { EnvelopeApiFactoryArgs } from '@kogito-tooling/envelope';
import {
  CustomDashboardListChannelApi,
  CustomDashboardListEnvelopeApi
} from '../../api';
import { CustomDashboardListEnvelopeApiImpl } from '../CustomDashboardListEnvelopeApiImpl';
import { CustomDashboardListEnvelopeViewApi } from '../CustomDashboardListEnvelopeView';
import { CustomDashboardListEnvelopeContext } from '../CustomDashboardListEnvelopeContext';

describe('CustomDashboardListEnvelopeApiImpl tests', () => {
  it('initialize', () => {
    const envelopeBusController = MockedEnvelopeBusController;
    const view = new MockedCustomDashboardListEnvelopeViewApi();
    const args: EnvelopeApiFactoryArgs<
      CustomDashboardListEnvelopeApi,
      CustomDashboardListChannelApi,
      CustomDashboardListEnvelopeViewApi,
      CustomDashboardListEnvelopeContext
    > = {
      envelopeBusController,
      envelopeContext: {},
      view: () => view
    };

    const envelopeApi = new CustomDashboardListEnvelopeApiImpl(args);

    envelopeApi.customDashboardList__init({
      envelopeServerId: 'envelopeServerId',
      origin: 'origin'
    });

    expect(envelopeBusController.associate).toHaveBeenCalledWith(
      'origin',
      'envelopeServerId'
    );
    expect(view.initialize).toHaveBeenCalled();
  });
});
