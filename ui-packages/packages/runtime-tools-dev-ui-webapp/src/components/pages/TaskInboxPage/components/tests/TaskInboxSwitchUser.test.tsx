import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import TaskInboxSwitchUser from '../TaskInboxSwitchUser';
import DevUIAppContextProvider from '../../../../contexts/DevUIAppContextProvider';

describe('TaskInboxSwitchUser tests', () => {
  it('Snapshot test with default props', () => {
    const { container } = render(
      <DevUIAppContextProvider users={[{ id: 'John snow', groups: ['admin'] }]}>
        <TaskInboxSwitchUser user="John" />
      </DevUIAppContextProvider>
    );
    expect(container).toMatchSnapshot();
  });

  it('Trigger onSelect event', () => {
    const { container } = render(
      <DevUIAppContextProvider users={[{ id: 'John snow', groups: ['admin'] }]}>
        <TaskInboxSwitchUser user="John" />
      </DevUIAppContextProvider>
    );

    const checkButton = screen.getByLabelText('Applications');
    fireEvent.click(checkButton);

    const checkDropdownText = container.querySelector('a').textContent;
    expect(checkDropdownText).toEqual('John snow');
    fireEvent.click(container.querySelector('a'));
  });
});
