import {
  MockedEnvelopeClient,
  MockedFormDisplayerEnvelopeViewApi
} from './mocks/Mocks';
import { EnvelopeApiFactoryArgs } from '@kie-tools-core/envelope';
import {
  FormDisplayerChannelApi,
  FormDisplayerEnvelopeApi,
  FormType
} from '../../api';
import { FormDisplayerEnvelopeApiImpl } from '../FormDisplayerEnvelopeApiImpl';
import { FormDisplayerEnvelopeViewApi } from '../FormDisplayerEnvelopeView';
import { FormDisplayerEnvelopeContext } from '../FormDisplayerEnvelopeContext';

describe('FormDisplayerEnvelopeApiImpl tests', () => {
  it('initialize', async () => {
    const envelopeClient = MockedEnvelopeClient;
    const view = new MockedFormDisplayerEnvelopeViewApi();
    const args: EnvelopeApiFactoryArgs<
      FormDisplayerEnvelopeApi,
      FormDisplayerChannelApi,
      FormDisplayerEnvelopeViewApi,
      FormDisplayerEnvelopeContext
    > = {
      envelopeClient,
      envelopeContext: {},
      viewDelegate: () => Promise.resolve(() => view)
    };

    const envelopeApi = new FormDisplayerEnvelopeApiImpl(args);

    envelopeApi.formDisplayer__init(
      {
        envelopeServerId: 'envelopeServerId',
        origin: 'origin'
      },
      {
        form: {
          formInfo: {
            lastModified: new Date('2021-08-23T13:26:02.130Z'),
            name: 'react_hiring_HRInterview',
            type: FormType.TSX
          },
          configuration: {
            resources: {
              scripts: {},
              styles: {}
            },
            schema: 'json schema'
          },
          source: 'react source code'
        }
      }
    );

    expect(envelopeClient.associate).toHaveBeenCalledWith(
      'origin',
      'envelopeServerId'
    );
    const calledView = await view.initForm;
    expect(calledView).toHaveBeenCalled();
  });
});
