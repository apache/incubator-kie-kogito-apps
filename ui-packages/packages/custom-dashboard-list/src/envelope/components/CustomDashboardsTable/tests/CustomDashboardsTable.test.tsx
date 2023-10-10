import React from 'react';
import { render, screen } from '@testing-library/react';
import CustomDashboardsTable from '../CustomDashboardsTable';
import {
  customDashboardInfos,
  MockedCustomDashboardListDriver
} from '../../../tests/mocks/MockedCustomDashboardsListDriver';
import { DataTable } from '@kogito-apps/components-common/dist/components/DataTable';

Date.now = jest.fn(() => 1487076708000); //14.02.2017

describe('customDashboard table test', () => {
  const driver = new MockedCustomDashboardListDriver();
  it('renders table', () => {
    const { container } = render(
      <CustomDashboardsTable
        driver={driver}
        isLoading={false}
        customDashboardData={customDashboardInfos}
        setDashboardsData={jest.fn()}
      />
    );
    expect(container).toMatchSnapshot();
    const checkTable = container.querySelector('table');
    expect(checkTable).toBeTruthy();
  });
});
