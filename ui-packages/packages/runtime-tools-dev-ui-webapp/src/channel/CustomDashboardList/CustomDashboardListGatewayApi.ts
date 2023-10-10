import {
  CustomDashboardFilter,
  CustomDashboardInfo
} from '@kogito-apps/custom-dashboard-list';
import { getCustomDashboard } from '../apis';

/* eslint-disable @typescript-eslint/no-empty-interface */
export interface CustomDashboardListGatewayApi {
  getCustomDashboardFilter(): Promise<CustomDashboardFilter>;
  applyFilter(customDashboardFilter: CustomDashboardFilter): Promise<void>;
  getCustomDashboardsQuery(): Promise<CustomDashboardInfo[]>;
  openDashboard: (customDashboardInfo: CustomDashboardInfo) => Promise<void>;
  onOpenCustomDashboardListen: (
    listener: OnOpenDashboardListener
  ) => UnSubscribeHandler;
}

export interface OnOpenDashboardListener {
  onOpen: (dashboardInfo: CustomDashboardInfo) => void;
}

export interface UnSubscribeHandler {
  unSubscribe: () => void;
}

export class CustomDashboardListGatewayApiImpl
  implements CustomDashboardListGatewayApi
{
  private _CustomDashboardFilter: CustomDashboardFilter = {
    customDashboardNames: []
  };
  private readonly listeners: OnOpenDashboardListener[] = [];

  getCustomDashboardFilter = (): Promise<CustomDashboardFilter> => {
    return Promise.resolve(this._CustomDashboardFilter);
  };

  applyFilter = (
    customDashboardFilter: CustomDashboardFilter
  ): Promise<void> => {
    this._CustomDashboardFilter = customDashboardFilter;
    return Promise.resolve();
  };

  getCustomDashboardsQuery(): Promise<CustomDashboardInfo[]> {
    return getCustomDashboard(this._CustomDashboardFilter.customDashboardNames);
  }

  openDashboard = (customDashboardInfo: CustomDashboardInfo): Promise<void> => {
    this.listeners.forEach((listener) => listener.onOpen(customDashboardInfo));
    return Promise.resolve();
  };

  onOpenCustomDashboardListen(
    listener: OnOpenDashboardListener
  ): UnSubscribeHandler {
    this.listeners.push(listener);

    const unSubscribe = () => {
      const index = this.listeners.indexOf(listener);
      if (index > -1) {
        this.listeners.splice(index, 1);
      }
    };

    return {
      unSubscribe
    };
  }
}
