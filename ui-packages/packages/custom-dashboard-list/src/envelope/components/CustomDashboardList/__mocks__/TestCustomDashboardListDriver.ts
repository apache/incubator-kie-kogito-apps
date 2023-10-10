import {
  CustomDashboardFilter,
  CustomDashboardInfo,
  CustomDashboardListDriver
} from '../../../../api';

export default class TestCustomDashboardListDriver
  implements CustomDashboardListDriver
{
  private readonly customDashboardInfos: CustomDashboardInfo[];
  constructor(customDashboardInfos: CustomDashboardInfo[]) {
    this.customDashboardInfos = customDashboardInfos;
  }
  applyFilter(customDashboardFilter: CustomDashboardFilter): Promise<void> {
    return Promise.resolve();
  }

  getCustomDashboardFilter(): Promise<CustomDashboardFilter> {
    return Promise.resolve({ customDashboardNames: ['age.dash.yaml'] });
  }

  getCustomDashboardsQuery(): Promise<CustomDashboardInfo[]> {
    return Promise.resolve(this.customDashboardInfos);
  }

  openDashboard(customDashboardInfo: CustomDashboardInfo): Promise<void> {
    return Promise.resolve();
  }
}
