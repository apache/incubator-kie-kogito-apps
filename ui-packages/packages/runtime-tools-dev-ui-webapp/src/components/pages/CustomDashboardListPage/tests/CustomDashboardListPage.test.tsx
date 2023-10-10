import React from 'react';
import { render, screen } from '@testing-library/react';
import CustomDashboardListPage from '../CustomDashboardListPage';
import { BrowserRouter } from 'react-router-dom';

jest.mock(
  '../../../containers/CustomDashboardListContainer/CustomDashboardListContainer'
);

describe('CustomDashboardListPage tests', () => {
  it('Snapshot', () => {
    const { container } = render(
      <BrowserRouter>
        <CustomDashboardListPage />
      </BrowserRouter>
    );

    expect(container).toMatchSnapshot();

    expect(
      document.querySelector(
        'body[data-ouia-page-type="custom-dashboard-list"]'
      )
    );
  });
});
