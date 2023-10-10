import React from 'react';
import { EmbeddedCustomDashboardList } from '../EmbeddedCustomDashboardList';
import { MockedCustomDashboardListDriver } from './utils/Mocks';
import { render, screen } from '@testing-library/react';

describe('EmbeddedCustomDashboardList tests', () => {
  it('Snapshot', () => {
    const props = {
      targetOrigin: 'origin',
      envelopePath: 'path',
      driver: new MockedCustomDashboardListDriver()
    };

    const { container } = render(<EmbeddedCustomDashboardList {...props} />);

    expect(container).toMatchSnapshot();

    const contentDiv = container.querySelector('div');

    expect(contentDiv).toBeTruthy();
  });
});
