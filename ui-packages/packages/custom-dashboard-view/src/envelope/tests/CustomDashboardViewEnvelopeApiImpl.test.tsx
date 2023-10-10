import {
  MockedEnvelopeClient,
  MockedCustomDashboardViewEnvelopeViewApi
} from './mocks/Mocks';
import { EnvelopeApiFactoryArgs } from '@kie-tools-core/envelope';
import {
  CustomDashboardViewChannelApi,
  CustomDashboardViewEnvelopeApi
} from '../../api';
import { CustomDashboardViewEnvelopeApiImpl } from '../CustomDashboardViewEnvelopeApiImpl';
import { CustomDashboardViewEnvelopeViewApi } from '../CustomDashboardViewEnvelopeView';
import { CustomDashboardViewEnvelopeContext } from '../CustomDashboardViewEnvelopeContext';

describe('CustomDashboardViewEnvelopeApiImpl tests', () => {
  it('initialize', async () => {
    const envelopeClient = MockedEnvelopeClient;
    const view = new MockedCustomDashboardViewEnvelopeViewApi();
    const args: EnvelopeApiFactoryArgs<
      CustomDashboardViewEnvelopeApi,
      CustomDashboardViewChannelApi,
      CustomDashboardViewEnvelopeViewApi,
      CustomDashboardViewEnvelopeContext
    > = {
      envelopeClient,
      envelopeContext: {},
      viewDelegate: () => Promise.resolve(() => view)
    };

    const envelopeApi = new CustomDashboardViewEnvelopeApiImpl(args);

    envelopeApi.customDashboardView__init(
      {
        envelopeServerId: 'envelopeServerId',
        origin: 'origin'
      },
      'name'
    );

    expect(envelopeClient.associate).toHaveBeenCalledWith(
      'origin',
      'envelopeServerId'
    );
    const calledView = await view.initialize;
    expect(calledView).toHaveBeenCalled();
  });
});
