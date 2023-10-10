import React from 'react';
import { render, screen } from '@testing-library/react';
import TaskInboxPage from '../TaskInboxPage';
import TaskInboxContainer from '../../../containers/TaskInboxContainer/TaskInboxContainer';
import DevUIAppContextProvider from '../../../contexts/DevUIAppContextProvider';
import { TaskInboxContextProvider } from '../../../../channel/TaskInbox';
import { TaskInboxQueries } from '../../../../channel/TaskInbox/TaskInboxQueries';

jest.mock('../../../containers/TaskInboxContainer/TaskInboxContainer');

describe('TaskInboxPage tests', () => {
  const MockQueries = jest.fn<TaskInboxQueries, []>(() => ({
    getUserTaskById: jest.fn(),
    getUserTasks: jest.fn()
  }));

  const props = {
    ouiaId: 'task-inbox',
    ouiaSafe: true
  };
  it('Snapshot', () => {
    const { container } = render(
      <DevUIAppContextProvider
        isProcessEnabled={true}
        isTracingEnabled={false}
        users={[{ id: 'John snow', groups: ['admin'] }]}
      >
        <TaskInboxContextProvider apolloClient={new MockQueries()}>
          <TaskInboxPage {...props} />
        </TaskInboxContextProvider>
      </DevUIAppContextProvider>
    );

    expect(container).toMatchSnapshot();

    const checkTaskInboxPage = container.querySelector('h1').textContent;
    expect(checkTaskInboxPage).toEqual('Task Inbox');
  });
});
