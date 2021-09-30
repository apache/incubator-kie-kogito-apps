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
  MessageBusClientApi,
  RequestPropertyNames
} from '@kogito-tooling/envelope-bus/dist/api';
import { MockedMessageBusClientApi } from './mocks/Mocks';
import FormsListEnvelopeViewDriver from '../FormsListEnvelopeViewDriver';
import { FormInfo, FormsListChannelApi } from '../../api';

let channelApi: MessageBusClientApi<FormsListChannelApi>;
let requests: Pick<
  FormsListChannelApi,
  RequestPropertyNames<FormsListChannelApi>
>;
let driver: FormsListEnvelopeViewDriver;

describe('FormsListEnvelopeViewDriver tests', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    channelApi = new MockedMessageBusClientApi();
    requests = channelApi.requests;
    driver = new FormsListEnvelopeViewDriver(channelApi);
  });

  describe('Requests', () => {
    it('get forms query', () => {
      driver.getFormsQuery();
      expect(requests.formsList__getFormsQuery).toHaveBeenCalled();
    });

    it('getFormFilter', () => {
      driver.getFormFilter();
      expect(requests.formsList__getFormFilter).toHaveBeenCalled();
    });

    it('applyFilter', () => {
      const formsFilter = {
        formNames: ['form1']
      };
      driver.applyFilter(formsFilter);
      expect(requests.formsList__applyFilter).toHaveBeenCalledWith(formsFilter);
    });

    it('open form', () => {
      const formData: FormInfo = {
        name: 'form1',
        type: 'html',
        lastModified: new Date(new Date('2020-07-11T18:30:00.000Z'))
      };
      driver.openForm(formData);
      expect(requests.formsList__openForm).toHaveBeenCalledWith(formData);
    });
  });
});
