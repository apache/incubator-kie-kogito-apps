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
  Job,
  JobCancel,
  JobStatus,
  NodeInstance,
  ProcessInstance
} from '@kogito-apps/management-console-shared/dist/types';
import { setTitle } from '@kogito-apps/management-console-shared/dist/utils/Utils';
import {
  getOmmitedNodesForTimeline,
  getSuccessNodes,
  handleJobRescheduleUtil,
  handleNodeInstanceCancel,
  handleNodeInstanceRetrigger,
  jobCancel,
  JobsIconCreator
} from '../Utils';
import TestProcessDetailsDriver from '../../envelope/tests/mocks/TestProcessDetailsDriver';
import { handleSkip, handleRetry } from '../Utils';
import wait from 'waait';
const children = 'children';

export const processInstance: ProcessInstance = {
  endpoint: '',
  id: '',
  lastUpdate: undefined,
  nodes: [],
  processId: '',
  start: undefined,
  state: undefined
};

export const node: NodeInstance = {
  definitionId: '',
  enter: undefined,
  id: '',
  name: '',
  nodeId: '',
  type: ''
};

export const job: Job = {
  callbackEndpoint: '',
  expirationTime: undefined,
  id: '',
  lastUpdate: undefined,
  priority: 0,
  processId: '',
  processInstanceId: '',
  repeatInterval: 0,
  repeatLimit: 0,
  retries: 0,
  scheduledId: '',
  status: undefined
};

const driver = new TestProcessDetailsDriver(
  '2d962eef-45b8-48a9-ad4e-9cde0ad6af89'
);

const nodeInstances = [
  {
    definitionId: '_jbpm-unique-2',
    enter: new Date('2023-07-19T09:22:43.364Z'),
    exit: new Date('2023-07-19T09:22:43.371Z'),
    id: '5e67c1a8-2d66-40c3-a5a2-a1ac3b6c8138',
    name: 'HelloWorld',
    nodeId: '3',
    type: 'ActionNode'
  },
  {
    definitionId: '_jbpm-unique-2',
    enter: new Date('2023-07-19T09:22:43.364Z'),
    exit: new Date('2023-07-19T09:22:43.371Z'),
    id: '5e67c1a8-2d66-40c3-a5a2-a1ac3b6c8138',
    name: 'HelloWorld',
    nodeId: '3',
    type: 'ActionNode'
  },
  {
    definitionId: '_jbpm-unique-1',
    enter: new Date('2023-07-19T09:22:43.372Z'),
    exit: new Date('2023-07-19T09:22:43.372Z'),
    id: 'e273f5b2-d52f-4fc3-b45d-8f346952e86f',
    name: 'End',
    nodeId: '2',
    type: 'EndNode'
  },
  {
    definitionId: '_jbpm-unique-5',
    enter: new Date('2023-07-19T09:22:43.372Z'),
    exit: new Date('2023-07-19T09:22:43.372Z'),
    id: 'e273f5b2-d52f-4fc3-b45d-8f346952e86f',
    name: 'branch-18',
    nodeId: '2',
    type: 'branch'
  },
  {
    definitionId: '_jbpm-unique-6',
    enter: new Date('2023-07-19T09:22:43.372Z'),
    exit: new Date('2023-07-19T09:22:43.372Z'),
    id: 'e273f5b2-d52f-4fc3-b45d-8f346952e86f',
    name: 'EmbeddedStart',
    nodeId: '2',
    type: 'EmbeddedStart'
  }
];

describe('process details package utils', () => {
  it('Jobs icon creator tests', () => {
    const jobsErrorResult = JobsIconCreator(JobStatus.Error);
    const jobsCanceledResult = JobsIconCreator(JobStatus.Canceled);
    const jobsScheduledResult = JobsIconCreator(JobStatus.Scheduled);
    const jobsExecutedResult = JobsIconCreator(JobStatus.Executed);
    const jobsRetryResult = JobsIconCreator(JobStatus.Retry);

    expect(jobsErrorResult.props[children][1]).toEqual('Error');
    expect(jobsCanceledResult.props[children][1]).toEqual('Canceled');
    expect(jobsScheduledResult.props[children][1]).toEqual('Scheduled');
    expect(jobsRetryResult.props[children][1]).toEqual('Retry');
    expect(jobsExecutedResult.props[children][1]).toEqual('Executed');
  });
});

