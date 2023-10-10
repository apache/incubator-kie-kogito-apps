import { render } from '@testing-library/react';
import React from 'react';
import JobsManagementPage from '../JobsManagementPage';
import { BrowserRouter } from 'react-router-dom';
import DevUIAppContextProvider from '../../../contexts/DevUIAppContextProvider';
import {
  DefaultUser,
  User
} from '@kogito-apps/consoles-common/dist/environment/auth';

const user: User = new DefaultUser('jon', []);
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

describe('WebApp - JobsManagementPage tests', () => {
  it('Snapshot test with default values', () => {
    const { container } = render(
      <BrowserRouter>
        <DevUIAppContextProvider users={[user]} {...appContextProps}>
          <JobsManagementPage />
        </DevUIAppContextProvider>
      </BrowserRouter>
    );
    expect(container).toMatchSnapshot();
  });
});
