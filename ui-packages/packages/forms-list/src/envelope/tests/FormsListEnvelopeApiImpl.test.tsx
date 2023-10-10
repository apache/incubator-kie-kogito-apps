import {
  MockedEnvelopeClient,
  MockedFormsListEnvelopeViewApi
} from './mocks/Mocks';
import { EnvelopeApiFactoryArgs } from '@kie-tools-core/envelope';
import { FormsListChannelApi, FormsListEnvelopeApi } from '../../api';
import { FormsListEnvelopeApiImpl } from '../FormsListEnvelopeApiImpl';
import { FormsListEnvelopeViewApi } from '../FormsListEnvelopeView';
import { FormsListEnvelopeContext } from '../FormsListEnvelopeContext';

describe('FormsListEnvelopeApiImpl tests', () => {
  it('initialize', async () => {
    const envelopeClient = MockedEnvelopeClient;
    const view = new MockedFormsListEnvelopeViewApi();
    const args: EnvelopeApiFactoryArgs<
      FormsListEnvelopeApi,
      FormsListChannelApi,
      FormsListEnvelopeViewApi,
      FormsListEnvelopeContext
    > = {
      envelopeClient,
      envelopeContext: {},
      viewDelegate: () => Promise.resolve(() => view)
    };

    const envelopeApi = new FormsListEnvelopeApiImpl(args);

    envelopeApi.formsList__init({
      envelopeServerId: 'envelopeServerId',
      origin: 'origin'
    });

    expect(MockedEnvelopeClient.associate).toHaveBeenCalledWith(
      'origin',
      'envelopeServerId'
    );
    const calledView = await view.initialize;
    expect(calledView).toHaveBeenCalled();
  });
});
