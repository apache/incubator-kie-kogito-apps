import { EnvelopeApiFactoryArgs } from '@kie-tools-core/envelope';
import { CustomDashboardViewEnvelopeViewApi } from './CustomDashboardViewEnvelopeView';
import {
  Association,
  CustomDashboardViewChannelApi,
  CustomDashboardViewEnvelopeApi
} from '../api';
import { CustomDashboardViewEnvelopeContext } from './CustomDashboardViewEnvelopeContext';

/**
 * Implementation of the CustomDashboardViewEnvelopeApi
 */
export class CustomDashboardViewEnvelopeApiImpl
  implements CustomDashboardViewEnvelopeApi
{
  private view: () => CustomDashboardViewEnvelopeViewApi;
  private capturedInitRequestYet = false;
  constructor(
    private readonly args: EnvelopeApiFactoryArgs<
      CustomDashboardViewEnvelopeApi,
      CustomDashboardViewChannelApi,
      CustomDashboardViewEnvelopeViewApi,
      CustomDashboardViewEnvelopeContext
    >
  ) {}

  private hasCapturedInitRequestYet() {
    return this.capturedInitRequestYet;
  }

  private ackCapturedInitRequest() {
    this.capturedInitRequestYet = true;
  }

  customDashboardView__init = async (
    association: Association,
    dashboardName: string
  ): Promise<void> => {
    this.args.envelopeClient?.associate(
      association.origin,
      association.envelopeServerId
    );

    if (this.hasCapturedInitRequestYet()) {
      return;
    }
    this.ackCapturedInitRequest();
    this.view = await this.args.viewDelegate();
    this.view().initialize(dashboardName, association.origin);
  };
}
