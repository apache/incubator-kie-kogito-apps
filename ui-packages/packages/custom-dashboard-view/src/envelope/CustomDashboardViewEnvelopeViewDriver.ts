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

import { MessageBusClientApi } from '@kogito-tooling/envelope-bus/dist/api';
import {
  CustomDashboardViewChannelApi,
  CustomDashboardViewDriver
} from '../api';

/**
 * Implementation of FormsListDriver that delegates calls to the channel Api
 */
export default class CustomDashboardViewEnvelopeViewDriver
  implements CustomDashboardViewDriver {
  constructor(
    private readonly channelApi: MessageBusClientApi<
      CustomDashboardViewChannelApi
    >
  ) {}

  getCustomDashboardContent(name: string): Promise<string> {
    return this.channelApi.requests.customDashboardView__getCusmtonDashboardView(
      name
    );
  }
}
