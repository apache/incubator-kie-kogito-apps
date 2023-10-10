import { TaskDetailsChannelApi, TaskDetailsEnvelopeApi } from '../../api';

import { TaskDetailsEnvelopeViewApi } from '../TaskDetailsEnvelopeView';
import { TaskDetailsEnvelopeContext } from '../TaskDetailsEnvelopeContext';
import { EnvelopeApiFactoryArgs } from '@kie-tools-core/envelope';
import { TaskDetailsEnvelopeApiImpl } from '../TaskDetailsEnvelopeApiImpl';
import {
  MockedEnvelopeClient,
  MockedTaskDetailsEnvelopeViewApi,
  userTask
} from './utils/Mocks';

describe('TaskDetailsEnvelopeApiImpl tests', () => {
  it('init', async () => {
    const envelopeClient = MockedEnvelopeClient;
    const view = new MockedTaskDetailsEnvelopeViewApi();
    const args: EnvelopeApiFactoryArgs<
      TaskDetailsEnvelopeApi,
      TaskDetailsChannelApi,
      TaskDetailsEnvelopeViewApi,
      TaskDetailsEnvelopeContext
    > = {
      envelopeClient,
      envelopeContext: {},
      viewDelegate: () => Promise.resolve(() => view)
    };

    const envelopeApi = new TaskDetailsEnvelopeApiImpl(args);

    envelopeApi.taskDetails__init(
      {
        envelopeServerId: 'envelopeServerId',
        origin: 'origin'
      },
      {
        task: userTask
      }
    );

    expect(envelopeClient.associate).toHaveBeenCalledWith(
      'origin',
      'envelopeServerId'
    );
    const setTaskView = await view.setTask;
    expect(setTaskView).toHaveBeenCalledWith(userTask);
  });
});
