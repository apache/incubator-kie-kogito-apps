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

import { GraphQL } from '@kogito-apps/consoles-common';
import wait from 'waait';
import {
  handleJobReschedule,
  handleProcessMultipleAction,
  jobCancel,
  performMultipleCancel,
  getTriggerableNodes,
  handleNodeTrigger,
  handleProcessVariableUpdate,
  handleNodeInstanceRetrigger,
  handleNodeInstanceCancel,
  handleProcessAbort,
  handleProcessSkip,
  handleProcessRetry
} from '../apis';
import {
  BulkProcessInstanceActionResponse,
  OperationType,
  ProcessInstanceState,
  NodeInstance,
  ProcessInstance
} from '@kogito-apps/management-console-shared';
import { act } from 'react-dom/test-utils';
import reactApollo from 'react-apollo';

jest.mock('axios');
jest.mock('apollo-client');

jest.mock('react-apollo', () => {
  const ApolloClient = { query: jest.fn(), mutate: jest.fn() };
  return { useApolloClient: jest.fn(() => ApolloClient) };
});

const processInstance: ProcessInstance = {
  id: 'a1e139d5-4e77-48c9-84ae-34578e904e5a',
  processId: 'hotelBooking',
  businessKey: 'T1234HotelBooking01',
  parentProcessInstanceId: null,
  processName: 'HotelBooking',
  rootProcessInstanceId: null,
  roles: [],
  state: ProcessInstanceState.Active,
  start: new Date('2020-02-19T11:11:56.282Z'),
  end: new Date('2020-02-19T11:11:56.282Z'),
  lastUpdate: new Date('2020-02-19T11:11:56.282Z'),
  serviceUrl: 'http://localhost:4000',
  endpoint: 'http://localhost:4000',
  error: {
    nodeDefinitionId: 'a1e139d5-4e77-48c9-84ae-34578e904e6b',
    message: 'some thing went wrong'
  },
  addons: [],
  variables:
    '{"trip":{"begin":"2019-10-22T22:00:00Z[UTC]","city":"Bangalore","country":"India","end":"2019-10-30T22:00:00Z[UTC]","visaRequired":false},"hotel":{"address":{"city":"Bangalore","country":"India","street":"street","zipCode":"12345"},"bookingNumber":"XX-012345","name":"Perfect hotel","phone":"09876543"},"traveller":{"address":{"city":"Bangalore","country":"US","street":"Bangalore","zipCode":"560093"},"email":"ajaganat@redhat.com","firstName":"Ajay","lastName":"Jaganathan","nationality":"US"}}',
  nodes: [
    {
      nodeId: '1',
      name: 'End Event 1',
      definitionId: 'EndEvent_1',
      id: '27107f38-d888-4edf-9a4f-11b9e6d751b6',
      enter: new Date('2020-02-19T11:11:56.282Z'),
      exit: new Date('2020-02-19T11:11:56.282Z'),
      type: 'EndNode'
    }
  ],
  childProcessInstances: []
};

