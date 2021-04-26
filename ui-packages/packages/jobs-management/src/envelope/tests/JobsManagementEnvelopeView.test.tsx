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

import React from 'react';
import { act } from 'react-dom/test-utils';
import { getWrapper } from '@kogito-apps/components-common';
import { MockedMessageBusClientApi } from './mocks/Mocks';
import JobsManagementEnvelopeView, {
  JobsManagementEnvelopeViewApi
} from '../JobsManagementEnvelopeView';
import JobsManagement from '../components/JobsManagement/JobsManagement';

jest.mock('../components/JobsManagement/JobsManagement');

describe('JobsManagementEnvelopeView tests', () => {
  it('Snapshot', () => {
    const channelApi = new MockedMessageBusClientApi();

    const forwardRef = React.createRef<JobsManagementEnvelopeViewApi>();

    let wrapper = getWrapper(
      <JobsManagementEnvelopeView channelApi={channelApi} ref={forwardRef} />,
      'JobsManagementEnvelopeView'
    );

    expect(wrapper).toMatchSnapshot();

    act(() => {
      if (forwardRef.current) {
        forwardRef.current.initialize();
      }
    });

    wrapper = wrapper.update().find(JobsManagementEnvelopeView);

    expect(wrapper).toMatchSnapshot();

    const inbox = wrapper.find(JobsManagement);

    expect(inbox.exists()).toBeTruthy();
    expect(inbox.props().isEnvelopeConnectedToChannel).toBeTruthy();
    expect(inbox.props().driver).not.toBeNull();
  });
});
