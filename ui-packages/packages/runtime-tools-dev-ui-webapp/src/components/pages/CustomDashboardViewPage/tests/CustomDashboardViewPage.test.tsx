import React from 'react';
import { render, screen } from '@testing-library/react';
import CustomDashboardViewPage from '../CustomDashboardViewPage';
import { BrowserRouter } from 'react-router-dom';

jest.mock(
  '../../../containers/CustomDashboardViewContainer/CustomDashboardViewContainer'
);

// Date.now = jest.fn(() => 1592000000000); // UTC Fri Jun 12 2020 22:13:20

jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useHistory: () => ({
    location: {
      state: {
        data: 'name'
      }
    }
  })
}));

describe('CustomDashboardViewPage tests', () => {
  it('Snapshot', () => {
    const { container } = render(
      <BrowserRouter>
        <CustomDashboardViewPage />
      </BrowserRouter>
    );

    expect(container).toMatchSnapshot();
  });
});
