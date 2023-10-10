import { EnvelopeApiFactoryArgs } from '@kie-tools-core/envelope';
import {
  MockedEnvelopeClient,
  MockedTaskFormEnvelopeViewApi,
  testUserTask
} from './mocks/Mocks';
import {
  TaskFormChannelApi,
  TaskFormEnvelopeApi,
  TaskFormInitArgs
} from '../../api';
import { TaskFormEnvelopeViewApi } from '../TaskFormEnvelopeView';
import { TaskFormEnvelopeContext } from '../TaskFormEnvelopeContext';
import { TaskFormEnvelopeApiImpl } from '../TaskFormEnvelopeApiImpl';

describe('TaskFormEnvelopeApiImpl tests', () => {
  it('initialize', async () => {
    const envelopeClient = MockedEnvelopeClient;
    const view = new MockedTaskFormEnvelopeViewApi();
    const args: EnvelopeApiFactoryArgs<
      TaskFormEnvelopeApi,
      TaskFormChannelApi,
      TaskFormEnvelopeViewApi,
      TaskFormEnvelopeContext
    > = {
      envelopeClient,
      envelopeContext: {},
      viewDelegate: () => Promise.resolve(() => view)
    };

    const envelopeApi = new TaskFormEnvelopeApiImpl(args);

    const initArgs: TaskFormInitArgs = {
      userTask: testUserTask,
      user: {
        id: 'test',
        groups: ['group1']
      }
    };

    envelopeApi.taskForm__init(
      {
        envelopeServerId: 'envelopeServerId',
        origin: 'origin'
      },
      initArgs
    );

    expect(envelopeClient.associate).toHaveBeenCalledWith(
      'origin',
      'envelopeServerId'
    );

    const initializedView = await view.initialize;
    expect(initializedView).toHaveBeenCalledWith(initArgs);
  });
});