const processInstances = [
  {
    id: 'a1e139d5-4e77-48c9-84ae-34578e904e5a',
    processId: 'hotelBooking',
    businessKey: 'T1234HotelBooking01',
    parentProcessInstanceId: null,
    parentProcessInstance: null,
    processName: 'HotelBooking',
    rootProcessInstanceId: 'e4448857-fa0c-403b-ad69-f0a353458b9d',
    roles: [],
    state: ProcessInstanceState.Error,
    start: new Date('2019-10-22T03:40:44.089Z'),
    end: new Date('2019-10-22T05:40:44.089Z'),
    lastUpdate: new Date('2019-10-22T05:40:44.089Z'),
    serviceUrl: 'http://localhost:4000',
    endpoint: 'http://localhost:4000',
    error: {
      nodeDefinitionId: 'a1e139d5-4e77-48c9-84ae-34578e904e6b',
      message: 'some thing went wrong'
    },
    addons: [],
    variables:
      '{"trip":{"begin":"2019-10-22T22:00:00Z[UTC]","city":"Bangalore","country":"India","end":"2019-10-30T22:00:00Z[UTC]","visaRequired":false},"hotel":{"address":{"city":"Bangalore","country":"India","street":"street","zipCode":"12345"},"bookingNumber":"XX-012345","name":"Perfect hotel","phone":"09876543"},"traveller":{"address":{"city":"Bangalore","country":"US","street":"Bangalore","zipCode":"560093"},"email":"ajaganat@redhat.com","firstName":"Ajay","lastName":"Jaganathan","nationality":"US"}}',
    nodes: [
      {
        nodeId: '1',
        name: 'End Event 1',
        definitionId: 'EndEvent_1',
        id: '27107f38-d888-4edf-9a4f-11b9e6d751b6',
        enter: new Date('2019-10-22T03:37:30.798Z'),
        exit: new Date('2019-10-22T03:37:30.798Z'),
        type: 'EndNode'
      }
    ]
  }
];

describe('bulk cancel tests', () => {
  let client;
  let useApolloClient;
  const mockUseApolloClient = () => {
    act(() => {
      client = useApolloClient();
    });
  };

  beforeEach(() => {
    act(() => {
      useApolloClient = jest.spyOn(reactApollo, 'useApolloClient');
      mockUseApolloClient();
    });
  });
  const bulkJobs = [
    {
      id: 'dad3aa88-5c1e-4858-a919-6123c675a0fa_0',
      processId: 'travels',
      processInstanceId: '8035b580-6ae4-4aa8-9ec0-e18e19809e0b',
      rootProcessId: '',
      status: GraphQL.JobStatus.Scheduled,
      priority: 0,
      callbackEndpoint:
        'http://localhost:8080/management/jobs/travels/instances/5c56eeff-4cbf-3313-a325-4c895e0afced/timers/dad3aa88-5c1e-4858-a919-6123c675a0fa_0',
      repeatInterval: null,
      repeatLimit: null,
      scheduledId: null,
      retries: 0,
      lastUpdate: '2020-08-27T03:35:54.635Z',
      expirationTime: '2020-08-27T04:35:54.631Z',
      endpoint: 'http://localhost:4000/jobs',
      errorMessage: ''
    }
  ];
  it('bulk cancel success', async () => {
    const expectedResults = {
      successJobs: [
        {
          id: 'dad3aa88-5c1e-4858-a919-6123c675a0fa_0',
          processId: 'travels',
          processInstanceId: '8035b580-6ae4-4aa8-9ec0-e18e19809e0b',
          rootProcessId: '',
          status: 'SCHEDULED',
          priority: 0,
          callbackEndpoint:
            'http://localhost:8080/management/jobs/travels/instances/5c56eeff-4cbf-3313-a325-4c895e0afced/timers/dad3aa88-5c1e-4858-a919-6123c675a0fa_0',
          repeatInterval: null,
          repeatLimit: null,
          scheduledId: null,
          retries: 0,
          lastUpdate: '2020-08-27T03:35:54.635Z',
          expirationTime: '2020-08-27T04:35:54.631Z',
          endpoint: 'http://localhost:4000/jobs',
          errorMessage: ''
        }
      ],
      failedJobs: []
    };
    client.mutate.mockResolvedValue({});
    const result = await performMultipleCancel(bulkJobs, client);
    await wait(0);
    expect(result).toEqual(expectedResults);
  });
  it('bulk cancel failure', async () => {
    const expectedResults = {
      successJobs: [],
      failedJobs: [
        {
          id: 'dad3aa88-5c1e-4858-a919-6123c675a0fa_0',
          processId: 'travels',
          processInstanceId: '8035b580-6ae4-4aa8-9ec0-e18e19809e0b',
          rootProcessId: '',
          status: 'SCHEDULED',
          priority: 0,
          callbackEndpoint:
            'http://localhost:8080/management/jobs/travels/instances/5c56eeff-4cbf-3313-a325-4c895e0afced/timers/dad3aa88-5c1e-4858-a919-6123c675a0fa_0',
          repeatInterval: null,
          repeatLimit: null,
          scheduledId: null,
          retries: 0,
          lastUpdate: '2020-08-27T03:35:54.635Z',
          expirationTime: '2020-08-27T04:35:54.631Z',
          endpoint: 'http://localhost:4000/jobs',
          errorMessage: undefined
        }
      ]
    };

    client.mutate.mockRejectedValue({});
    const result = await performMultipleCancel(bulkJobs, client);
    await wait(0);
    expect(result).toEqual(expectedResults);
  });
});

