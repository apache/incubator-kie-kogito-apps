import {
  CustomDashboardFilter,
  CustomDashboardInfo,
  CustomDashboardListDriver
} from '../../api';
import { CustomDashboardListChannelApiImpl } from '../CustomDashboardListChannelApiImpl';
import { MockedCustomDashboardListDriver } from './utils/Mocks';

let driver: CustomDashboardListDriver;
let api: CustomDashboardListChannelApiImpl;

describe('CustomDashboardListChannelApiImpl tests', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    driver = new MockedCustomDashboardListDriver();
    api = new CustomDashboardListChannelApiImpl(driver);
  });

  it('CustomDashboardList__getFilter', () => {
    api.customDashboardList__getFilter();
    expect(driver.getCustomDashboardFilter).toHaveBeenCalled();
  });

  it('CustomDashboardList__applyFilter', () => {
    const filter: CustomDashboardFilter = {
      customDashboardNames: ['dashboard1']
    };
    api.customDashboardList__applyFilter(filter);
    expect(driver.applyFilter).toHaveBeenCalledWith(filter);
  });

  it('customDashboardList__getCustomDashboardQuery', () => {
    api.customDashboardList__getCustomDashboardQuery();
    expect(driver.getCustomDashboardsQuery).toHaveBeenCalled();
  });

  it('customDashboardList__openDashboard', () => {
    const data: CustomDashboardInfo = {
      name: 'dashboard1',
      path: '/user/home',
      lastModified: new Date(new Date('2022-07-11T18:30:00.000Z'))
    };
    api.customDashboardList__openDashboard(data);
    expect(driver.openDashboard).toHaveBeenCalledWith(data);
  });
});
