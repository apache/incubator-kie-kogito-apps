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
import { getWrapper, getWrapperAsync } from '@kogito-apps/components-common';
import { MockedJobsManagementDriver } from '../../../../api/mocks/MockedJobsManagementDriver';
import JobsManagement from '../JobsManagement';
import { JobStatus } from '../../../../types';
import { OrderBy } from '../../../../api';
import { Jobs } from '../__mocks__/mockData';
import { act } from 'react-dom/test-utils';

jest.mock('../../JobsManagementToolbar/JobsManagementToolbar');
jest.mock('../../JobsManagementTable/JobsManagementTable');
describe('JobsManagement component tests', () => {
  const props = {
    ouiaId: null,
    ouiaSafe: true,
    driver: new MockedJobsManagementDriver(),
    isEnvelopeConnectedToChannel: true
  };
  it('Snapshot tests with default props', async () => {
    // @ts-ignore
    await props.driver.query.mockImplementationOnce(() =>
      Promise.resolve(Jobs)
    );
    const wrapper = await getWrapperAsync(
      <JobsManagement {...props} />,
      'JobsManagement'
    );

    expect(wrapper).toMatchSnapshot();
  });

  it('Test LoadMore handler', async () => {
    // @ts-ignore
    await props.driver.query.mockImplementationOnce(() =>
      Promise.resolve(Jobs)
    );
    const wrapper = await getWrapperAsync(
      <JobsManagement {...props} />,
      'JobsManagement'
    );
    console.log('wrapper', wrapper.debug());
  });

  it('Test onRefresh function', async () => {
    let wrapper = getWrapper(<JobsManagement {...props} />, 'JobsManagement');
    wrapper = wrapper.update();
    const selectedStatus = [JobStatus.Scheduled];
    const orderBy = { lastUpdate: OrderBy.ASC };
    await act(async () => {
      wrapper
        .find('MockedJobsManagementToolbar')
        .props()
        ['onRefresh']();
    });
    expect(props.driver.initialLoad).toHaveBeenCalledWith(
      selectedStatus,
      orderBy
    );
    expect(props.driver.query).toHaveBeenCalledWith(0, 10);
  });
});
