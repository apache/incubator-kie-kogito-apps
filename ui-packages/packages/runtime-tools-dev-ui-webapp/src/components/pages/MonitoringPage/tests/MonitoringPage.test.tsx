import React from 'react';
import MonitoringPage from '../MonitoringPage';
import { render } from '@testing-library/react';

describe('MonitoringPage tests', () => {
  it('Snapshot tests', async () => {
    const container = render(<MonitoringPage />).container;

    expect(container).toMatchSnapshot();
  });
});
