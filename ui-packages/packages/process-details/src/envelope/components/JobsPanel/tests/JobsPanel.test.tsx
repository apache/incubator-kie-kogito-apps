/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
import React from 'react';
import JobsPanel from '../JobsPanel';
import { render } from '@testing-library/react';
import { MockedProcessDetailsDriver } from '../../../../embedded/tests/mocks/Mocks';
import { JobStatus } from '@kogito-apps/management-console-shared/dist/types';
import { act } from 'react-dom/test-utils';

const MockedComponent = (): React.ReactElement => {
  return <></>;
};

jest.mock('@patternfly/react-icons/dist/js/icons/error-circle-o-icon', () =>
  Object.assign({}, jest.requireActual('@patternfly/react-icons'), {
    ErrorCircleOIcon: () => {
      return <MockedComponent />;
    }
  })
);

jest.mock('@patternfly/react-icons/dist/js/icons/ban-icon', () =>
  Object.assign({}, jest.requireActual('@patternfly/react-icons'), {
    BanIcon: () => {
      return <MockedComponent />;
    }
  })
);

jest.mock('@patternfly/react-icons/dist/js/icons/check-circle-icon', () =>
  Object.assign({}, jest.requireActual('@patternfly/react-icons'), {
    CheckCircleIcon: () => {
      return <MockedComponent />;
    }
  })
);

jest.mock('@patternfly/react-icons/dist/js/icons/undo-icon', () =>
  Object.assign({}, jest.requireActual('@patternfly/react-icons'), {
    UndoIcon: () => {
      return <MockedComponent />;
    }
  })
);

jest.mock('@patternfly/react-icons/dist/js/icons/clock-icon', () =>
  Object.assign({}, jest.requireActual('@patternfly/react-icons'), {
    ClockIcon: () => {
      return <MockedComponent />;
    }
  })
);

const props = {
  jobs: [
    {
      id: '6e74a570-31c8-4020-bd70-19be2cb625f3_0',
      processId: 'travels',
      processInstanceId: '5c56eeff-4cbf-3313-a325-4c895e0afced',
      rootProcessId: null,
      status: JobStatus.Executed,
      priority: 0,
      callbackEndpoint:
        'http://localhost:8080/management/jobs/travels/instances/5c56eeff-4cbf-3313-a325-4c895e0afced/timers/6e74a570-31c8-4020-bd70-19be2cb625f3_0',
      repeatInterval: null,
      repeatLimit: null,
      scheduledId: '0',
      retries: 0,
      lastUpdate: new Date('2020-08-27T03:35:50.147Z'),
      expirationTime: null,
      endpoint: 'http://localhost:4000'
    }
  ],
  driver: MockedProcessDetailsDriver()
};

const props2 = {
  jobs: [
    {
      id: '6e74a570-31c8-4020-bd70-19be2cb625f3_0',
      processId: 'travels',
      processInstanceId: '5c56eeff-4cbf-3313-a325-4c895e0afced',
      rootProcessId: null,
      status: JobStatus.Executed,
      priority: 0,
      callbackEndpoint:
        'http://localhost:8080/management/jobs/travels/instances/5c56eeff-4cbf-3313-a325-4c895e0afced/timers/6e74a570-31c8-4020-bd70-19be2cb625f3_0',
      repeatInterval: null,
      repeatLimit: null,
      scheduledId: '0',
      retries: 0,
      lastUpdate: new Date('2020-08-27T03:35:50.147Z'),
      expirationTime: new Date('2020-08-29T03:35:50.147Z'),
      endpoint: 'http://localhost:4000'
    }
  ],
  driver: MockedProcessDetailsDriver(),
  ouiaSafe: true
};

const props3 = {
  jobs: [],
  driver: MockedProcessDetailsDriver(),
  ouiaSafe: true
};

Date.now = jest.fn(() => 1592000000000); // UTC Fri Jun 12 2020 22:13:20
describe('Processdetails jobs pannel component tests', () => {
  it('Snapshot testing', async () => {
    let container;
    await act(async () => {
      container = render(<JobsPanel {...props} />).container;
    });
    expect(container).toMatchSnapshot();
  });
  it('test expiration time', async () => {
    let container;
    await act(async () => {
      container = render(<JobsPanel {...props2} />).container;
    });
    expect(container).toMatchSnapshot();
  });

  it('Jobs empty response', async () => {
    let container;
    await act(async () => {
      container = render(<JobsPanel {...props3} />).container;
    });
    expect(container).toMatchSnapshot();
  });
});
