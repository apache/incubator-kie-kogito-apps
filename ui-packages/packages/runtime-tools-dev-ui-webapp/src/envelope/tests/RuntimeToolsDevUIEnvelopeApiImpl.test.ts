import {
  MockedEnvelopeClient,
  MockedRuntimeToolsDevUIEnvelopeViewApi
} from './mocks/Mocks';
import { EnvelopeApiFactoryArgs } from '@kie-tools-core/envelope';
import {
  RuntimeToolsDevUIChannelApi,
  RuntimeToolsDevUIEnvelopeApi
} from '../../api';
import { RuntimeToolsDevUIEnvelopeApiImpl } from '../RuntimeToolsDevUIEnvelopeApiImpl';
import { RuntimeToolsDevUIEnvelopeContextType } from '../RuntimeToolsDevUIEnvelopeContext';
import { RuntimeToolsDevUIEnvelopeViewApi } from '../RuntimeToolsDevUIEnvelopeViewApi';

describe('JobsManagementEnvelopeApiImpl tests', () => {
  it('initialize', () => {
    const envelopeClient = new MockedEnvelopeClient();
    const view = new MockedRuntimeToolsDevUIEnvelopeViewApi();
    const args: EnvelopeApiFactoryArgs<
      RuntimeToolsDevUIEnvelopeApi,
      RuntimeToolsDevUIChannelApi,
      RuntimeToolsDevUIEnvelopeViewApi,
      RuntimeToolsDevUIEnvelopeContextType
    > = {
      envelopeClient,
      envelopeContext: {} as any,
      viewDelegate: () => Promise.resolve(() => view)
    };

    const envelopeApi = new RuntimeToolsDevUIEnvelopeApiImpl(args);

    envelopeApi.runtimeToolsDevUI_initRequest(
      {
        envelopeServerId: 'envelopeServerId',
        origin: 'origin'
      },
      {
        users: [],
        dataIndexUrl: '',
        trustyServiceUrl: '',
        page: '',
        devUIUrl: '',
        openApiPath: '',
        isProcessEnabled: true,
        isTracingEnabled: true,
        isStunnerEnabled: false
      }
    );

    expect(envelopeClient.associate).toHaveBeenCalledWith(
      'origin',
      'envelopeServerId'
    );
  });
});
