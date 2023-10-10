import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import { NoData } from '../NoData';
import { BrowserRouter as Router } from 'react-router-dom';

const props1 = {
  defaultPath: '/processInstances',
  defaultButton: '',
  location: {}
};

const props2 = {
  defaultPath: '/processInstances',
  defaultButton: '',
  location: {
    state: {
      title: 'NoData',
      prev: '/processInstances',
      description: 'some description',
      buttonText: 'button'
    }
  }
};

describe('NoData component tests', () => {
  it('snapshot tests with location object', () => {
    const { container } = render(<NoData {...props1} />);
    expect(container).toMatchSnapshot();
  });

  it('snapshot tests without location object', () => {
    const { container } = render(<NoData {...props2} />);
    expect(container).toMatchSnapshot();
  });
  /* tslint:disable */
  it('redirect button click', async () => {
    const { container } = render(
      <Router>
        <NoData {...props2} />
      </Router>
    );
    const button = screen.getByTestId('redirect-button');
    fireEvent.click(button);
    expect(container).toMatchSnapshot();
  });
});
