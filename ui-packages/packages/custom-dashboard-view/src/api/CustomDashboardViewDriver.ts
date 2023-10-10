/**
 * Interface that defines a Driver for CustomDashboard views.
 */
export interface CustomDashboardViewDriver {
  getCustomDashboardContent(name: string): Promise<string>;
}
