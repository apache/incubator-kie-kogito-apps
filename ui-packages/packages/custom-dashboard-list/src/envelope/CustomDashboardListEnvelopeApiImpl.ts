import { EnvelopeApiFactoryArgs } from '@kie-tools-core/envelope';
import { CustomDashboardListEnvelopeViewApi } from './CustomDashboardListEnvelopeView';
import {
  Association,
  CustomDashboardListChannelApi,
  CustomDashboardListEnvelopeApi
} from '../api';
import { CustomDashboardListEnvelopeContext } from './CustomDashboardListEnvelopeContext';

/**
 * Implementation of the CustomDashboardListEnvelopeApi
 */
export class CustomDashboardListEnvelopeApiImpl
  implements CustomDashboardListEnvelopeApi
{
  private view: () => CustomDashboardListEnvelopeViewApi;
  private capturedInitRequestYet = false;
  constructor(
    private readonly args: EnvelopeApiFactoryArgs<
      CustomDashboardListEnvelopeApi,
      CustomDashboardListChannelApi,
      CustomDashboardListEnvelopeViewApi,
      CustomDashboardListEnvelopeContext
    >
  ) {}

  private hasCapturedInitRequestYet() {
    return this.capturedInitRequestYet;
  }

  private ackCapturedInitRequest() {
    this.capturedInitRequestYet = true;
  }

  customDashboardList__init = async (
    association: Association
  ): Promise<void> => {
    this.args.envelopeClient.associate(
      association.origin,
      association.envelopeServerId
    );

    if (this.hasCapturedInitRequestYet()) {
      return;
    }

    this.ackCapturedInitRequest();
    this.view = await this.args.viewDelegate();
    this.view().initialize();
  };
}
