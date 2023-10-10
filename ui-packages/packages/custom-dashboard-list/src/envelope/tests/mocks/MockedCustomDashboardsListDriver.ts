import {
  CustomDashboardFilter,
  CustomDashboardInfo,
  CustomDashboardListDriver
} from '../../../api';

export const customDashboardInfos: CustomDashboardInfo[] = [
  {
    name: 'dashboard1',
    path: '/user/home',
    lastModified: new Date('2022-07-11T18:30:00.000Z')
  },
  {
    name: 'dashboard2',
    path: '/user/home',
    lastModified: new Date('2022-07-11T18:30:00.000Z')
  }
];
export class MockedCustomDashboardListDriver
  implements CustomDashboardListDriver
{
  applyFilter(customDashboardFilter: CustomDashboardFilter): Promise<void> {
    return Promise.resolve();
  }

  getCustomDashboardFilter(): Promise<CustomDashboardFilter> {
    return Promise.resolve({ customDashboardNames: [] });
  }

  getCustomDashboardsQuery(): Promise<CustomDashboardInfo[]> {
    return Promise.resolve(customDashboardInfos);
  }

  openDashboard(customDashboardInfo: CustomDashboardInfo): Promise<void> {
    return Promise.resolve();
  }
}