describe('job cancel tests', () => {
  let client;
  let useApolloClient;
  const mockUseApolloClient = () => {
    act(() => {
      client = useApolloClient();
    });
  };

  beforeEach(() => {
    act(() => {
      useApolloClient = jest.spyOn(reactApollo, 'useApolloClient');
      mockUseApolloClient();
    });
  });
  const job = {
    id: 'T3113e-vbg43-2234-lo89-cpmw3214ra0fa_0',
    processId: 'travels',
    processInstanceId: '8035b580-6ae4-4aa8-9ec0-e18e19809e0b',
    rootProcessId: '',
    status: 'ERROR',
    priority: 0,
    callbackEndpoint:
      'http://localhost:8080/management/jobs/travels/instances/5c56eeff-4cbf-3313-a325-4c895e0afced/timers/dad3aa88-5c1e-4858-a919-6123c675a0fa_0',
    repeatInterval: null,
    repeatLimit: null,
    scheduledId: null,
    retries: 0,
    lastUpdate: '2020-08-27T03:35:54.635Z',
    expirationTime: '2020-08-27T04:35:54.631Z'
  };

  it('executes job cancel successfully', async () => {
    const expectedResult = {
      modalTitle: 'success',
      modalContent:
        'The job: T3113e-vbg43-2234-lo89-cpmw3214ra0fa_0 is canceled successfully'
    };
    client.mutate.mockResolvedValue({});
    await jobCancel(job, client)
      .then(value => {
        expect(value).toEqual(expectedResult);
      })
      .catch(reason => {
        expect(reason).toEqual(expectedResult);
      });
  });

  it('fails to execute job cancel', async () => {
    const expectedResult = {
      modalTitle: 'failure',
      modalContent:
        'The job: T3113e-vbg43-2234-lo89-cpmw3214ra0fa_0 failed to cancel. Error message: 404 error'
    };
    client.mutate.mockRejectedValue({ message: '404 error' });
    await jobCancel(job, client)
      .then(value => {
        expect(value).toEqual(expectedResult);
      })
      .catch(reason => {
        expect(reason).toEqual(expectedResult);
      });
  });

  it('test reschedule function', async () => {
    client.mutate.mockResolvedValue({
      status: 200,
      statusText: 'OK',
      data: {
        callbackEndpoint:
          'http://localhost:8080/management/jobs/travels/instances/9865268c-64d7-3a44-8972-7325b295f7cc/timers/58180644-2fdf-4261-83f2-f4e783d308a3_0',
        executionCounter: 0,
        executionResponse: null,
        expirationTime: '2020-10-16T10:17:22.879Z',
        id: '58180644-2fdf-4261-83f2-f4e783d308a3_0',
        lastUpdate: '2020-10-07T07:41:31.467Z',
        priority: 0,
        processId: 'travels',
        processInstanceId: '9865268c-64d7-3a44-8972-7325b295f7cc',
        repeatInterval: null,
        repeatLimit: null,
        retries: 0,
        rootProcessId: null,
        rootProcessInstanceId: null,
        scheduledId: null,
        status: 'SCHEDULED'
      }
    });
    const job = {
      id: '6e74a570-31c8-4020-bd70-19be2cb625f3_0',
      processId: 'travels',
      processInstanceId: '5c56eeff-4cbf-3313-a325-4c895e0afced',
      rootProcessId: '5c56eeff-4cbf-3313-a325-4c895e0afced',
      status: GraphQL.JobStatus.Executed,
      priority: 0,
      callbackEndpoint:
        'http://localhost:8080/management/jobs/travels/instances/5c56eeff-4cbf-3313-a325-4c895e0afced/timers/6e74a570-31c8-4020-bd70-19be2cb625f3_0',
      repeatInterval: 1,
      repeatLimit: 3,
      scheduledId: '0',
      retries: 0,
      lastUpdate: '2020-08-27T03:35:50.147Z',
      expirationTime: '2020-08-27T03:35:50.147Z'
    };
    const repeatInterval = 2;
    const repeatLimit = 1;
    const scheduleDate = new Date('2020-08-27T03:35:50.147Z');
    const modalTitle = 'success';
    const modalContent = `Reschedule of job: ${job.id} is successful`;
    // const result = await handleJobReschedule(
    //   job,
    //   repeatInterval,
    //   repeatLimit,
    //   scheduleDate,
    //     client
    // );
    await handleJobReschedule(
      job,
      repeatInterval,
      repeatLimit,
      scheduleDate,
      client
    )
      .then(value => {
        expect(value).toEqual({ modalTitle, modalContent });
      })
      .catch(reason => {
        expect(reason).toEqual({ modalTitle, modalContent });
      });

    // expect(result).toEqual({ modalTitle, modalContent });
  });
  it('test error response for reschedule function', async () => {
    client.mutate.mockRejectedValue({ message: '403 error' });
    const job = {
      id: '6e74a570-31c8-4020-bd70-19be2cb625f3_0',
      processId: 'travels',
      processInstanceId: '5c56eeff-4cbf-3313-a325-4c895e0afced',
      rootProcessId: '5c56eeff-4cbf-3313-a325-4c895e0afced',
      status: GraphQL.JobStatus.Executed,
      priority: 0,
      callbackEndpoint:
        'http://localhost:8080/management/jobs/travels/instances/5c56eeff-4cbf-3313-a325-4c895e0afced/timers/6e74a570-31c8-4020-bd70-19be2cb625f3_0',
      repeatInterval: 1,
      repeatLimit: 3,
      scheduledId: '0',
      retries: 0,
      lastUpdate: '2020-08-27T03:35:50.147Z',
      expirationTime: '2020-08-27T03:35:50.147Z'
    };
    const repeatInterval = null;
    const repeatLimit = null;
    const scheduleDate = new Date('2020-08-27T03:35:50.147Z');
    const modalTitle = 'failure';
    const modalContent = `Reschedule of job ${job.id} failed. Message: 403 error`;
    await handleJobReschedule(
      job,
      repeatInterval,
      repeatLimit,
      scheduleDate,
      client
    )
      .then(value => {
        expect(value).toEqual({ modalTitle, modalContent });
      })
      .catch(reason => {
        expect(reason).toEqual({ modalTitle, modalContent });
      });
  });
});

