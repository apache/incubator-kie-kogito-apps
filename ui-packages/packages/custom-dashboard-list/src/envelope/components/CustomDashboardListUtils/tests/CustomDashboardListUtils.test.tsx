import {
  getDateColumn,
  getDashboardNameColumn
} from '../CustomDashboardListUtils';

describe('CustomDashboard list utils tests', () => {
  it('get CustomDashboard name column', () => {
    const result = getDashboardNameColumn(jest.fn());
    expect(result.label).toEqual('Name');
    expect(result.path).toEqual('name');
  });
  it('get CustomDashboard date column', () => {
    const result = getDateColumn('lastModified', 'Last Modified');
    expect(result.label).toEqual('Last Modified');
    expect(result.path).toEqual('lastModified');
  });
});
