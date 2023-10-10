import React from 'react';
import { render } from '@testing-library/react';
import ProcessFormContainer from '../ProcessFormContainer';
import * as FormDetailsContext from '../../../../channel/FormDetails/FormDetailsContext';
import { FormDetailsGatewayApiImpl } from '../../../../channel/FormDetails/FormDetailsGatewayApi';
import DevUIAppContextProvider from '../../../contexts/DevUIAppContextProvider';
import {
  DefaultUser,
  User
} from '@kogito-apps/consoles-common/dist/environment/auth';
import { EmbeddedProcessForm } from '@kogito-apps/process-form';
import * as ProcessFormContext from '../../../../channel/ProcessForm/ProcessFormContext';
import { ProcessFormGatewayApi } from '../../../../channel/ProcessForm/ProcessFormGatewayApi';

jest
  .spyOn(FormDetailsContext, 'useFormDetailsGatewayApi')
  .mockImplementation(() => new FormDetailsGatewayApiImpl());

const MockProcessFormGatewayApi = jest.fn<ProcessFormGatewayApi, []>(() => ({
  setBusinessKey: jest.fn(),
  getBusinessKey: jest.fn(),
  currentBusinessKey: '',
  getProcessFormSchema: jest.fn()
}));

const gatewayApi = new MockProcessFormGatewayApi();

jest
  .spyOn(ProcessFormContext, 'useProcessFormGatewayApi')
  .mockImplementation(() => gatewayApi);

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

describe('ProcessFormContainer tests', () => {
  it('Snapshot', () => {
    const props = {
      processDefinitionData: {
        processName: 'process1',
        endpoint: 'http://localhost:4000'
      },
      onSubmitSuccess: jest.fn(),
      onSubmitError: jest.fn()
    };
    const { container } = render(
      <DevUIAppContextProvider users={[user]} {...appContextProps}>
        <ProcessFormContainer {...props} />
      </DevUIAppContextProvider>
    );

    expect(container).toMatchSnapshot();

    expect(container.querySelector('div')).toBeTruthy();
  });
});
