import {
  MessageBusClientApi,
  RequestPropertyNames
} from '@kie-tools-core/envelope-bus/dist/api';
import { MockedMessageBusClientApi, processInstance } from './mocks/Mocks';
import ProcessListEnvelopeViewDriver from '../ProcessListEnvelopeViewDriver';
import { ProcessListChannelApi } from '../../api';
import {
  ProcessInstanceState,
  OrderBy,
  ProcessInstanceFilter,
  ProcessListSortBy
} from '@kogito-apps/management-console-shared/dist/types';
import { OperationType } from '@kogito-apps/management-console-shared/dist/components/BulkList';

let channelApi: MessageBusClientApi<ProcessListChannelApi>;
let requests: Pick<
  ProcessListChannelApi,
  RequestPropertyNames<ProcessListChannelApi>
>;
let driver: ProcessListEnvelopeViewDriver;

describe('ProcessListEnvelopeViewDriver tests', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    channelApi = new MockedMessageBusClientApi();
    requests = channelApi.requests;
    driver = new ProcessListEnvelopeViewDriver(channelApi);
  });

  describe('Requests', () => {
    it('initial Load', () => {
      const initialState = {
        filters: {
          status: [ProcessInstanceState.Active]
        },
        sortBy: { lastUpdate: OrderBy.DESC }
      };

      driver.initialLoad(initialState.filters, initialState.sortBy);

      expect(requests.processList__initialLoad).toHaveBeenCalledWith(
        initialState.filters,
        initialState.sortBy
      );
    });

    it('applyFilter', () => {
      const filter: ProcessInstanceFilter = {
        status: [ProcessInstanceState.Active],
        businessKey: []
      };
      driver.applyFilter(filter);

      expect(requests.processList__applyFilter).toHaveBeenCalledWith(filter);
    });

    it('applySorting', () => {
      const sortBy: ProcessListSortBy = {
        lastUpdate: OrderBy.DESC
      };
      driver.applySorting(sortBy);

      expect(requests.processList__applySorting).toHaveBeenCalledWith(sortBy);
    });

    it('handleProcessSkip', () => {
      driver.handleProcessSkip(processInstance);
      expect(requests.processList__handleProcessSkip).toHaveBeenCalledWith(
        processInstance
      );
    });

    it('handleProcessRetry', () => {
      driver.handleProcessRetry(processInstance);
      expect(requests.processList__handleProcessRetry).toHaveBeenCalledWith(
        processInstance
      );
    });

    it('handleProcessAbort', () => {
      driver.handleProcessAbort(processInstance);
      expect(requests.processList__handleProcessAbort).toHaveBeenCalledWith(
        processInstance
      );
    });

    it('handleProcessMultipleAction', () => {
      driver.handleProcessMultipleAction(
        [processInstance],
        OperationType.ABORT
      );
      expect(
        requests.processList__handleProcessMultipleAction
      ).toHaveBeenCalledWith([processInstance], OperationType.ABORT);
    });

    it('query', () => {
      driver.query(0, 10);
      expect(requests.processList__query).toHaveBeenCalledWith(0, 10);
    });

    it('get child query', () => {
      const rootProcessInstanceId = 'a23e6c20-02c2-4c2b-8c5c-e988a0adf863';
      driver.getChildProcessesQuery(rootProcessInstanceId);
      expect(requests.processList__getChildProcessesQuery).toHaveBeenCalledWith(
        rootProcessInstanceId
      );
    });
  });
});
