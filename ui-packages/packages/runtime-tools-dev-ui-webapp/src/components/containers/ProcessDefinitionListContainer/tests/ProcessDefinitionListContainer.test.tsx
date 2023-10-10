import React from 'react';
import { render } from '@testing-library/react';
import ProcessDefinitionListContainer from '../ProcessDefinitionListContainer';
import * as ProcessDefinitionListContext from '../../../../channel/ProcessDefinitionList/ProcessDefinitionListContext';
import { ProcessDefinitionListGatewayApiImpl } from '../../../../channel/ProcessDefinitionList/ProcessDefinitionListGatewayApi';
import DevUIAppContextProvider from '../../../contexts/DevUIAppContextProvider';
import {
  DefaultUser,
  User
} from '@kogito-apps/consoles-common/dist/environment/auth';
import { EmbeddedProcessDefinitionList } from '@kogito-apps/process-definition-list';

jest
  .spyOn(ProcessDefinitionListContext, 'useProcessDefinitionListGatewayApi')
  .mockImplementation(
    () =>
      new ProcessDefinitionListGatewayApiImpl(
        'http://localhost:9000',
        '/mocked'
      )
  );

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

describe('ProcessDefinitionListContainer tests', () => {
  it('Snapshot', () => {
    const { container } = render(
      <DevUIAppContextProvider users={[user]} {...appContextProps}>
        <ProcessDefinitionListContainer />
      </DevUIAppContextProvider>
    );

    expect(container).toMatchSnapshot();

    expect(container.querySelector('div')).toBeTruthy();
  });
});
