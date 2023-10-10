import {
  MockedEnvelopeClient,
  MockedTaskInboxEnvelopeViewApi
} from './mocks/Mocks';
import { EnvelopeApiFactoryArgs } from '@kie-tools-core/envelope';
import { TaskInboxChannelApi, TaskInboxEnvelopeApi } from '../../api';
import { TaskInboxEnvelopeApiImpl } from '../TaskInboxEnvelopeApiImpl';
import { TaskInboxEnvelopeViewApi } from '../TaskInboxEnvelopeView';
import { TaskInboxEnvelopeContext } from '../TaskInboxEnvelopeContext';

describe('TaskInboxEnvelopeApiImpl tests', () => {
  it('initialize', async () => {
    const envelopeClient = MockedEnvelopeClient;
    const view = new MockedTaskInboxEnvelopeViewApi();
    const args: EnvelopeApiFactoryArgs<
      TaskInboxEnvelopeApi,
      TaskInboxChannelApi,
      TaskInboxEnvelopeViewApi,
      TaskInboxEnvelopeContext
    > = {
      envelopeClient,
      envelopeContext: {},
      viewDelegate: () => Promise.resolve(() => view)
    };

    const envelopeApi = new TaskInboxEnvelopeApiImpl(args);

    const activeTaskStates = ['Ready'];
    const allTaskStates = ['Ready', 'Finished'];
    envelopeApi.taskInbox__init(
      {
        envelopeServerId: 'envelopeServerId',
        origin: 'origin'
      },
      {
        initialState: undefined,
        activeTaskStates,
        allTaskStates
      }
    );
    expect(envelopeClient.associate).toHaveBeenCalledWith(
      'origin',
      'envelopeServerId'
    );
    const initializedView = await view.initialize;
    envelopeApi.taskInbox__notify('John');

    expect(initializedView).toHaveBeenCalledWith(
      undefined,
      allTaskStates,
      activeTaskStates
    );
  });
});
