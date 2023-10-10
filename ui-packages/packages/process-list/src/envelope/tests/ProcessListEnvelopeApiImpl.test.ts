import {
  MockedEnvelopeClient,
  MockedProcessListEnvelopeViewApi
} from './mocks/Mocks';
import { EnvelopeApiFactoryArgs } from '@kie-tools-core/envelope';
import { ProcessListChannelApi, ProcessListEnvelopeApi } from '../../api';
import { ProcessListEnvelopeApiImpl } from '../ProcessListEnvelopeApiImpl';
import { ProcessListEnvelopeViewApi } from '../ProcessListEnvelopeView';
import { ProcessListEnvelopeContext } from '../ProcessListEnvelopeContext';
import {
  OrderBy,
  ProcessInstanceState
} from '@kogito-apps/management-console-shared/dist/types';

describe('ProcessListEnvelopeApiImpl tests', () => {
  it('initialize', async () => {
    const envelopeClient = MockedEnvelopeClient;
    const view = new MockedProcessListEnvelopeViewApi();
    const args: EnvelopeApiFactoryArgs<
      ProcessListEnvelopeApi,
      ProcessListChannelApi,
      ProcessListEnvelopeViewApi,
      ProcessListEnvelopeContext
    > = {
      envelopeClient,
      envelopeContext: {},
      viewDelegate: () => Promise.resolve(() => view)
    };

    const envelopeApi = new ProcessListEnvelopeApiImpl(args);

    envelopeApi.processList__init(
      {
        envelopeServerId: 'envelopeServerId',
        origin: 'origin'
      },
      {
        initialState: {
          filters: {
            status: [ProcessInstanceState.Active],
            businessKey: []
          },
          sortBy: {
            lastUpdate: OrderBy.DESC
          }
        },
        singularProcessLabel: 'Workflow',
        pluralProcessLabel: 'Workflows',
        isWorkflow: true
      }
    );

    expect(envelopeClient.associate).toHaveBeenCalledWith(
      'origin',
      'envelopeServerId'
    );
    const initializedView = await view.initialize;
    expect(initializedView).toHaveBeenCalled();
  });
});
