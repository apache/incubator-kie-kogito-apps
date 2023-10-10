import React from 'react';
import { render } from '@testing-library/react';
import TaskInboxContainer from '../TaskInboxContainer';
import * as TaskInboxContext from '../../../../channel/TaskInbox/TaskInboxContext';
import { TaskInboxQueries } from '../../../../channel/TaskInbox/TaskInboxQueries';
import { TaskInboxGatewayApiImpl } from '../../../../channel/TaskInbox/TaskInboxGatewayApi';
import DevUIAppContextProvider from '../../../contexts/DevUIAppContextProvider';

const MockQueries = jest.fn<TaskInboxQueries, []>(() => ({
  getUserTaskById: jest.fn(),
  getUserTasks: jest.fn()
}));
const getCurrentUser = jest.fn();
jest
  .spyOn(TaskInboxContext, 'useTaskInboxGatewayApi')
  .mockImplementation(
    () => new TaskInboxGatewayApiImpl(new MockQueries(), getCurrentUser)
  );

const appContextProps = {
  devUIUrl: 'http://localhost:9000',
  openApiPath: '/mocked',
  isProcessEnabled: false,
  isTracingEnabled: false,
  omittedProcessTimelineEvents: [],
  isStunnerEnabled: false,
  availablePages: [],
  customLabels: {
    singularProcessLabel: 'test-singular',
    pluralProcessLabel: 'test-plural'
  },
  diagramPreviewSize: { width: 100, height: 100 }
};

describe('TaskInboxContainer tests', () => {
  it('Snapshot', () => {
    const { container } = render(
      <DevUIAppContextProvider
        users={[{ id: 'John snow', groups: ['admin'] }]}
        {...appContextProps}
      >
        <TaskInboxContainer />
      </DevUIAppContextProvider>
    );

    expect(container).toMatchSnapshot();

    expect(container.querySelector('div')).toBeTruthy();
  });
});
