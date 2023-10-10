import React from 'react';
import MonitoringContainer from '../MonitoringContainer';
import { act, render } from '@testing-library/react';
import wait from 'waait';

describe('MonitoringPage tests', () => {
  it('Snapshot tests', async () => {
    let container;
    await act(async () => {
      container = render(<MonitoringContainer />).container;
      await wait(500);
    });
    expect(container).toMatchSnapshot();
  });
});
