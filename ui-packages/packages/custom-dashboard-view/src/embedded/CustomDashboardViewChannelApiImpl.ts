import {
  CustomDashboardViewChannelApi,
  CustomDashboardViewDriver
} from '../api';

/**
 * Implementation of the CustomDashboardViewChannelApiImpl delegating to a CustomDashboardViewDriver
 */
export class CustomDashboardViewChannelApiImpl
  implements CustomDashboardViewChannelApi
{
  constructor(private readonly driver: CustomDashboardViewDriver) {}

  customDashboardView__getCustomDashboardView(name: string): Promise<string> {
    return this.driver.getCustomDashboardContent(name);
  }
}
