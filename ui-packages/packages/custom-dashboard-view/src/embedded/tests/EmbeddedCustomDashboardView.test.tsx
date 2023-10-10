import React from 'react';
import { EmbeddedCustomDashboardView } from '../EmbeddedCustomDashboardView';
import { MockedCustomDashboardViewDriver } from './utils/Mocks';
import { render } from '@testing-library/react';

describe('EmbeddedCustomDashboardView tests', () => {
  it('Snapshot', () => {
    const props = {
      dashboardName: 'name',
      targetOrigin: 'origin',
      driver: new MockedCustomDashboardViewDriver()
    };

    const { container } = render(<EmbeddedCustomDashboardView {...props} />);

    const contentDiv = container.querySelector('div');

    expect(contentDiv).toBeTruthy();
    expect(container).toMatchSnapshot();
  });
});