describe('handleSkip tests', () => {
  const onSkipSuccess = jest.fn();
  const onSkipFailure = jest.fn();
  it('success test', async () => {
    const mockDriverHandleSkipSuccess = jest.spyOn(driver, 'handleProcessSkip');
    mockDriverHandleSkipSuccess.mockResolvedValue();
    await handleSkip(processInstance, driver, onSkipSuccess, onSkipFailure);
    await wait(0);
    expect(onSkipSuccess).toHaveBeenCalled();
  });

  it('fails executing skip process', async () => {
    const mockDriverHandleSkipFailed = jest.spyOn(driver, 'handleProcessSkip');
    mockDriverHandleSkipFailed.mockRejectedValue({ message: '403 error' });
    await handleSkip(processInstance, driver, onSkipSuccess, onSkipFailure);
    await wait(0);
    expect(onSkipFailure.mock.calls[0][0]).toEqual('"403 error"');
    expect(onSkipFailure).toHaveBeenCalled();
  });
});

describe('handleRetry tests', () => {
  const onSkipSuccess = jest.fn();
  const onSkipFailure = jest.fn();
  it('success test', async () => {
    const mockDriverHandleProcessRetrySuccess = jest.spyOn(
      driver,
      'handleProcessRetry'
    );
    mockDriverHandleProcessRetrySuccess.mockResolvedValue();
    await handleRetry(processInstance, driver, onSkipSuccess, onSkipFailure);
    await wait(0);
    expect(onSkipSuccess).toHaveBeenCalled();
  });

  it('fails executing retry process', async () => {
    const mockDriverHandleProcessRetryFailed = jest.spyOn(
      driver,
      'handleProcessRetry'
    );
    mockDriverHandleProcessRetryFailed.mockRejectedValue({
      message: '403 error'
    });
    await handleRetry(processInstance, driver, onSkipSuccess, onSkipFailure);
    await wait(0);
    expect(onSkipFailure.mock.calls[0][0]).toEqual('"403 error"');
    expect(onSkipFailure).toHaveBeenCalled();
  });
});

describe('handleNodeInstanceCancel tests', () => {
  const onSkipSuccess = jest.fn();
  const onSkipFailure = jest.fn();
  it('success test', async () => {
    const mockDriverHandleNodeInstanceCancelSuccess = jest.spyOn(
      driver,
      'handleNodeInstanceCancel'
    );
    mockDriverHandleNodeInstanceCancelSuccess.mockResolvedValue();
    await handleNodeInstanceCancel(
      processInstance,
      driver,
      node,
      onSkipSuccess,
      onSkipFailure
    );
    await wait(0);
    expect(onSkipSuccess).toHaveBeenCalled();
  });

  it('fails executing retry process', async () => {
    const mockDriverHandleNodeInstanceCancelFailed = jest.spyOn(
      driver,
      'handleNodeInstanceCancel'
    );
    mockDriverHandleNodeInstanceCancelFailed.mockRejectedValue({
      message: '403 error'
    });
    await handleNodeInstanceCancel(
      processInstance,
      driver,
      node,
      onSkipSuccess,
      onSkipFailure
    );
    await wait(0);
    expect(onSkipFailure.mock.calls[0][0]).toEqual('"403 error"');
    expect(onSkipFailure).toHaveBeenCalled();
  });
});

describe('jobCancel tests', () => {
  it('success test', async () => {
    const successTitle = 'Success';
    const successContent = 'good job';
    const setModalTitle: (title: JSX.Element) => void = jest.fn();
    const setModalContent: (content: string) => void = jest.fn();
    const mockDriverJobCancelSuccess = jest.spyOn(driver, 'cancelJob');

    const jobCancelInstance: JobCancel = {
      modalContent: successContent,
      modalTitle: successTitle
    };
    mockDriverJobCancelSuccess.mockResolvedValue(jobCancelInstance);
    await jobCancel(driver, job, setModalTitle, setModalContent);
    await wait(0);
    expect(setModalTitle).toHaveBeenCalledWith(
      setTitle(successTitle, 'Job cancel')
    );
    expect(setModalContent).toHaveBeenCalledWith(successContent);
  });

  it('failed test', async () => {
    const failedTitle = 'failed';
    const failedContent = 'not good job';
    const setModalTitle: (title: JSX.Element) => void = jest.fn();
    const setModalContent: (content: string) => void = jest.fn();
    const mockDriverJobCancelSuccess = jest.spyOn(driver, 'cancelJob');

    const jobCancelInstance: JobCancel = {
      modalContent: failedContent,
      modalTitle: failedTitle
    };
    mockDriverJobCancelSuccess.mockResolvedValue(jobCancelInstance);
    await jobCancel(driver, job, setModalTitle, setModalContent);
    await wait(0);
    expect(setModalTitle).toHaveBeenCalledWith(
      setTitle(failedTitle, 'Job cancel')
    );
    expect(setModalContent).toHaveBeenCalledWith(failedContent);
  });
});

