import { MessageBusClientApi } from '@kie-tools-core/envelope-bus/dist/api';
import {
  CustomDashboardFilter,
  CustomDashboardListChannelApi,
  CustomDashboardListDriver,
  CustomDashboardInfo
} from '../api';

/**
 * Implementation of CustomDashboardListDriver that delegates calls to the channel Api
 */
export default class CustomDashboardListEnvelopeViewDriver
  implements CustomDashboardListDriver
{
  constructor(
    private readonly channelApi: MessageBusClientApi<CustomDashboardListChannelApi>
  ) {}

  getCustomDashboardFilter(): Promise<CustomDashboardFilter> {
    return this.channelApi.requests.customDashboardList__getFilter();
  }

  applyFilter(customDashboardFilter: CustomDashboardFilter): Promise<void> {
    return this.channelApi.requests.customDashboardList__applyFilter(
      customDashboardFilter
    );
  }

  getCustomDashboardsQuery(): Promise<CustomDashboardInfo[]> {
    return this.channelApi.requests.customDashboardList__getCustomDashboardQuery();
  }

  openDashboard(customDashboardInfo: CustomDashboardInfo): Promise<void> {
    return this.channelApi.requests.customDashboardList__openDashboard(
      customDashboardInfo
    );
  }
}
