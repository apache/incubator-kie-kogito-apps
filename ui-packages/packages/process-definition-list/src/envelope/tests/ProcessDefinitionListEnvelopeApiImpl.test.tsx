import {
  MockedEnvelopeClient,
  MockedProcessDefinitionListEnvelopeViewApi
} from './mocks/Mocks';
import { EnvelopeApiFactoryArgs } from '@kie-tools-core/envelope';
import {
  ProcessDefinitionListChannelApi,
  ProcessDefinitionListEnvelopeApi
} from '../../api';
import { ProcessDefinitionListEnvelopeApiImpl } from '../ProcessDefinitionListEnvelopeApiImpl';
import { ProcessDefinitionListEnvelopeViewApi } from '../ProcessDefinitionListEnvelopeView';
import { ProcessDefinitionListEnvelopeContext } from '../ProcessDefinitionListEnvelopeContext';

describe('ProcessDefinitionListEnvelopeApiImpl tests', () => {
  it('initialize', async () => {
    const envelopeClient = new MockedEnvelopeClient();
    const view = new MockedProcessDefinitionListEnvelopeViewApi();
    const args: EnvelopeApiFactoryArgs<
      ProcessDefinitionListEnvelopeApi,
      ProcessDefinitionListChannelApi,
      ProcessDefinitionListEnvelopeViewApi,
      ProcessDefinitionListEnvelopeContext
    > = {
      envelopeClient,
      envelopeContext: {},
      viewDelegate: () => Promise.resolve(() => view)
    };

    const envelopeApi = new ProcessDefinitionListEnvelopeApiImpl(args);

    envelopeApi.processDefinitionList__init(
      {
        envelopeServerId: 'envelopeServerId',
        origin: 'origin'
      },
      {
        singularProcessLabel: 'Workflow'
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
