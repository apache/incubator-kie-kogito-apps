import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import {
  MockedCustomDashboardListDriver,
  customDashboardInfos
} from '../../../tests/mocks/MockedCustomDashboardsListDriver';
import CustomDashboardCard from '../CustomDashboardCard';
import { Card } from '@patternfly/react-core/dist/js/components/Card';

describe('customDashboard card tests', () => {
  Date.now = jest.fn(() => 1487076708000);
  const driver = new MockedCustomDashboardListDriver();
  it('renders card - with tsx', () => {
    const { container } = render(
      <CustomDashboardCard
        driver={driver}
        customDashboardData={customDashboardInfos[0]}
      />
    );
    expect(container).toMatchSnapshot();
  });
  it('renders card - with html', () => {
    const { container } = render(
      <CustomDashboardCard
        driver={driver}
        customDashboardData={customDashboardInfos[1]}
      />
    );
    expect(container).toMatchSnapshot();
  });
  it('simulate click on card', () => {
    const openDashboardSpy = jest.spyOn(driver, 'openDashboard');
    render(
      <CustomDashboardCard
        driver={driver}
        customDashboardData={customDashboardInfos[0]}
      />
    );

    const card = screen.getByTestId('card');
    fireEvent.click(card);

    expect(openDashboardSpy).toHaveBeenCalled();
  });
});
