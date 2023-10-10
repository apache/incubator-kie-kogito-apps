import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import { PageNotFound } from '../PageNotFound';
import * as H from 'history';
import { match } from 'react-router';
import { BrowserRouter as Router } from 'react-router-dom';

const path = '/xy';
const match: match = {
  isExact: false,
  path,
  url: path,
  params: {}
};
const location = H.createLocation('/processInstances');

const props1 = {
  defaultPath: '/processInstances',
  defaultButton: '',
  location,
  history: H.createMemoryHistory({ keyLength: 0 }),
  match
};

const props2 = {
  defaultPath: '/processInstances',
  defaultButton: '',
  location: {
    state: {
      prev: '/processInstances',
      description: 'some description',
      buttonText: 'button'
    },
    pathname: '',
    search: '',
    hash: ''
  },
  history: H.createMemoryHistory({ keyLength: 0 }),
  match
};

describe('PageNotFound component tests', () => {
  it('snapshot testing without location object', () => {
    const { container } = render(
      <Router>
        <PageNotFound {...props1} />
      </Router>
    );
    expect(container).toMatchSnapshot();
  });

  it('snapshot testing with location object', () => {
    const { container } = render(
      <Router>
        <PageNotFound {...props2} />
      </Router>
    );
    expect(container).toMatchSnapshot();
  });
  /* tslint:disable */
  it('redirect button click', () => {
    const { container } = render(
      <Router>
        <PageNotFound {...props2} />
      </Router>
    );
    const button = screen.getByTestId('redirect-button');
    fireEvent.click(button);
    expect(container).toMatchSnapshot();
  });
});
