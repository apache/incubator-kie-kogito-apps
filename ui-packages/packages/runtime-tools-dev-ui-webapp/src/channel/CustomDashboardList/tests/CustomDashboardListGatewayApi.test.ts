import {
  CustomDashboardListGatewayApi,
  CustomDashboardListGatewayApiImpl,
  OnOpenDashboardListener
} from '../CustomDashboardListGatewayApi';
import { getCustomDashboard } from '../../apis';
import { CustomDashboardInfo } from '@kogito-apps/custom-dashboard-list';

jest.mock('../../apis/apis', () => ({
  getCustomDashboard: jest.fn()
}));

let gatewayApi: CustomDashboardListGatewayApi;

describe('CustomDashboardListGatewayApi tests', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    gatewayApi = new CustomDashboardListGatewayApiImpl();
  });

  it('applyFilter', async () => {
    const filter = {
      customDashboardNames: ['dashboard1']
    };
    gatewayApi.applyFilter(filter);
    expect(await gatewayApi.getCustomDashboardFilter()).toEqual(filter);
  });

  it('getCustomDashboard', async () => {
    const filter = {
      customDashboardNames: ['dashboard1']
    };
    gatewayApi.applyFilter(filter);
    gatewayApi.getCustomDashboardsQuery();
    expect(await gatewayApi.getCustomDashboardFilter()).toEqual(filter);
  });

  it('openDashboard', () => {
    const dashboardInfo: CustomDashboardInfo = {
      name: 'dashboard',
      path: '/user/html',
      lastModified: new Date(2020, 6, 12)
    };
    const listener: OnOpenDashboardListener = {
      onOpen: jest.fn()
    };

    const unsubscribe = gatewayApi.onOpenCustomDashboardListen(listener);

    gatewayApi.openDashboard(dashboardInfo);

    expect(listener.onOpen).toHaveBeenLastCalledWith(dashboardInfo);

    unsubscribe.unSubscribe();
  });
});
