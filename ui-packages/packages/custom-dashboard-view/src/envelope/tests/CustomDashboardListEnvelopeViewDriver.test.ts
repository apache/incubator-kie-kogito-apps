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
  MessageBusClientApi,
  RequestPropertyNames
} from '@kogito-tooling/envelope-bus/dist/api';
import { MockedMessageBusClientApi } from './mocks/Mocks';
import CustomDashboardViewEnvelopeViewDriver from '../CustomDashboardListEnvelopeViewDriver';
import {
  CustomDashboardFilter,
  CustomDashboardInfo,
  CustomDashboardListChannelApi
} from '../../api';

let channelApi: MessageBusClientApi<CustomDashboardListChannelApi>;
let requests: Pick<
  CustomDashboardListChannelApi,
  RequestPropertyNames<CustomDashboardListChannelApi>
>;
let driver: CustomDashboardViewEnvelopeViewDriver;

describe('FormsListEnvelopeViewDriver tests', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    channelApi = new MockedMessageBusClientApi();
    requests = channelApi.requests;
    driver = new CustomDashboardViewEnvelopeViewDriver(channelApi);
  });

  describe('Requests', () => {
    it('get forms query', () => {
      driver.getCustomDashboardsQuery();
      expect(
        requests.customDashboardList__getCustomDashboardQuery()
      ).toHaveBeenCalled();
    });

    it('getFormFilter', () => {
      driver.getCustomDashboardFilter();
      expect(requests.customDashboardList__getFormFilter()).toHaveBeenCalled();
    });

    it('applyFilter', () => {
      const filter: CustomDashboardFilter = {
        customDashboardNames: ['dashboard']
      };
      driver.applyFilter(filter);
      expect(requests.customDashboardList__applyFilter).toHaveBeenCalledWith(
        filter
      );
    });

    it('open dashboard', () => {
      const formData: CustomDashboardInfo = {
        name: 'dashboard1',
        path: '/user/home',
        lastModified: new Date(new Date('2022-07-11T18:30:00.000Z'))
      };
      driver.openDashboard(formData);
      expect(requests.customDashboardList__openDashboard).toHaveBeenCalledWith(
        formData
      );
    });
  });
});
