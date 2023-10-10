import {
  CustomDashboardFilter,
  CustomDashboardInfo
} from './CustomDashboardListEnvelopeApi';

/**
 * Interface that defines a Driver for CustomDashboardList views.
 */
export interface CustomDashboardListDriver {
  getCustomDashboardFilter(): Promise<CustomDashboardFilter>;
  applyFilter(customDashboardFilter: CustomDashboardFilter): Promise<void>;
  getCustomDashboardsQuery(): Promise<CustomDashboardInfo[]>;
  openDashboard(customDashboardInfo: CustomDashboardInfo): Promise<void>;
}
