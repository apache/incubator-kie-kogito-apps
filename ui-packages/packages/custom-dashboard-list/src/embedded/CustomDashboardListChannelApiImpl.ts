import {
  CustomDashboardListDriver,
  CustomDashboardListChannelApi,
  CustomDashboardFilter,
  CustomDashboardInfo
} from '../api';

/**
 * Implementation of the CustomDashboardListChannelApiImpl delegating to a CustomDashboardListDriver
 */
export class CustomDashboardListChannelApiImpl
  implements CustomDashboardListChannelApi
{
  constructor(private readonly driver: CustomDashboardListDriver) {}

  customDashboardList__getFilter(): Promise<CustomDashboardFilter> {
    return this.driver.getCustomDashboardFilter();
  }

  customDashboardList__applyFilter(
    customDashboardFilter: CustomDashboardFilter
  ): Promise<void> {
    return this.driver.applyFilter(customDashboardFilter);
  }

  customDashboardList__getCustomDashboardQuery(): Promise<
    CustomDashboardInfo[]
  > {
    return this.driver.getCustomDashboardsQuery();
  }

  customDashboardList__openDashboard(
    customDashboardInfo: CustomDashboardInfo
  ): Promise<void> {
    return this.driver.openDashboard(customDashboardInfo);
  }
}
