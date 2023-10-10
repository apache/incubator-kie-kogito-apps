import React from 'react';
import { render, screen } from '@testing-library/react';
import MonitoringWebapp from '../MonitoringWebapp';

describe('Monitoring Webapp Tests', () => {
  it('Snapshot - Monitoring Webapp', async () => {
    const container = render(<MonitoringWebapp></MonitoringWebapp>).container;
    expect(container).toMatchSnapshot();
  });
  it('Snapshot - Monitoring Webapp with parameters', async () => {
    const container = render(
      <MonitoringWebapp
        dataIndexUrl="http://localhost:8180"
        dashboard="someDashboard.yml"
        workflow="test123"
      ></MonitoringWebapp>
    ).container;
    expect(container).toMatchSnapshot();
  });
});
