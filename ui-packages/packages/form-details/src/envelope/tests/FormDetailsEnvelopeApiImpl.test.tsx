import {
  MockedEnvelopeClient,
  MockedFormDetailsEnvelopeViewApi
} from './mocks/Mocks';
import { FormType } from '@kogito-apps/forms-list';
import { EnvelopeApiFactoryArgs } from '@kie-tools-core/envelope';
import { FormDetailsChannelApi, FormDetailsEnvelopeApi } from '../../api';
import { FormDetailsEnvelopeApiImpl } from '../FormDetailsEnvelopeApiImpl';
import { FormDetailsEnvelopeViewApi } from '../FormDetailsEnvelopeView';
import { FormDetailsEnvelopeContext } from '../FormDetailsEnvelopeContext';

describe('FormDetailsEnvelopeApiImpl tests', () => {
  it('initialize', async () => {
    const envelopeClient = MockedEnvelopeClient;
    const view = new MockedFormDetailsEnvelopeViewApi();
    const args: EnvelopeApiFactoryArgs<
      FormDetailsEnvelopeApi,
      FormDetailsChannelApi,
      FormDetailsEnvelopeViewApi,
      FormDetailsEnvelopeContext
    > = {
      envelopeClient,
      envelopeContext: {},
      viewDelegate: () => Promise.resolve(() => view)
    };

    const envelopeApi = new FormDetailsEnvelopeApiImpl(args);

    envelopeApi.formDetails__init(
      {
        envelopeServerId: 'envelopeServerId',
        origin: 'origin'
      },
      {
        name: 'form1',
        type: FormType.HTML,
        lastModified: new Date('2020-07-11T18:30:00.000Z')
      }
    );

    expect(envelopeClient.associate).toHaveBeenCalledWith(
      'origin',
      'envelopeServerId'
    );
    const calledView = await view.initialize;
    expect(calledView).toHaveBeenCalled();
  });
});
