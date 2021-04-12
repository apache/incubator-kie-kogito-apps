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
import axios from 'axios';
import JobsManagementTable from '../JobsManagementTable';
import {
  getWrapper,
  getWrapperAsync,
  KogitoSpinner
} from '@kogito-apps/components-common';
import { act } from 'react-dom/test-utils';
import { Dropdown, DropdownItem, KebabToggle } from '@patternfly/react-core';
import { SelectColumn } from '@patternfly/react-table';
import { JobStatus } from '../../../../types';
import { MockedJobsManagementDriver } from '../../../../api/mocks/MockedJobsManagementDriver';
jest.mock('axios');
const mockedAxios = axios as jest.Mocked<typeof axios>;

describe('Jobs management table component tests', () => {
  const props = {
    jobs: [
      {
        callbackEndpoint:
          'http://localhost:8080/management/jobs/travels/instances/5c56eeff-4cbf-3313-a325-4c895e0afced/timers/6e74a570-31c8-4020-bd70-19be2cb625f3_0',
        endpoint: 'http://localhost:4000/jobs',
        expirationTime: null,
        id: '6e74a570-31c8-4020-bd70-19be2cb625f3_0',
        lastUpdate: new Date('2020-08-27T03:35:50.147Z'),
        priority: 0,
        processId: 'travels',
        processInstanceId: '8035b580-6ae4-4aa8-9ec0-e18e19809e0b',
        repeatInterval: null,
        repeatLimit: null,
        retries: 0,
        rootProcessId: null,
        scheduledId: '0',
        status: JobStatus.Executed,
        executionCounter: 1
      },
      {
        callbackEndpoint:
          'http://localhost:8080/management/jobs/travels/instances/5c56eeff-4cbf-3313-a325-4c895e0afced/timers/dad3aa88-5c1e-4858-a919-6123c675a0fa_0',
        endpoint: 'http://localhost:4000/jobs',
        expirationTime: new Date('2020-08-27T04:35:54.631Z'),
        id: 'dad3aa88-5c1e-4858-a919-6123c675a0fa_0',
        lastUpdate: new Date('2020-08-27T03:35:54.635Z'),
        priority: 0,
        processId: 'travels',
        processInstanceId: '8035b580-6ae4-4aa8-9ec0-e18e19809e0b',
        repeatInterval: null,
        repeatLimit: null,
        retries: 0,
        rootProcessId: '',
        scheduledId: null,
        status: JobStatus.Scheduled,
        executionCounter: 2
      },
      {
        callbackEndpoint:
          'http://localhost:8080/management/jobs/travels/instances/5c56eeff-4cbf-3313-a325-4c895e0afced/timers/dad3aa88-5c1e-4858-a919-6123c675a0fa_0',
        endpoint: 'http://localhost:4000/jobs',
        expirationTime: new Date('2020-08-27T04:35:54.631Z'),
        id: '2234dde-npce1-2908-b3131-6123c675a0fa_0',
        lastUpdate: new Date('2020-08-27T03:35:54.635Z'),
        priority: 0,
        processId: 'travels',
        processInstanceId: '8035b580-6ae4-4aa8-9ec0-e18e19809e0b',
        repeatInterval: null,
        repeatLimit: null,
        retries: 0,
        rootProcessId: '',
        scheduledId: null,
        status: JobStatus.Canceled,
        executionCounter: 4
      }
    ],
    driver: new MockedJobsManagementDriver(),
    doQueryJobs: jest.fn(),
    handleDetailsToggle: jest.fn(),
    handleRescheduleToggle: jest.fn(),
    handleCancelModalToggle: jest.fn(),
    setModalTitle: jest.fn(),
    setModalContent: jest.fn(),
    setOrderBy: jest.fn(),
    setSelectedJob: jest.fn(),
    selectedJobInstances: [],
    setSelectedJobInstances: jest.fn(),
    sortBy: {},
    setSortBy: jest.fn(),
    isActionPerformed: true,
    setIsActionPerformed: jest.fn(),
    isLoading: false
  };
  it('Snapshot test with default props', async () => {
    const wrapper = await getWrapperAsync(
      <JobsManagementTable {...props} />,
      'JobsManagementTable'
    );
    expect(wrapper).toMatchSnapshot();
  });

  it('onSelect tests', async () => {
    const wrapperWithoutSelectedInstances = getWrapper(
      <JobsManagementTable {...props} />,
      'JobsManagementTable'
    );
    // select 1 row
    await act(async () => {
      wrapperWithoutSelectedInstances
        .find(SelectColumn)
        .at(2)
        .simulate('change');
    });
    expect(props.setSelectedJobInstances).toHaveBeenCalled();
    const wrapperWithSelectedInstances = getWrapper(
      <JobsManagementTable
        {...{ ...props, selectedJobInstances: [{ ...props.jobs[1] }] }}
      />,
      'JobsManagementTable'
    );
    //deselect 1 row
    await act(async () => {
      wrapperWithSelectedInstances
        .find(SelectColumn)
        .at(2)
        .simulate('change');
    });
    expect(props.setSelectedJobInstances).toHaveBeenCalled();
    //select all rows
    await act(async () => {
      wrapperWithoutSelectedInstances
        .find(SelectColumn)
        .at(0)
        .simulate('change');
    });
    expect(props.setSelectedJobInstances).toHaveBeenCalled();
    const wrapperWithAllSelected = getWrapper(
      <JobsManagementTable
        {...{ ...props, selectedJobInstances: [...props.jobs] }}
      />,
      'JobsManagementTable'
    );
    //deselect all rows
    await act(async () => {
      wrapperWithAllSelected
        .find(SelectColumn)
        .at(0)
        .simulate('change');
    });
    expect(props.setSelectedJobInstances).toHaveBeenCalled();
  });

  it('test sorting controls', async () => {
    const handleSort: any = jest.spyOn(React, 'useState');
    handleSort.mockImplementation(sortBy => [sortBy, props.setSortBy]);
    let wrapper = await getWrapperAsync(
      <JobsManagementTable {...props} />,
      'JobsManagementTable'
    );
    const event = { target: { innerText: 'Last update' } };
    const index = 1;
    const direction = 'asc';
    await act(async () => {
      wrapper
        .find('Table')
        .props()
        ['onSort'](event, index, direction);
    });
    wrapper = wrapper.update();
    expect(props.setSortBy).toBeTruthy();
  });

  it('test job details action', async () => {
    let wrapper = await getWrapperAsync(
      <JobsManagementTable {...props} />,
      'JobsManagementTable'
    );
    wrapper
      .find(Dropdown)
      .at(1)
      .find(KebabToggle)
      .find('button')
      .at(0)
      .simulate('click');
    wrapper = wrapper.update();
    expect(
      wrapper
        .find(DropdownItem)
        .at(0)
        .find('button')
        .children()
        .contains('Details')
    ).toBeTruthy();
    await act(async () => {
      wrapper
        .find(DropdownItem)
        .at(0)
        .find('button')
        .simulate('click');
    });
    expect(props.handleDetailsToggle).toHaveBeenCalled();
  });

  it('test job cancel action', async () => {
    const modalTitle = 'success';
    const modalContent = 'Cancel successfull';
    // @ts-ignore
    props.driver.cancelJob.mockImplementationOnce(() =>
      Promise.resolve({ modalTitle, modalContent })
    );
    mockedAxios.delete.mockResolvedValue({});
    const jobCancelSpy = jest.spyOn(props.driver, 'cancelJob');
    let wrapper = await getWrapperAsync(
      <JobsManagementTable {...props} />,
      'JobsManagementTable'
    );
    wrapper
      .find(Dropdown)
      .at(1)
      .find(KebabToggle)
      .find('button')
      .at(0)
      .simulate('click');
    wrapper = wrapper.update();
    expect(
      wrapper
        .find(DropdownItem)
        .at(2)
        .find('button')
        .children()
        .contains('Cancel')
    ).toBeTruthy();
    await act(async () => {
      wrapper
        .find(DropdownItem)
        .at(2)
        .find('button')
        .simulate('click');
    });
    expect(jobCancelSpy).toHaveBeenCalled();
  });

  it('test job reschedule action', async () => {
    mockedAxios.delete.mockResolvedValue({});
    let wrapper = await getWrapperAsync(
      <JobsManagementTable {...props} />,
      'JobsManagementTable'
    );
    wrapper
      .find(Dropdown)
      .at(1)
      .find(KebabToggle)
      .find('button')
      .at(0)
      .simulate('click');
    wrapper = wrapper.update();
    expect(
      wrapper
        .find(DropdownItem)
        .at(1)
        .find('button')
        .children()
        .contains('Reschedule')
    ).toBeTruthy();
    await act(async () => {
      wrapper
        .find(DropdownItem)
        .at(1)
        .find('button')
        .simulate('click');
    });
    expect(props.handleRescheduleToggle).toHaveBeenCalled();
  });

  it('test checkNotEmpty method', async () => {
    const jobs = [];
    const wrapper = await getWrapperAsync(
      <JobsManagementTable {...{ ...props, jobs }} />,
      'JobsManagementTable'
    );
    expect(wrapper.find('Table').props()['onSelect']).toEqual(null);
  });

  it('test isLoading true', async () => {
    const rows = [
      {
        rowKey: '1',
        cells: [
          {
            props: { colSpan: 8 },
            title: <KogitoSpinner spinnerText={'Loading jobs list...'} />
          }
        ]
      }
    ];
    const isLoading = true;
    const wrapper = await getWrapperAsync(
      <JobsManagementTable {...{ ...props, isLoading }} />,
      'JobsManagementTable'
    );

    expect(wrapper.find('Table').props()['rows']).toEqual(rows);
  });
});