describe('handle skip test', () => {
  let client;
  let useApolloClient;
  const mockUseApolloClient = () => {
    act(() => {
      client = useApolloClient();
    });
  };

  beforeEach(() => {
    act(() => {
      useApolloClient = jest.spyOn(reactApollo, 'useApolloClient');
      mockUseApolloClient();
    });
  });

  it('on successful skip', async () => {
    client.mutate.mockResolvedValue({ data: 'success' });
    let result = null;
    await handleProcessSkip(processInstance, client)
      .then(() => {
        result = 'success';
      })
      .catch(error => {
        result = error.message;
      });
    expect(result).toEqual('success');
  });
  it('on failed skip', async () => {
    client.mutate.mockRejectedValue({ message: '404 error' });
    let result = null;
    await handleProcessSkip(processInstance, client)
      .then(() => {
        result = 'success';
      })
      .catch(error => {
        result = error.message;
      });
    expect(result).toEqual('404 error');
  });
});

describe('handle retry test', () => {
  let client;
  let useApolloClient;
  const mockUseApolloClient = () => {
    act(() => {
      client = useApolloClient();
    });
  };

  beforeEach(() => {
    act(() => {
      useApolloClient = jest.spyOn(reactApollo, 'useApolloClient');
      mockUseApolloClient();
    });
  });

  it('on successful retrigger', async () => {
    client.mutate.mockResolvedValue({ data: 'success' });
    let result = null;
    await handleProcessRetry(processInstances[0], client)
      .then(() => {
        result = 'success';
      })
      .catch(error => {
        result = error.message;
      });
    expect(result).toEqual('success');
  });
  it('on failed retrigger', async () => {
    client.mutate.mockRejectedValue({ message: '404 error' });
    let result = null;
    await handleProcessRetry(processInstance[0], client)
      .then(() => {
        result = 'success';
      })
      .catch(error => {
        result = error.message;
      });
    expect(result).toEqual("Cannot read property 'id' of undefined");
  });
});

