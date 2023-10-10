import React from 'react';
import { render, screen } from '@testing-library/react';

import KogitoAppContextProvider from '../KogitoAppContextProvider';
import KogitoAppContext from '../KogitoAppContext';
import { TestUserContext } from '../TestUserContext';

describe('KogitoAppContextProvider tests', () => {
  it('Snapshot testing', () => {
    const { container } = render(
      <KogitoAppContextProvider userContext={new TestUserContext()}>
        <KogitoAppContext.Consumer>
          {(ctx) => <div />}
        </KogitoAppContext.Consumer>
      </KogitoAppContextProvider>
    );

    expect(container).toMatchSnapshot();

    const MockedComponent = container.querySelector('div');

    expect(MockedComponent).toBeTruthy();
  });
});
