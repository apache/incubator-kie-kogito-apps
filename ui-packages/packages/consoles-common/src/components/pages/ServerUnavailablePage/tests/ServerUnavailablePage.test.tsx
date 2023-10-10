import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import { ServerUnavailablePage } from '../ServerUnavailablePage';

const reload = jest.fn();
process.env.KOGITO_APP_NAME = 'Sample console';

describe('ServerUnavailablePage tests', () => {
  beforeEach(() => {
    reload.mockClear();
  });
  it('Snapshot with default name', () => {
    const { container } = render(<ServerUnavailablePage reload={reload} />);

    expect(container).toMatchSnapshot();

    const emptystates = screen.getAllByTestId('empty-state-body');

    expect(emptystates).toHaveLength(2);
    expect(emptystates[0].textContent).toContain(
      `The ${process.env.KOGITO_APP_NAME} could not access the server to display content.`
    );

    const reset = screen.getByTestId('refresh-button');
    fireEvent.click(reset);

    expect(reload).toHaveBeenCalled();
  });

  it('Snapshot with custom name', () => {
    const customDisplayName: string = 'My custom display Name';

    const { container } = render(
      <ServerUnavailablePage displayName={customDisplayName} reload={reload} />
    );

    expect(container).toMatchSnapshot();

    const emptystates = screen.getAllByTestId('empty-state-body');

    expect(emptystates).toHaveLength(2);
    expect(emptystates[0].textContent).toContain(
      `The ${customDisplayName} could not access the server to display content.`
    );

    const reset = screen.getByTestId('refresh-button');
    fireEvent.click(reset);

    expect(reload).toHaveBeenCalled();
  });
});
