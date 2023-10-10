import {
  CustomDashboardFilter,
  CustomDashboardInfo
} from './CustomDashboardListEnvelopeApi';
/**
 * Channel Api for CustomDashboard List
 */
export interface CustomDashboardListChannelApi {
  customDashboardList__getFilter(): Promise<CustomDashboardFilter>;
  customDashboardList__applyFilter(
    customDashboardFilter: CustomDashboardFilter
  ): Promise<void>;
  customDashboardList__getCustomDashboardQuery(): Promise<
    CustomDashboardInfo[]
  >;
  customDashboardList__openDashboard(
    customDashboardInfo: CustomDashboardInfo
  ): Promise<void>;
}
