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
import { JobStatus } from '@kogito-apps/management-console-shared';
import { OrderBy } from '../../../../api';
import { Jobs } from '../__mocks__/mockData';
import { act } from 'react-dom/test-utils';

jest.mock('../../JobsManagementToolbar/JobsManagementToolbar');
jest.mock('../../JobsManagementTable/JobsManagementTable');

const MockedLoadMore = (): React.ReactElement => {
  return <></>;
};

const MockedJobsCancelModal = (): React.ReactElement => {
  return <></>;
};

const MockedJobsDetailsModal = (): React.ReactElement => {
  return <></>;
};

const MockedJobsRescheduleModal = (): React.ReactElement => {
  return <></>;
};

jest.mock('@kogito-apps/components-common', () => ({
  ...jest.requireActual('@kogito-apps/components-common'),
  LoadMore: () => {
    return <MockedLoadMore />;
  }
}));

jest.mock('@kogito-apps/management-console-shared', () => ({
  ...jest.requireActual('@kogito-apps/management-console-shared'),
  JobsCancelModal: () => {
    return <MockedJobsCancelModal />;
  },
  JobsDetailsModal: () => {
    return <MockedJobsDetailsModal />;
  },
  JobsRescheduleModal: () => {
    return <MockedJobsRescheduleModal />;
  }
}));
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
    const selectedStatus = [JobStatus.Scheduled];
    const orderBy = { lastUpdate: OrderBy.ASC };
    const wrapper = await getWrapperAsync(
      <JobsManagement {...props} />,
      'JobsManagement'
    );
    await act(async () => {
      wrapper
        .find('LoadMore')
        .props()
        ['getMoreItems']();
    });
    expect(props.driver.initialLoad).toHaveBeenCalledWith(
      selectedStatus,
      orderBy
    );
    expect(props.driver.query).toHaveBeenCalledWith(0, 10);
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

  it('Test ResetToDefault method', async () => {
    const wrapper = getWrapper(<JobsManagement {...props} />, 'JobsManagement');
    await act(async () => {
      wrapper
        .find('MockedJobsManagementToolbar')
        .props()
        ['onResetToDefault']();
    });
    expect(
      wrapper.find('MockedJobsManagementToolbar').props()['selectedStatus']
    ).toEqual([JobStatus.Scheduled]);
    expect(
      wrapper.find('MockedJobsManagementToolbar').props()['chips']
    ).toEqual([JobStatus.Scheduled]);
  });

  it('Test Ouiasafe prop', async () => {
    const wrapper = getWrapper(
      <JobsManagement {...{ ...props, ouiaSafe: false }} />,
      'JobsManagement'
    );
    await act(async () => {
      expect(wrapper.find('JobsManagement').props()['ouiaSafe']).toEqual(false);
    });
  });

  it('Test Job cancel modal', async () => {
    // @ts-ignore
    await props.driver.query.mockImplementationOnce(() =>
      Promise.resolve(Jobs)
    );
    let wrapper = getWrapper(<JobsManagement {...props} />, 'JobsManagement');
    await act(async () => {
      wrapper
        .find('MockedJobsManagementTable')
        .props()
        ['handleCancelModalToggle']();
    });
    wrapper = wrapper.update();
    expect(wrapper.find('JobsCancelModal').props()['isModalOpen']).toEqual(
      true
    );
  });

  it('Test Job details modal', async () => {
    // @ts-ignore
    await props.driver.query.mockImplementationOnce(() =>
      Promise.resolve(Jobs)
    );
    let wrapper = getWrapper(<JobsManagement {...props} />, 'JobsManagement');
    await act(async () => {
      wrapper
        .find('MockedJobsManagementTable')
        .props()
        ['setSelectedJob'](Jobs[0]);
    });
    wrapper = wrapper.update();
    await act(async () => {
      wrapper
        .find('MockedJobsManagementTable')
        .props()
        ['handleDetailsToggle']();
    });
    wrapper = wrapper.update();
    expect(wrapper.find('JobsDetailsModal').props()['isModalOpen']).toEqual(
      true
    );
  });

  it('Test Job reschedule modal', async () => {
    // @ts-ignore
    await props.driver.query.mockImplementationOnce(() =>
      Promise.resolve(Jobs)
    );
    const wrapper = getWrapper(<JobsManagement {...props} />, 'JobsManagement');
    await act(async () => {
      wrapper
        .find('MockedJobsManagementTable')
        .props()
        ['handleRescheduleToggle']();
    });
  });

  it('Test Bulk cancel method', async () => {
    // @ts-ignore
    await props.driver.query.mockImplementationOnce(() =>
      Promise.resolve(Jobs)
    );
    const successJobs = [];
    const failedJobs = [];
    // @ts-ignore
    await props.driver.bulkCancel.mockImplementationOnce(() =>
      Promise.resolve({ successJobs, failedJobs })
    );
    let wrapper = getWrapper(<JobsManagement {...props} />, 'JobsManagement');
    let jobOperations;
    await act(async () => {
      jobOperations = wrapper.find('MockedJobsManagementToolbar').props()[
        'jobOperations'
      ];
      jobOperations.CANCEL.functions.perform();
    });

    wrapper = wrapper.update();
    expect(props.driver.bulkCancel).toHaveBeenCalled();
  });
});
