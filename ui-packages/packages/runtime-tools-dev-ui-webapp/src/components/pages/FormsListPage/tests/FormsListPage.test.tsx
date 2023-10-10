import React from 'react';
import { render, screen } from '@testing-library/react';
import FormsListPage from '../FormsListPage';
import { BrowserRouter } from 'react-router-dom';

jest.mock('../../../containers/FormsListContainer/FormsListContainer');

describe('FormsListPage tests', () => {
  it('Snapshot', () => {
    const { container } = render(
      <BrowserRouter>
        <FormsListPage />
      </BrowserRouter>
    );

    expect(container).toMatchSnapshot();

    expect(
      document.querySelector('body[data-ouia-page-type="forms-list"]')
    ).toBeTruthy();
  });
});
