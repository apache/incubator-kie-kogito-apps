import { MessageBusClientApi } from '@kie-tools-core/envelope-bus/dist/api';
import {
  CustomDashboardViewChannelApi,
  CustomDashboardViewDriver
} from '../api';

/**
 * Implementation of CustomDashboardViewEnvelopeViewDriver that delegates calls to the channel Api
 */
export default class CustomDashboardViewEnvelopeViewDriver
  implements CustomDashboardViewDriver
{
  constructor(
    private readonly channelApi: MessageBusClientApi<CustomDashboardViewChannelApi>
  ) {}

  getCustomDashboardContent(name: string): Promise<string> {
    return this.channelApi.requests.customDashboardView__getCustomDashboardView(
      name
    );
  }
}
