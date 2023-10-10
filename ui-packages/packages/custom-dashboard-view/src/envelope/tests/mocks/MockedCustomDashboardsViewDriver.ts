import { CustomDashboardViewDriver } from '../../../api';

export class MockedCustomDashboardViewDriver
  implements CustomDashboardViewDriver
{
  getCustomDashboardContent(name: string): Promise<string> {
    return Promise.resolve('');
  }
}
