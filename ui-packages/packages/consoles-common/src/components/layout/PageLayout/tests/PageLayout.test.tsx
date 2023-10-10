import React from 'react';
import * as Keycloak from '../../../../utils/KeycloakClient';
import { render, screen, fireEvent } from '@testing-library/react';

import PageLayout from '../PageLayout';

const props = {
  children: <React.Fragment>children rendered</React.Fragment>,
  BrandSrc: '../../../../static/kogito.png',
  PageNav: <React.Fragment>page Navigation elements</React.Fragment>,
  BrandAltText: 'Kogito logo',
  withHeader: true,
  BrandClick: jest.fn()
};

jest.mock('../../PageToolbar/PageToolbar');

describe('PageLayout component tests', () => {
  const isAuthEnabledMock = jest.spyOn(Keycloak, 'isAuthEnabled');
  isAuthEnabledMock.mockReturnValue(false);

  it('snapshot tests', () => {
    const { container } = render(<PageLayout {...props} />);
    expect(container).toMatchSnapshot();
  });

  it('open with PageSidebar closed', () => {
    const { container } = render(<PageLayout {...props} pageNavOpen={false} />);
    expect(container).toMatchSnapshot();

    let pageSidebar = screen.getByTestId('page-sidebar');
    expect(pageSidebar).toBeTruthy();

    const button = screen.getByLabelText('Global navigation');
    fireEvent.click(button);

    pageSidebar = screen.getByTestId('page-sidebar');
    expect(pageSidebar).toBeTruthy();
    expect(screen.getByText('page Navigation elements')).toBeTruthy();
  });

  it('check isNavOpen boolean', () => {
    const { container } = render(<PageLayout {...props} />);
    const button = screen.getByLabelText('Global navigation');
    fireEvent.click(button);
    expect(screen.getByText('page Navigation elements')).toBeTruthy();
    expect(container).toMatchSnapshot();
  });
});
