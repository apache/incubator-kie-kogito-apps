import { mount } from 'enzyme';
import React from 'react';
import { ProcessInstance } from '@kogito-apps/management-console-shared/dist/types';
import ProcessDetailsContainer from '../ProcessDetailsContainer';
import * as ProcessDetailsContext from '../../../../channel/ProcessDetails/ProcessDetailsContext';
import { ProcessDetailsGatewayApiImpl } from '../../../../channel/ProcessDetails/ProcessDetailsGatewayApi';
import { ProcessDetailsQueries } from '../../../../channel/ProcessDetails/ProcessDetailsQueries';

const getJobsMock = jest.fn();
const getProcessDetailsMock = jest.fn();
const handleProcessSkipMock = jest.fn();
const handleProcessAbortMock = jest.fn();
const handleProcessRetryMock = jest.fn();
const getSVGMock = jest.fn();
const jobCancelMock = jest.fn();
const rescheduleJobMock = jest.fn();
const getTriggerableNodesMock = jest.fn();
const handleNodeTriggerMock = jest.fn();
const handleProcessVariableUpdateMock = jest.fn();
const handleNodeInstanceCancelMock = jest.fn();
const handleNodeInstanceRetriggerMock = jest.fn();

const MockQueries = jest.fn<ProcessDetailsQueries, []>(() => ({
  getProcessDetails: getProcessDetailsMock,
  getJobs: getJobsMock,
  handleProcessSkip: handleProcessSkipMock,
  handleProcessAbort: handleProcessAbortMock,
  handleProcessRetry: handleProcessRetryMock,
  getSVG: getSVGMock,
  jobCancel: jobCancelMock,
  rescheduleJob: rescheduleJobMock,
  getTriggerableNodes: getTriggerableNodesMock,
  handleNodeTrigger: handleNodeTriggerMock,
  handleProcessVariableUpdate: handleProcessVariableUpdateMock,
  handleNodeInstanceCancel: handleNodeInstanceCancelMock,
  handleNodeInstanceRetrigger: handleNodeInstanceRetriggerMock
}));

jest
  .spyOn(ProcessDetailsContext, 'useProcessDetailsGatewayApi')
  .mockImplementation(
    () => new ProcessDetailsGatewayApiImpl(new MockQueries())
  );

const processInstance: ProcessInstance = {} as ProcessInstance;

describe('WebApp - ProcessDetailsContainer tests', () => {
  it('Snapshot test with default values', () => {
    const wrapper = mount(
      <ProcessDetailsContainer processInstance={processInstance} />
    ).find('ProcessDetailsContainer');
    expect(wrapper).toMatchSnapshot();
  });
});
