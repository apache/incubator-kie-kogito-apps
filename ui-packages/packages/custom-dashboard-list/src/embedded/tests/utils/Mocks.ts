import { CustomDashboardListDriver } from '../../../api';

export const MockedCustomDashboardListDriver = jest.fn<
  CustomDashboardListDriver,
  []
>(() => ({
  getCustomDashboardFilter: jest.fn(),
  applyFilter: jest.fn(),
  getCustomDashboardsQuery: jest.fn(),
  openDashboard: jest.fn()
}));
