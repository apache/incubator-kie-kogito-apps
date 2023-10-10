import {
  CustomDashboardViewGatewayApi,
  CustomDashboardViewGatewayApiImpl
} from '../CustomDashboardViewGatewayApi';
import { getCustomDashboardContent } from '../../apis';

jest.mock('../../apis/apis', () => ({
  getCustomDashboardContent: jest.fn()
}));

let gatewayApi: CustomDashboardViewGatewayApi;

describe('CustomDashboardViewGatewayApiImpl tests', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    gatewayApi = new CustomDashboardViewGatewayApiImpl();
  });
  const result = "it's a yml file";
  it('getCustomDashboardContent', async () => {
    const name = 'name';
    await gatewayApi.getCustomDashboardContent(name);
    expect(getCustomDashboardContent).toHaveBeenCalledWith(name);
  });
});
