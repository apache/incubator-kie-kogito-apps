import { render } from '@testing-library/react';
import React from 'react';
import JobsManagementContainer from '../JobsManagementContainer';
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

describe('WebApp - JobsManagementContainer tests', () => {
  it('Snapshot test with default values', () => {
    const { container } = render(
      <DevUIAppContextProvider users={[user]} {...appContextProps}>
        <JobsManagementContainer />
      </DevUIAppContextProvider>
    );
    expect(container).toMatchSnapshot();
    expect(container.querySelector('div')).toBeTruthy();
  });
});
