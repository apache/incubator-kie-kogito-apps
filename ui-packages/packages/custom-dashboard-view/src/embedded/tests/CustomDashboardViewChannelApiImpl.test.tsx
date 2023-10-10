import { CustomDashboardViewDriver } from '../../api';
import { CustomDashboardViewChannelApiImpl } from '../CustomDashboardViewChannelApiImpl';
import { MockedCustomDashboardViewDriver } from './utils/Mocks';

let driver: CustomDashboardViewDriver;
let api: CustomDashboardViewChannelApiImpl;

describe('CustomDashboardViewChannelApiImpl tests', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    driver = new MockedCustomDashboardViewDriver();
    api = new CustomDashboardViewChannelApiImpl(driver);
  });

  it('customDashboardView__getCustomDashboardView', () => {
    api.customDashboardView__getCustomDashboardView('name');
    expect(driver.getCustomDashboardContent).toHaveBeenCalledWith('name');
  });
});
