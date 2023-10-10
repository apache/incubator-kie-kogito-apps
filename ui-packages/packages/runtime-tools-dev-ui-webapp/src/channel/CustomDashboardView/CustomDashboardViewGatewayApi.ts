/* eslint-disable @typescript-eslint/no-empty-interface */

import { getCustomDashboardContent } from '../apis';

export interface CustomDashboardViewGatewayApi {
  getCustomDashboardContent(name: string): Promise<string>;
}

export class CustomDashboardViewGatewayApiImpl
  implements CustomDashboardViewGatewayApi
{
  constructor() {}
  getCustomDashboardContent(name: string): Promise<string> {
    return getCustomDashboardContent(name);
  }
}
