import React from 'react';
import { render, screen } from '@testing-library/react';
import { KogitoSpinner } from '../KogitoSpinner';

describe('KogitoSpinner component tests', () => {
  it('snapshot testing with loading test', () => {
    const { container } = render(
      <KogitoSpinner
        spinnerText={'loading...'}
        ouiaId="kogito-spinner-ouia-id"
      />
    );
    expect(container).toMatchSnapshot();
  });
});