describe('handle abort test', () => {
  let client;
  let useApolloClient;
  const mockUseApolloClient = () => {
    act(() => {
      client = useApolloClient();
    });
  };

  beforeEach(() => {
    act(() => {
      useApolloClient = jest.spyOn(reactApollo, 'useApolloClient');
      mockUseApolloClient();
    });
  });
  it('on successful abort', async () => {
    client.mutate.mockResolvedValue({ data: 'success' });
    let result = null;
    await handleProcessAbort(processInstances[0], client)
      .then(() => {
        result = 'success';
      })
      .catch(error => {
        result = error.message;
      });
    expect(result).toEqual('success');
  });
  it('on failed abort', async () => {
    client.mutate.mockRejectedValue({ message: '404 error' });
    let result = null;
    await handleProcessAbort(processInstances[0], client)
      .then(() => {
        result = 'success';
      })
      .catch(error => {
        result = error.message;
      });
    expect(result).toEqual('404 error');
  });
});

describe('multiple action in process list', () => {
  let client;
  let useApolloClient;
  const mockUseApolloClient = () => {
    act(() => {
      client = useApolloClient();
    });
  };

  beforeEach(() => {
    act(() => {
      useApolloClient = jest.spyOn(reactApollo, 'useApolloClient');
      mockUseApolloClient();
    });
  });
  it('multiple skip test', async () => {
    client.mutate.mockResolvedValue({ data: 'success' });
    const result: BulkProcessInstanceActionResponse = await handleProcessMultipleAction(
      processInstances,
      OperationType.SKIP,
      client
    );
    expect(result.successProcessInstances.length).toEqual(1);
  });
  it('multiple skip test', async () => {
    client.mutate.mockRejectedValue({ message: '404 error' });
    const result: BulkProcessInstanceActionResponse = await handleProcessMultipleAction(
      processInstances,
      OperationType.SKIP,
      client
    );
    expect(result.failedProcessInstances[0].errorMessage).toEqual('404 error');
  });

  it('multiple retry test', async () => {
    client.mutate.mockResolvedValue({ data: 'success' });
    const result: BulkProcessInstanceActionResponse = await handleProcessMultipleAction(
      processInstances,
      OperationType.RETRY,
      client
    );
    expect(result.successProcessInstances.length).toEqual(1);
  });
  it('multiple retry test', async () => {
    client.mutate.mockRejectedValue({ message: '404 error' });
    const result: BulkProcessInstanceActionResponse = await handleProcessMultipleAction(
      processInstances,
      OperationType.RETRY,
      client
    );
    expect(result.failedProcessInstances[0].errorMessage).toEqual('404 error');
  });

  it('multiple abort test', async () => {
    client.mutate.mockResolvedValue({ data: 'success' });
    const result: BulkProcessInstanceActionResponse = await handleProcessMultipleAction(
      processInstances,
      OperationType.ABORT,
      client
    );
    expect(result.successProcessInstances.length).toEqual(1);
  });
  it('multiple abort test', async () => {
    client.mutate.mockRejectedValue({ message: '404 error' });
    const result: BulkProcessInstanceActionResponse = await handleProcessMultipleAction(
      processInstances,
      OperationType.ABORT,
      client
    );
    expect(result.failedProcessInstances[0].errorMessage).toEqual('404 error');
  });
});

