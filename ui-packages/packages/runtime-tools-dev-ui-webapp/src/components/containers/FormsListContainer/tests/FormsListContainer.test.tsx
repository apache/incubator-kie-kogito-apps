import React from 'react';
import { render } from '@testing-library/react';
import FormsListContainer from '../FormsListContainer';
import * as FormsListContext from '../../../../channel/FormsList/FormsListContext';
import { FormsListGatewayApiImpl } from '../../../../channel/FormsList/FormsListGatewayApi';
import DevUIAppContextProvider from '../../../contexts/DevUIAppContextProvider';
import {
  DefaultUser,
  User
} from '@kogito-apps/consoles-common/dist/environment/auth';
import { EmbeddedFormsList } from '@kogito-apps/forms-list';

jest
  .spyOn(FormsListContext, 'useFormsListGatewayApi')
  .mockImplementation(() => new FormsListGatewayApiImpl());

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

describe('FormsListContainer tests', () => {
  it('Snapshot', () => {
    const { container } = render(
      <DevUIAppContextProvider users={[user]} {...appContextProps}>
        <FormsListContainer />
      </DevUIAppContextProvider>
    );

    expect(container).toMatchSnapshot();

    expect(container.querySelector('div')).toBeTruthy();
  });
});
