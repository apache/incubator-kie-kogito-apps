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
import { getWrapper } from '@kogito-apps/components-common';
import React from 'react';
import { act } from 'react-dom/test-utils';
import TestProcessListDriver from '../../ProcessListPage/mocks/TestProcessListDriver';
import { childProcessInstances } from '../mocks/Mocks';
import ProcessListChildTable from '../ProcessListChildTable';
Date.now = jest.fn(() => 1592000000000); // UTC Fri Jun 12 2020 22:13:20
const MockedComponent = (): React.ReactElement => {
  return <></>;
};

jest.mock('@kogito-apps/components-common', () => ({
  ...jest.requireActual('@kogito-apps/components-common'),
  ServerErrors: () => {
    return <MockedComponent />;
  },
  KogitoSpinner: () => {
    return <MockedComponent />;
  },
  KogitoEmptyState: () => {
    return <MockedComponent />;
  },
  ItemDescriptor: () => {
    return <MockedComponent />;
  },
  EndpointLink: () => {
    return <MockedComponent />;
  }
}));

describe('ProcessListChildTable test', () => {
  it('render table', async () => {
    const driver = new TestProcessListDriver([], childProcessInstances);
    const driverGetChildQueryMock = jest.spyOn(
      driver,
      'getChildProcessesQuery'
    );
    const props = {
      driver,
      parentProcessId: 'e4448857-fa0c-403b-ad69-f0a353458b9d'
    };
    let wrapper;
    await act(async () => {
      wrapper = getWrapper(
        <ProcessListChildTable {...props} />,
        'ProcessListChildTable'
      );
    });
    expect(wrapper).toMatchSnapshot();
    expect(driverGetChildQueryMock).toHaveBeenCalledWith(props.parentProcessId);
  });

  it('error in query', async () => {
    const driver = new TestProcessListDriver([], childProcessInstances);
    const driverGetChildQueryMock = jest.spyOn(
      driver,
      'getChildProcessesQuery'
    );
    const props = {
      driver,
      parentProcessId: 'e4448857-fa0c-403b-ad69-f0a353458b9d'
    };
    driverGetChildQueryMock.mockImplementation(() => {
      throw new Error('404 error');
    });
    let wrapper;
    await act(async () => {
      wrapper = getWrapper(
        <ProcessListChildTable {...props} />,
        'ProcessListChildTable'
      );
    });
    wrapper = wrapper.update();
    const serverError = wrapper.find('ServerErrors');
    expect(serverError).toMatchSnapshot();
    expect(serverError.exists()).toBeTruthy();
  });

  it('no results found', async () => {
    const driver = new TestProcessListDriver([], childProcessInstances);
    const driverGetChildQueryMock = jest.spyOn(
      driver,
      'getChildProcessesQuery'
    );
    const props = {
      driver,
      parentProcessId: 'e4448857-fa0c-403b-ad69-f0a353458b9d'
    };
    driverGetChildQueryMock.mockImplementation(() => {
      return Promise.resolve([]);
    });
    let wrapper;
    await act(async () => {
      wrapper = getWrapper(
        <ProcessListChildTable {...props} />,
        'ProcessListChildTable'
      );
    });
    wrapper = wrapper.update();
    expect(driverGetChildQueryMock).toHaveBeenCalledWith(props.parentProcessId);
    const EmptyState = wrapper.find('KogitoEmptyState');
    expect(EmptyState.exists()).toBeTruthy();
    expect(EmptyState).toMatchSnapshot();
  });
});