describe('test utilities of process variables', () => {
  const updatedProcessInstances = [
    {
      id: 'a1e139d5-4e77-48c9-84ae-34578e904e5a',
      variables:
        '{"trip":{"begin":"2019-10-22T22:00:00Z[UTC]","city":"Bangalore","country":"India","end":"2019-10-30T22:00:00Z[UTC]","visaRequired":false},"hotel":{"address":{"city":"Bangalore","country":"India","street":"street","zipCode":"12345"},"bookingNumber":"XX-012345","name":"Perfect hotel","phone":"09876543"},"traveller":{"address":{"city":"Bangalore","country":"US","street":"Bangalore","zipCode":"560093"},"email":"ajaganat@redhat.com","firstName":"Ajay","lastName":"Jaganathan","nationality":"US"}}'
    }
  ];

  let client;
  let useApolloClient;
  const mockUseApolloClient = () => {
    act(() => {
      client = useApolloClient();
    });
  };

  beforeEach(() => {
    act(() => {
      useApolloClient = jest.spyOn(reactApollo, 'useApolloClient');
      mockUseApolloClient();
    });
  });

  it('test put method that updates process variables-success', async () => {
    client.mutate.mockResolvedValue({
      data: { ProcessInstanceUpdateVariables: processInstances[0].variables }
    });
    let result;
    await handleProcessVariableUpdate(
      processInstance,
      JSON.parse(updatedProcessInstances[0].variables),
      client
    )
      .then(data => {
        result = data;
      })
      .catch(error => {
        result = error.message;
      });
    expect(result).toEqual(JSON.parse(processInstances[0].variables));
  });
});

describe('retrieve list of triggerable nodes test', () => {
  let client;
  let useApolloClient;
  const mockUseApolloClient = () => {
    act(() => {
      client = useApolloClient();
    });
  };

  beforeEach(() => {
    act(() => {
      useApolloClient = jest.spyOn(reactApollo, 'useApolloClient');
      mockUseApolloClient();
    });
  });
  const nodeDefinitions = [
    {
      nodeDefinitionId: '_BDA56801-1155-4AF2-94D4-7DAADED2E3C0',
      name: 'Send visa application',
      id: 1,
      type: 'ActionNode',
      uniqueId: '1'
    },
    {
      nodeDefinitionId: '_175DC79D-C2F1-4B28-BE2D-B583DFABF70D',
      name: 'Book',
      id: 2,
      type: 'Split',
      uniqueId: '2'
    },
    {
      nodeDefinitionId: '_E611283E-30B0-46B9-8305-768A002C7518',
      name: 'visasrejected',
      id: 3,
      type: 'EventNode',
      uniqueId: '3'
    }
  ];

  it('successfully retrieves the list of nodes', async () => {
    client.query.mockResolvedValue({
      data: {
        ProcessInstances: [
          {
            nodeDefinitions
          }
        ]
      }
    });
    let result = null;
    await getTriggerableNodes(processInstance, client)
      .then(nodes => {
        result = nodes;
      })
      .catch(error => {
        result = error;
      });
    expect(result).toEqual(nodeDefinitions);
  });
  it('fails to retrieve the list of nodes', async () => {
    client.query.mockRejectedValue({ message: '403 error' });
    let result = null;
    await getTriggerableNodes(processInstance, client)
      .then(nodes => {
        result = nodes;
      })
      .catch(error => {
        result = error.message;
      });
    expect(result).toEqual('403 error');
  });
});

