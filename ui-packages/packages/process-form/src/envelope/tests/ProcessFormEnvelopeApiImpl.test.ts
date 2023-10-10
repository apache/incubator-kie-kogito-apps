import { EnvelopeApiFactoryArgs } from '@kie-tools-core/envelope';
import {
  MockedEnvelopeClient,
  MockedProcessFormEnvelopeViewApi
} from './mocks/Mocks';
import { ProcessFormChannelApi, ProcessFormEnvelopeApi } from '../../api';
import { ProcessFormEnvelopeViewApi } from '../ProcessFormEnvelopeView';
import { ProcessFormEnvelopeContext } from '../ProcessFormEnvelopeContext';
import { ProcessFormEnvelopeApiImpl } from '../ProcessFormEnvelopeApiImpl';

const processDefinitionData = {
  processName: 'process1',
  endpoint: 'http://localhost:4000'
};

describe('ProcessFormEnvelopeApiImpl tests', () => {
  it('initialize', async () => {
    const envelopeClient = new MockedEnvelopeClient();
    const view = new MockedProcessFormEnvelopeViewApi();
    const args: EnvelopeApiFactoryArgs<
      ProcessFormEnvelopeApi,
      ProcessFormChannelApi,
      ProcessFormEnvelopeViewApi,
      ProcessFormEnvelopeContext
    > = {
      envelopeClient,
      envelopeContext: {},
      viewDelegate: () => Promise.resolve(() => view)
    };

    const envelopeApi = new ProcessFormEnvelopeApiImpl(args);

    envelopeApi.processForm__init(
      {
        envelopeServerId: 'envelopeServerId',
        origin: 'origin'
      },
      {
        processDefinition: processDefinitionData
      }
    );

    expect(envelopeClient.associate).toHaveBeenCalledWith(
      'origin',
      'envelopeServerId'
    );
    const initializedView = await view.initialize;
    expect(initializedView).toHaveBeenCalledWith({
      processDefinition: processDefinitionData
    });
  });
});
