import { render, screen, fireEvent } from '@testing-library/react';
import React from 'react';
import { KeycloakUnavailablePage } from '../KeycloakUnavailablePage';

describe('KeycloakUnavailablePage test', () => {
  it('render the page', () => {
    const { container } = render(<KeycloakUnavailablePage />);
    expect(container).toMatchSnapshot();
  });
  it('reload button is clicked', () => {
    const location: Location = window.location;
    delete window.location;
    window.location = {
      ...location,
      reload: jest.fn()
    };
    render(<KeycloakUnavailablePage />);
    const button = screen.getByText('click here to retry');
    fireEvent.click(button);
    expect(window.location.reload).toHaveBeenCalled();
    jest.restoreAllMocks();
    window.location = location;
  });
});
