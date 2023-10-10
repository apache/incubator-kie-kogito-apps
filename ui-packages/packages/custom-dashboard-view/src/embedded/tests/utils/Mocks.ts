import { CustomDashboardViewDriver } from '../../../api';

export const MockedCustomDashboardViewDriver = jest.fn<
  CustomDashboardViewDriver,
  []
>(() => ({
  getCustomDashboardContent: jest.fn()
}));
