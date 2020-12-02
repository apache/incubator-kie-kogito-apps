import React from 'react';
import JobsManagementTable from '../JobsManagementTable';
import { getWrapperAsync, GraphQL, getWrapper } from '@kogito-apps/common';
import { Dropdown, DropdownItem, KebabToggle } from '@patternfly/react-core';
import { act } from 'react-dom/test-utils';
import * as Utils from '../../../../utils/Utils';
import axios from 'axios';
import { refetchContext } from '../../../contexts';
import { SelectColumn } from '@patternfly/react-table';
jest.mock('axios');
Date.now = jest.fn(() => new Date(Date.UTC(2020, 10, 30)).valueOf());
const mockedAxios = axios as jest.Mocked<typeof axios>;

const MockedIcon = (): React.ReactElement => {
  return <></>;
};

jest.mock('@patternfly/react-icons', () => ({
  ...jest.requireActual('@patternfly/react-icons'),
  HistoryIcon: () => {
    return <MockedIcon />;
  },
  ClockIcon: () => {
    return <MockedIcon />;
  },
  BanIcon: () => {
    return <MockedIcon />;
  },
  CheckCircleIcon: () => {
    return <MockedIcon />;
  }
}));
describe('Jobs management table component tests', () => {
  const props = {
    data: {
      Jobs: [
        {
          callbackEndpoint:
            'http://localhost:8080/management/jobs/travels/instances/5c56eeff-4cbf-3313-a325-4c895e0afced/timers/6e74a570-31c8-4020-bd70-19be2cb625f3_0',
          endpoint: 'http://localhost:4000/jobs',
          expirationTime: null,
          id: '6e74a570-31c8-4020-bd70-19be2cb625f3_0',
          lastUpdate: '2020-08-27T03:35:50.147Z',
          priority: 0,
          processId: 'travels',
          processInstanceId: '8035b580-6ae4-4aa8-9ec0-e18e19809e0b',
          repeatInterval: null,
          repeatLimit: null,
          retries: 0,
          rootProcessId: null,
          scheduledId: '0',
          status: GraphQL.JobStatus.Executed,
          executionCounter: 1
        },
        {
          callbackEndpoint:
            'http://localhost:8080/management/jobs/travels/instances/5c56eeff-4cbf-3313-a325-4c895e0afced/timers/dad3aa88-5c1e-4858-a919-6123c675a0fa_0',
          endpoint: 'http://localhost:4000/jobs',
          expirationTime: '2020-08-27T04:35:54.631Z',
          id: 'dad3aa88-5c1e-4858-a919-6123c675a0fa_0',
          lastUpdate: '2020-08-27T03:35:54.635Z',
          priority: 0,
          processId: 'travels',
          processInstanceId: '8035b580-6ae4-4aa8-9ec0-e18e19809e0b',
          repeatInterval: null,
          repeatLimit: null,
          retries: 0,
          rootProcessId: '',
          scheduledId: null,
          status: GraphQL.JobStatus.Scheduled,
          executionCounter: 2
        },
        {
          callbackEndpoint:
            'http://localhost:8080/management/jobs/travels/instances/5c56eeff-4cbf-3313-a325-4c895e0afced/timers/dad3aa88-5c1e-4858-a919-6123c675a0fa_0',
          endpoint: 'http://localhost:4000/jobs',
          expirationTime: '2020-08-27T04:35:54.631Z',
          id: '2234dde-npce1-2908-b3131-6123c675a0fa_0',
          lastUpdate: '2020-08-27T03:35:54.635Z',
          priority: 0,
          processId: 'travels',
          processInstanceId: '8035b580-6ae4-4aa8-9ec0-e18e19809e0b',
          repeatInterval: null,
          repeatLimit: null,
          retries: 0,
          rootProcessId: '',
          scheduledId: null,
          status: GraphQL.JobStatus.Canceled,
          executionCounter: 4
        }
      ]
    },
    handleDetailsToggle: jest.fn(),
    handleRescheduleToggle: jest.fn(),
    handleCancelModalToggle: jest.fn(),
    setModalTitle: jest.fn(),
    setModalContent: jest.fn(),
    setSelectedJob: jest.fn(),
    selectedJobInstances: [],
    setSelectedJobInstances: jest.fn()
  };
  it('Snapshot with default props', async () => {
    const wrapper = await getWrapperAsync(
      <JobsManagementTable {...props} />,
      'JobsManagementTable'
    );
    expect(wrapper).toMatchSnapshot();
    const event: any = {};
    await act(async () => {
      wrapper
        .find('Table')
        .props()
        ['onSelect'](event);
    });
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
    mockedAxios.delete.mockResolvedValue({});
    const jobCancelSpy = jest.spyOn(Utils, 'jobCancel');
    const refetch = jest.fn();
    let wrapper = await getWrapperAsync(
      <refetchContext.Provider value={refetch}>
        <JobsManagementTable {...props} />
      </refetchContext.Provider>,
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

  it('test job cancel action with error response', async () => {
    mockedAxios.delete.mockRejectedValue({ message: '404 error' });
    const jobCancelSpy = jest.spyOn(Utils, 'jobCancel');
    const refetch = jest.fn();
    let wrapper = await getWrapperAsync(
      <refetchContext.Provider value={refetch}>
        <JobsManagementTable {...props} />
      </refetchContext.Provider>,
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
    const refetch = jest.fn();
    let wrapper = await getWrapperAsync(
      <refetchContext.Provider value={refetch}>
        <JobsManagementTable {...props} />
      </refetchContext.Provider>,
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
        {...{ ...props, selectedJobInstances: [{ ...props.data.Jobs[1] }] }}
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
        {...{ ...props, selectedJobInstances: [...props.data.Jobs] }}
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
});