describe('handleNodeInstanceRetrigger tests', () => {
  const onSkipSuccess = jest.fn();
  const onSkipFailure = jest.fn();
  it('success test', async () => {
    const mockDriverHandleNodeInstanceCancelSuccess = jest.spyOn(
      driver,
      'handleNodeInstanceRetrigger'
    );
    mockDriverHandleNodeInstanceCancelSuccess.mockResolvedValue();
    await handleNodeInstanceRetrigger(
      processInstance,
      driver,
      node,
      onSkipSuccess,
      onSkipFailure
    );
    await wait(0);
    expect(onSkipSuccess).toHaveBeenCalled();
  });

  it('fails executing retry process', async () => {
    const mockDriverHandleNodeInstanceCancelFailed = jest.spyOn(
      driver,
      'handleNodeInstanceRetrigger'
    );
    mockDriverHandleNodeInstanceCancelFailed.mockRejectedValue({
      message: '403 error'
    });
    await handleNodeInstanceRetrigger(
      processInstance,
      driver,
      node,
      onSkipSuccess,
      onSkipFailure
    );
    await wait(0);
    expect(onSkipFailure.mock.calls[0][0]).toEqual('"403 error"');
    expect(onSkipFailure).toHaveBeenCalled();
  });
});

describe('test utils of jobs', () => {
  const repeatInterval = null;
  const repeatLimit = null;
  const scheduleDate = '2020-08-27T03:35:50.147Z';
  const handleRescheduleAction = jest.fn();
  const setRescheduleError: (modalContent: string) => void = jest.fn();

  it('test reschedule function', async () => {
    const successTitle = 'success';
    const successContent = 'good job';
    const mockDriverRescheduleJobSuccess = jest.spyOn(driver, 'rescheduleJob');
    mockDriverRescheduleJobSuccess.mockResolvedValue({
      modalTitle: successTitle,
      modalContent: successContent
    });
    await handleJobRescheduleUtil(
      repeatInterval,
      repeatLimit,
      scheduleDate,
      job,
      handleRescheduleAction,
      driver,
      setRescheduleError
    );
    expect(handleRescheduleAction).toHaveBeenCalled();
  });
  it('test error response for reschedule function', async () => {
    const failedTitle = 'failure';
    const failedContent = 'not good job';
    const mockDriverRescheduleJobSuccess = jest.spyOn(driver, 'rescheduleJob');
    mockDriverRescheduleJobSuccess.mockResolvedValue({
      modalTitle: failedTitle,
      modalContent: failedContent
    });

    await handleJobRescheduleUtil(
      repeatInterval,
      repeatLimit,
      scheduleDate,
      job,
      handleRescheduleAction,
      driver,
      setRescheduleError
    );
    expect(handleRescheduleAction).toHaveBeenCalled();
    expect(setRescheduleError).toHaveBeenCalledWith(failedContent);
  });

  it('test getOmmitedNodesForTimeline with json values', () => {
    const source =
      '{"id":"hello","version":"1.0","specVersion":"0.8.0","name":"HelloWorld","start":"HelloWorld","states":[{"name":"HelloWorld","type":"inject","data":{"message":"HelloWorld"},"end":true}]}';
    const ommittedNodes = getOmmitedNodesForTimeline(nodeInstances, source);
    expect(ommittedNodes).toEqual(['branch-18', 'EmbeddedStart']);
  }),
    it('test getOmmitedNodesForTimeline with yaml values', () => {
      const source =
        "id: hello\nversion: '1.0'\nspecVersion: 0.8.0\nname: Hello World\nstart: HelloWorld\nstates:\n- name: HelloWorld\n  type: inject\n  data:\n    message: Hello World\n  end: true\n";
      const ommittedNodes = getOmmitedNodesForTimeline(nodeInstances, source);
      expect(ommittedNodes).toEqual(['branch-18', 'EmbeddedStart']);
    });

  it('test getOmmitedNodesForTimeline with null values', () => {
    const ommittedNodes = getOmmitedNodesForTimeline([], null);
    expect(ommittedNodes).toEqual([]);
  });

  it('test getSuccessNodes with error node', () => {
    const source =
      '{"id":"hello","version":"1.0","specVersion":"0.8.0","name":"HelloWorld","start":"HelloWorld","states":[{"name":"HelloWorld","type":"inject","data":{"message":"HelloWorld"},"end":true}]}';
    const nodeNames = ['Start', 'HelloWorld', 'End'];
    const errorNode = {
      definitionId: '_jbpm-unique-6',
      enter: new Date('2023-07-19T09:22:43.372Z'),
      exit: new Date('2023-07-19T09:22:43.372Z'),
      id: 'e273f5b2-d52f-4fc3-b45d-8f346952e86f',
      name: 'EmbeddedStart',
      nodeId: '2',
      type: 'EmbeddedStart'
    };
    const successNodes = getSuccessNodes(
      nodeInstances,
      nodeNames,
      source,
      errorNode
    );
    expect(successNodes.length).not.toEqual(0);
    expect(successNodes).toEqual(['Start', 'HelloWorld', 'End']);
  });
});
