import React from 'react';
import { fireEvent, render, screen } from '@testing-library/react';
import CustomDashboardListToolbar from '../CustomDashboardListToolbar';

describe('CustomDashboard list toolbar tests', () => {
  it('render toolbar', () => {
    const { container } = render(
      <CustomDashboardListToolbar
        applyFilter={jest.fn()}
        setFilterDashboardNames={jest.fn()}
        filterDashboardNames={[]}
      />
    );
    expect(container).toMatchSnapshot();
  });

  it('apply filter click', () => {
    const applyFilter = jest.fn();
    render(
      <CustomDashboardListToolbar
        applyFilter={applyFilter}
        setFilterDashboardNames={jest.fn()}
        filterDashboardNames={[]}
      />
    );

    const button = screen.getByTestId('apply-filter');
    fireEvent.click(button);
    expect(applyFilter).toHaveBeenCalled();
  });

  it('reset click', () => {
    const applyFilter = jest.fn();
    render(
      <CustomDashboardListToolbar
        applyFilter={applyFilter}
        setFilterDashboardNames={jest.fn()}
        filterDashboardNames={['dashboard']}
      />
    );
    const resetButton = screen.getAllByText('Reset to default')[1];
    fireEvent.click(resetButton);
    expect(applyFilter).toHaveBeenCalled();
  });
  it('refresh click', () => {
    const applyFilter = jest.fn();
    render(
      <CustomDashboardListToolbar
        applyFilter={applyFilter}
        setFilterDashboardNames={jest.fn()}
        filterDashboardNames={[]}
      />
    );

    const refreshButton = screen.getByTestId('refresh');
    fireEvent.click(refreshButton);
    expect(applyFilter).toHaveBeenCalled();
  });

  it('enter clicked', () => {
    const applyFilter = jest.fn();
    const { container } = render(
      <CustomDashboardListToolbar
        applyFilter={applyFilter}
        setFilterDashboardNames={jest.fn()}
        filterDashboardNames={[]}
      />
    );
    const searchInput = screen.getByTestId('search-input');
    fireEvent.keyPress(searchInput, { key: 'Enter', code: 13 });

    const applyFilterButton = screen.getByTestId('apply-filter');

    fireEvent.click(applyFilterButton);

    expect(applyFilter).toHaveBeenCalled();
  });

  it('on delete chip', () => {
    const applyFilter = jest.fn();
    const { container } = render(
      <CustomDashboardListToolbar
        applyFilter={applyFilter}
        setFilterDashboardNames={jest.fn()}
        filterDashboardNames={['dashboard']}
      />
    );

    const closeButton = screen.getByLabelText('close');
    fireEvent.click(closeButton);

    expect(applyFilter).toHaveBeenCalled();
  });
});
