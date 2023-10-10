import React from 'react';
import { render } from '@testing-library/react';
import { ProcessListState } from '@kogito-apps/management-console-shared/dist/types';
import ProcessListContainer from '../ProcessListContainer';
import * as ProcessListContext from '../../../../channel/ProcessList/ProcessListContext';
import { ProcessListQueries } from '../../../../channel/ProcessList/ProcessListQueries';
import { ProcessListGatewayApiImpl } from '../../../../channel/ProcessList/ProcessListGatewayApi';
import DevUIAppContextProvider from '../../../contexts/DevUIAppContextProvider';
import { EmbeddedProcessList } from '@kogito-apps/process-list';
import {
  DefaultUser,
  User
} from '@kogito-apps/consoles-common/dist/environment/auth';

const MockQueries = jest.fn<ProcessListQueries, []>(() => ({
  getProcessInstances: jest.fn(),
  getChildProcessInstances: jest.fn(),
  handleProcessSkip: jest.fn(),
  handleProcessAbort: jest.fn(),
  handleProcessRetry: jest.fn(),
  handleProcessMultipleAction: jest.fn()
}));

jest
  .spyOn(ProcessListContext, 'useProcessListGatewayApi')
  .mockImplementation(() => new ProcessListGatewayApiImpl(new MockQueries()));

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
describe('ProcessListContainer tests', () => {
  const props = {
    initialState: {} as ProcessListState
  };
  it('Snapshot', () => {
    const { container } = render(
      <DevUIAppContextProvider users={[user]} {...appContextProps}>
        <ProcessListContainer {...props} />
      </DevUIAppContextProvider>
    );

    expect(container).toMatchSnapshot();

    expect(container.querySelector('div')).toBeTruthy();
  });
});
