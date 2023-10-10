import React from 'react';
import { render, screen } from '@testing-library/react';
import { PageSectionHeader } from '../PageSectionHeader';
import { BrowserRouter } from 'react-router-dom';

describe('PageSectionHeader tests', () => {
  const props = {
    titleText: 'Process Details',
    breadcrumbText: ['Home', 'Processes'],
    breadcrumbPath: ['/', { pathname: '/ProcessInstances', state: {} }]
  };
  it('Snapshot test with default props', () => {
    const { container } = render(
      <BrowserRouter>
        <PageSectionHeader {...props} />
      </BrowserRouter>
    );
    expect(container).toMatchSnapshot();
  });
});
