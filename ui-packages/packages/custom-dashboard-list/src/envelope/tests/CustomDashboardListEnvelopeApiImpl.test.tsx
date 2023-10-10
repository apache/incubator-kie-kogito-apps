import {
  MockedEnvelopeClient,
  MockedCustomDashboardListEnvelopeViewApi
} from './mocks/Mocks';
import { EnvelopeApiFactoryArgs } from '@kie-tools-core/envelope';
import {
  CustomDashboardListChannelApi,
  CustomDashboardListEnvelopeApi
} from '../../api';
import { CustomDashboardListEnvelopeApiImpl } from '../CustomDashboardListEnvelopeApiImpl';
import { CustomDashboardListEnvelopeViewApi } from '../CustomDashboardListEnvelopeView';
import { CustomDashboardListEnvelopeContext } from '../CustomDashboardListEnvelopeContext';

describe('CustomDashboardListEnvelopeApiImpl tests', () => {
  it('initialize', async () => {
    const envelopeClient = MockedEnvelopeClient;
    const view = new MockedCustomDashboardListEnvelopeViewApi();
    const args: EnvelopeApiFactoryArgs<
      CustomDashboardListEnvelopeApi,
      CustomDashboardListChannelApi,
      CustomDashboardListEnvelopeViewApi,
      CustomDashboardListEnvelopeContext
    > = {
      envelopeClient,
      envelopeContext: {},
      viewDelegate: () => Promise.resolve(() => view)
    };

    const envelopeApi = new CustomDashboardListEnvelopeApiImpl(args);

    envelopeApi.customDashboardList__init({
      envelopeServerId: 'envelopeServerId',
      origin: 'origin'
    });

    expect(envelopeClient.associate).toHaveBeenCalledWith(
      'origin',
      'envelopeServerId'
    );
    const calledView = await view.initialize;
    expect(calledView).toHaveBeenCalled();
  });
});
