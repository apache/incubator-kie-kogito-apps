import {
  stateIconCreator,
  setTitle,
  handleSkip,
  handleRetry,
  handleAbort,
  isModalOpen,
  modalToggle,
  handleNodeInstanceCancel
} from '../Utils';
import { GraphQL } from '@kogito-apps/common';
import ProcessInstanceState = GraphQL.ProcessInstanceState;
import axios from 'axios';
import wait from 'waait';
jest.mock('axios');
const mockedAxios = axios as jest.Mocked<typeof axios>;
const children = 'children';
describe('uitility function testing', () => {
  it('state icon creator tests', () => {
    const activeTestResult = stateIconCreator(ProcessInstanceState.Active);
    const completedTestResult = stateIconCreator(
      ProcessInstanceState.Completed
    );
    const errorTestResult = stateIconCreator(ProcessInstanceState.Error);
    const suspendedTestResult = stateIconCreator(
      ProcessInstanceState.Suspended
    );
    const abortedTestResult = stateIconCreator(ProcessInstanceState.Aborted);
    expect(activeTestResult.props[children][1]).toEqual('Active');
    expect(completedTestResult.props[children][1]).toEqual('Completed');
    expect(errorTestResult.props[children][1]).toEqual('Error');
    expect(suspendedTestResult.props[children][1]).toEqual('Suspended');
    expect(abortedTestResult.props[children][1]).toEqual('Aborted');
  });

  it('set title tests', () => {
    const successResult = setTitle('success', 'Abort operation');
    const failureResult = setTitle('failure', 'Skip operation');
    expect(successResult.props[children][2]).toEqual('Abort operation');
    expect(failureResult.props[children][2]).toEqual('Skip operation');
  });

  it('isModalOpen tests', () => {
    const skipResult = isModalOpen('Skip operation', true, false);
    expect(skipResult).toBeTruthy();
    const retryResult = isModalOpen('Retry operation', false, true);
    expect(retryResult).toBeTruthy();
  });

  it('modalToggle tests', () => {
    const handleSkipModalToggle = jest.fn();
    const handleRetryModalToggle = jest.fn();
    const skipResult = modalToggle(
      'Skip operation',
      handleSkipModalToggle,
      handleRetryModalToggle
    );
    expect(skipResult).toEqual(handleSkipModalToggle);
    const retryResult = modalToggle(
      'Retry operation',
      handleSkipModalToggle,
      handleRetryModalToggle
    );
    expect(retryResult).toEqual(handleRetryModalToggle);
  });

  describe('handle skip tests', () => {
    const processInstanceData = {
      id: '123',
      processId: 'trav',
      serviceUrl: 'http://localhost:4000',
      state: ProcessInstanceState.Active
    };
    it('executes skip process successfully', async () => {
      const setModalTitle = jest.fn();
      const setTitleType = jest.fn();
      const setModalContent = jest.fn();
      const handleSkipModalToggle = jest.fn();
      mockedAxios.post.mockResolvedValue({});
      handleSkip(
        processInstanceData,
        setModalTitle,
        setTitleType,
        setModalContent,
        handleSkipModalToggle
      );
      await wait(0);
      expect(setModalTitle.mock.calls[0][0]).toEqual('Skip operation');
      expect(setTitleType.mock.calls[0][0]).toEqual('success');
      expect(setModalContent.mock.calls[0][0]).toEqual(
        'Process execution has successfully skipped node which was in error state.'
      );
      expect(handleSkipModalToggle).toHaveBeenCalled();
    });
    it('fails executing skip process', async () => {
      const setModalTitle = jest.fn();
      const setTitleType = jest.fn();
      const setModalContent = jest.fn();
      const handleSkipModalToggle = jest.fn();
      mockedAxios.post.mockRejectedValue({ error: { message: '403 error' } });
      handleSkip(
        processInstanceData,
        setModalTitle,
        setTitleType,
        setModalContent,
        handleSkipModalToggle
      );
      await wait(0);
      expect(setModalTitle.mock.calls[0][0]).toEqual('Skip operation');
      expect(setTitleType.mock.calls[0][0]).toEqual('failure');
      expect(setModalContent.mock.calls[0][0]).toContain(
        'Process execution failed to skip node which is in error state.'
      );
      expect(handleSkipModalToggle).toHaveBeenCalled();
    });
  });

  describe('handle Retry tests', () => {
    const processInstanceData = {
      id: '123',
      processId: 'trav',
      serviceUrl: 'http://localhost:4000',
      state: ProcessInstanceState.Active
    };
    it('executes retry process successfully', async () => {
      const setModalTitle = jest.fn();
      const setTitleType = jest.fn();
      const setModalContent = jest.fn();
      const handleRetryModalToggle = jest.fn();
      mockedAxios.post.mockResolvedValue({});
      handleRetry(
        processInstanceData,
        setModalTitle,
        setTitleType,
        setModalContent,
        handleRetryModalToggle
      );
      await wait(0);
      expect(setModalTitle.mock.calls[0][0]).toEqual('Retry operation');
      expect(setTitleType.mock.calls[0][0]).toEqual('success');
      expect(setModalContent.mock.calls[0][0]).toEqual(
        'Process execution has successfully re-executed node which was in error state.'
      );
      expect(handleRetryModalToggle).toHaveBeenCalled();
    });
    it('fails executing Retry process', async () => {
      const setModalTitle = jest.fn();
      const setTitleType = jest.fn();
      const setModalContent = jest.fn();
      const handleRetryModalToggle = jest.fn();
      mockedAxios.post.mockRejectedValue({ error: { message: '403 error' } });
      handleRetry(
        processInstanceData,
        setModalTitle,
        setTitleType,
        setModalContent,
        handleRetryModalToggle
      );
      await wait(0);
      expect(setModalTitle.mock.calls[0][0]).toEqual('Retry operation');
      expect(setTitleType.mock.calls[0][0]).toEqual('failure');
      expect(setModalContent.mock.calls[0][0]).toContain(
        'Process execution failed to re-execute node which is in error state.'
      );
      expect(handleRetryModalToggle).toHaveBeenCalled();
    });
  });

  describe('handle Abort tests', () => {
    const processInstanceData = {
      id: '123',
      processId: 'trav',
      serviceUrl: 'http://localhost:4000',
      state: ProcessInstanceState.Active
    };
    it('executes Abort process successfully', async () => {
      const setModalTitle = jest.fn();
      const setTitleType = jest.fn();
      const handleAbortModalToggle = jest.fn();
      mockedAxios.delete.mockResolvedValue({});
      handleAbort(
        processInstanceData,
        setModalTitle,
        setTitleType,
        handleAbortModalToggle
      );
      await wait(0);
      expect(setModalTitle.mock.calls[0][0]).toEqual('Abort operation');
      expect(setTitleType.mock.calls[0][0]).toEqual('success');
      expect(handleAbortModalToggle).toHaveBeenCalled();
    });
    it('fails executing Abort process', async () => {
      const setModalTitle = jest.fn();
      const setTitleType = jest.fn();
      const handleAbortModalToggle = jest.fn();
      mockedAxios.delete.mockRejectedValue({});
      handleAbort(
        processInstanceData,
        setModalTitle,
        setTitleType,
        handleAbortModalToggle
      );
      await wait(0);
      expect(setModalTitle.mock.calls[0][0]).toEqual('Abort operation');
      expect(setTitleType.mock.calls[0][0]).toEqual('failure');
      expect(handleAbortModalToggle).toHaveBeenCalled();
    });
  });

  describe('node cancel tests', () => {
    const processInstanceData = {
      id: '8035b580-6ae4-4aa8-9ec0-e18e19809e0b',
      processId: 'trav',
      serviceUrl: 'http://localhost:4000',
      state: ProcessInstanceState.Active,
      nodes: [
        {
          nodeId: '2',
          name: 'Confirm travel',
          definitionId: 'UserTask_2',
          id: '843bd287-fb6e-4ee7-a304-ba9b430e52d8',
          enter: '2019-10-22T04:43:01.148Z',
          exit: null,
          type: 'HumanTaskNode'
        }
      ]
    };

    const nodeObject = {
      nodeId: '2',
      name: 'Confirm travel',
      definitionId: 'UserTask_2',
      id: '843bd287-fb6e-4ee7-a304-ba9b430e52d8',
      enter: '2019-10-22T04:43:01.148Z',
      exit: null,
      type: 'HumanTaskNode'
    };
    it('executes node cancel process successfully', async () => {
      const setModalTitle = jest.fn();
      const setTitleType = jest.fn();
      const setModalContent = jest.fn();
      const handleModalToggle = jest.fn();
      mockedAxios.delete.mockResolvedValue({});
      handleNodeInstanceCancel(
        processInstanceData,
        nodeObject,
        setModalTitle,
        setTitleType,
        setModalContent,
        handleModalToggle
      );
      await wait(0);
      expect(setModalTitle.mock.calls[0][0]).toEqual('Node cancel process');
      expect(setTitleType.mock.calls[0][0]).toEqual('success');
      expect(setModalContent.mock.calls[0][0]).toEqual(
        `The node - ${nodeObject.name} was successfully cancelled`
      );
      expect(handleModalToggle).toHaveBeenCalled();
    });
    it('fails executing node cancel process', async () => {
      const setModalTitle = jest.fn();
      const setTitleType = jest.fn();
      const setModalContent = jest.fn();
      const handleModalToggle = jest.fn();
      mockedAxios.delete.mockRejectedValue({});
      handleNodeInstanceCancel(
        processInstanceData,
        nodeObject,
        setModalTitle,
        setTitleType,
        setModalContent,
        handleModalToggle
      );
      await wait(0);
      expect(setModalTitle.mock.calls[0][0]).toEqual('Node cancel process');
      expect(setTitleType.mock.calls[0][0]).toEqual('failure');
      expect(setModalContent.mock.calls[0][0]).toEqual(
        `The node - ${nodeObject.name} failed to cancel`
      );
      expect(handleModalToggle).toHaveBeenCalled();
    });
  });
});