describe('handle node trigger click tests', () => {
  let client;
  let useApolloClient;
  const mockUseApolloClient = () => {
    act(() => {
      client = useApolloClient();
    });
  };

  beforeEach(() => {
    act(() => {
      useApolloClient = jest.spyOn(reactApollo, 'useApolloClient');
      mockUseApolloClient();
    });
  });
  const node = {
    nodeDefinitionId: '_BDA56801-1155-4AF2-94D4-7DAADED2E3C0',
    name: 'Send visa application',
    id: 1,
    type: 'ActionNode',
    uniqueId: '1'
  };
  it('executes node trigger successfully', async () => {
    let result = '';
    client.mutate.mockResolvedValue({});
    await handleNodeTrigger(processInstance, node, client)
      .then(() => {
        result = 'success';
      })
      .catch(error => {
        result = 'error';
      });
    expect(result).toEqual('success');
  });

  it('fails to execute node trigger', async () => {
    client.mutate.mockRejectedValue({ message: '404 error' });
    let result = '';
    await handleNodeTrigger(processInstance, node, client)
      .then(() => {
        result = 'success';
      })
      .catch(error => {
        result = 'error';
      });
    expect(result).toEqual('error');
  });
});

describe('handle node instance trigger click tests', () => {
  let client;
  let useApolloClient;
  const mockUseApolloClient = () => {
    act(() => {
      client = useApolloClient();
    });
  };

  beforeEach(() => {
    act(() => {
      useApolloClient = jest.spyOn(reactApollo, 'useApolloClient');
      mockUseApolloClient();
    });
  });
  const node: NodeInstance = {
    definitionId: '_BDA56801-1155-4AF2-94D4-7DAADED2E3C0',
    name: 'Send visa application',
    id: '1',
    type: 'ActionNode',
    nodeId: 'nodeOne',
    enter: new Date('2020-11-11')
  };
  it('executes node instance trigger successfully', async () => {
    let result = '';
    client.mutate.mockResolvedValue({});
    await handleNodeInstanceRetrigger(processInstance, node, client)
      .then(() => {
        result = 'success';
      })
      .catch(error => {
        result = 'error';
      });
    expect(result).toEqual('success');
  });

  it('fails to execute instance node trigger', async () => {
    client.mutate.mockRejectedValue({ message: '404 error' });
    let result = '';
    await handleNodeInstanceRetrigger(processInstance, node, client)
      .then(() => {
        result = 'success';
      })
      .catch(error => {
        result = 'error';
      });
    expect(result).toEqual('error');
  });
});

describe('handle node instance cancel', () => {
  let client;
  let useApolloClient;
  const mockUseApolloClient = () => {
    act(() => {
      client = useApolloClient();
    });
  };

  beforeEach(() => {
    act(() => {
      useApolloClient = jest.spyOn(reactApollo, 'useApolloClient');
      mockUseApolloClient();
    });
  });
  const node: NodeInstance = {
    definitionId: '_BDA56801-1155-4AF2-94D4-7DAADED2E3C0',
    name: 'Send visa application',
    id: '1',
    type: 'ActionNode',
    nodeId: 'nodeOne',
    enter: new Date('2020-11-11')
  };
  it('executes handle node instance cancel successfully', async () => {
    let result = '';
    client.mutate.mockResolvedValue({});
    await handleNodeInstanceCancel(processInstance, node, client)
      .then(() => {
        result = 'success';
      })
      .catch(error => {
        result = 'error';
      });
    expect(result).toEqual('success');
  });

  it('fails to handle node instance cancel', async () => {
    client.mutate.mockRejectedValue({ message: '404 error' });
    let result = '';
    await handleNodeInstanceCancel(processInstance, node, client)
      .then(() => {
        result = 'success';
      })
      .catch(error => {
        result = 'error';
      });
    expect(result).toEqual('error');
  });
});
